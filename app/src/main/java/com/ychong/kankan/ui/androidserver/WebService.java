package com.ychong.kankan.ui.androidserver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.ychong.kankan.entity.EventBusMessage;
import com.ychong.kankan.utils.BaseContract;
import com.ychong.kankan.utils.BaseUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class WebService extends Service {
    FileUploadHolder fileUploadHolder = new FileUploadHolder();
    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer mAsyncServer = new AsyncServer();

    public static void start(Context context) {
        Intent intent = new Intent(context, WebService.class);
        intent.setAction(BaseContract.ACTION_START_WEB_SERVICE);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, WebService.class);
        intent.setAction(BaseContract.ACTION_STOP_WEB_SERVICE);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (BaseContract.ACTION_START_WEB_SERVICE.equals(action)) {
                startServer();
            } else if (BaseContract.ACTION_STOP_WEB_SERVICE.equals(action)) {
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
        if (mAsyncServer != null) {
            mAsyncServer.stop();
        }
    }

    private void startServer() {
        server.get("/images/.*", this::sendResources);
        server.get("/scripts/.*", this::sendResources);
        server.get("/css/.*", this::sendResources);
        //index page
        server.get("/", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            try {
                response.send(BaseUtils.getIndexContent(this,"wifi/index.html"));
            } catch (IOException e) {
                e.printStackTrace();
                response.code(500).end();
            }
        });
        //query upload list
        server.get("/files", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            JSONArray array = new JSONArray();
            File dir = BaseContract.DIR;
            if (dir.exists() && dir.isDirectory()) {
                String[] fileNames = dir.list();
                if (fileNames != null) {
                    for (String fileName : fileNames) {
                        File file = new File(dir, fileName);
                        if (file.exists() && file.isFile()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("name", fileName);
                                long fileLen = file.length();
                                DecimalFormat df = new DecimalFormat("0.00");
                                if (fileLen > 1024 * 1024) {
                                    jsonObject.put("size", df.format(fileLen * 1f / 1024 / 1024) + "MB");
                                } else if (fileLen > 1024) {
                                    jsonObject.put("size", df.format(fileLen * 1f / 1024) + "KB");
                                } else {
                                    jsonObject.put("size", fileLen + "B");
                                }
                                array.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            response.send(array.toString());
        });
        //delete
        server.post("/files/.*", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            final UrlEncodedFormBody body = (UrlEncodedFormBody) request.getBody();
            if ("delete".equalsIgnoreCase(body.get().getString("_method"))) {
                String path = request.getPath().replace("/files/", "");
                try {
                    path = URLDecoder.decode(path, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                File file = new File(BaseContract.DIR, path);
                if (file.exists() && file.isFile()) {
                    file.delete();
                    EventBus.getDefault().post(new EventBusMessage(BaseContract.RxBusEventType.LOAD_BOOK_LIST, 0));
                }
            }
            response.end();
        });
        //download
        server.get("/files/.*", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            String path = request.getPath().replace("/files/", "");
            try {
                path = URLDecoder.decode(path, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            File file = new File(BaseContract.DIR, path);
            if (file.exists() && file.isFile()) {
                try {
                    response.getHeaders().add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                response.sendFile(file);
                return;
            }
            response.code(404).send("Not found!");
        });
        //upload
        server.post("/files", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
                    final MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
                    body.setMultipartCallback((Part part) -> {
                        if (part.isFile()) {
                            body.setDataCallback((DataEmitter emitter, ByteBufferList bb) -> {
                                fileUploadHolder.write(bb.getAllByteArray());
                                bb.recycle();
                            });
                        } else {
                            if (body.getDataCallback() == null) {
                                body.setDataCallback((DataEmitter emitter, ByteBufferList bb) -> {
                                    try {
                                        String fileName = URLDecoder.decode(new String(bb.getAllByteArray()), "UTF-8");
                                        fileUploadHolder.setFileName(fileName);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    bb.recycle();
                                });
                            }
                        }
                    });
                    request.setEndCallback((Exception e) -> {
                        fileUploadHolder.reset();
                        response.end();
                        EventBus.getDefault().post(new EventBusMessage(BaseContract.RxBusEventType.LOAD_BOOK_LIST, 0));
                    });
                });
        server.get("/progress/.*", (final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) -> {
                    JSONObject res = new JSONObject();

                    String path = request.getPath().replace("/progress/", "");

                    if (path.equals(fileUploadHolder.fileName)) {
                        try {
                            res.put("fileName", fileUploadHolder.fileName);
                            res.put("size", fileUploadHolder.totalSize);
                            res.put("progress", fileUploadHolder.fileOutPutStream == null ? 1 : 0.1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    response.send(res);
                });
        server.listen(mAsyncServer, BaseContract.HTTP_PORT);
    }

    private void sendResources(final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
        try {
            String fullPath = request.getPath();
            fullPath = fullPath.replace("%20", " ");
            String resourceName = fullPath;
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1);
            }
            if (resourceName.indexOf("?") > 0) {
                resourceName = resourceName.substring(0, resourceName.indexOf("?"));
            }
            if (!TextUtils.isEmpty(BaseUtils.getContentTypeByResourceName(resourceName))) {
                response.setContentType(BaseUtils.getContentTypeByResourceName(resourceName));
            }
            BufferedInputStream bInputStream = new BufferedInputStream(getAssets().open("wifi/" + resourceName));
            response.sendStream(bInputStream, bInputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            response.code(404).end();
            return;
        }
    }


}
