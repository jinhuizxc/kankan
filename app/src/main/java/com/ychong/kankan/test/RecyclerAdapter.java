package com.ychong.kankan.test;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> implements IHandler {
    private static final String TAG = RecyclerAdapter.class.getSimpleName();
    private static final int MSG_SHOW_NO_MORE = 1;
    private static final int MSG_SHOW_LOAD_MORE = 2;
    private static final int MSG_SHOW_LOAD_MORE_ERROR = 3;

    public static final int HEADER_TYPE = 111;
    public static final int FOOTER_TYPE = 222;
    public static final int STATUS_TYPE = 333;

    private Context mContext;
    private List<T> mData = new ArrayList<>();
    private int mViewCount = 0;
    protected View mStatusView;
    private View headerView;
    private View footerView;
    private View mLoadMoreLayout;
    private View mLoadMoreView;
    private TextView mLoadMoreError;
    private TextView mNoMoreView;
    private WeakHandler mHandler = new WeakHandler(this);

    protected List<Action> mLoadMoreActions = new ArrayList<>();
    protected List<Action> mErrorActions = new ArrayList<>();

    // 是否可加载更多
    protected boolean mLoadMoreEnable = false;
    // 是否可显示 no more
    protected boolean mNoMoreEnable = false;
    // 是否正在显示 load more
    protected boolean mIsLoadMoring = false;
    // 是否正在显示 no more
    protected boolean mIsNoMoring = false;

    private boolean hasHeader = false;
    private boolean hasFooter = false;


    public RecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public RecyclerAdapter(Context context, List<T> data) {
        mContext = context;
        this.mData = data;
        mViewCount += data.size();
        notifyDataSetChanged();
    }

    public void setLoadMoreEnable(boolean b) {
        mLoadMoreEnable = b;
        initEndStatusView();
    }

    public void setShowNoMoreEnable(boolean b) {
        mNoMoreEnable = b;
        initEndStatusView();
    }

    private void initEndStatusView() {
        if (hasEndStatusView() && mStatusView == null) {
            mStatusView = LayoutInflater.from(mContext).inflate(R.layout.view_status_last, null);
            mStatusView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreLayout = mStatusView.findViewById(R.id.load_more_Layout);
            mLoadMoreView = mStatusView.findViewById(R.id.load_more_loading);
            mLoadMoreError = mStatusView.findViewById(R.id.load_more_error);
            mNoMoreView = mStatusView.findViewById(R.id.no_more_view);
            mViewCount++;
            mLoadMoreError.setOnClickListener(view -> {
                mHandler.sendEmptyMessage(MSG_SHOW_LOAD_MORE);
                for (Action action : mErrorActions) {
                    action.onAction();
                }
            });

        }
    }

    public boolean isShowNoMoring() {
        return mIsNoMoring;
    }


    private boolean hasEndStatusView() {
        return mLoadMoreEnable || mNoMoreEnable;
    }


    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            return new BaseViewHolder<>(headerView);
        } else if (viewType == FOOTER_TYPE) {
            return new BaseViewHolder<>(footerView);
        } else if (viewType == STATUS_TYPE) {
            return new BaseViewHolder<>(mStatusView);
        } else {
            return onCreateBaseViewHolder(parent, viewType);
        }
    }

    public abstract BaseViewHolder<T> onCreateBaseViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        if (position < 0) {
            return;
        }
        int dataSize = mData.size();
        if (hasEndStatusView()) {
            if (!hasHeader && !hasFooter && position < dataSize) {
                //没有header和footer
                holder.setData(mData.get(position - 1));
            } else if (hasHeader && !hasFooter && position > 0 && position < mViewCount - 1 && position - 1 < dataSize) {
                //有header 没有footer
                holder.setData(mData.get(position-1));
            }else if (!hasHeader&&position<mViewCount-2&&position<dataSize){
                //没有header 有footer
                holder.setData(mData.get(position));
            }else if (position>0&&position<mViewCount-2&&position-1<dataSize){
                //header footer都有
                holder.setData(mData.get(position-1));
            }
        }else {
            if (!hasHeader&&!hasFooter&&position<dataSize){
                //没有header和footer
                holder.setData(mData.get(position));
            }else if (hasHeader&&!hasFooter&&position>0&&position<mViewCount&&position-1<dataSize){
                //有header没有footer
                holder.setData(mData.get(position-1));
            }else if (!hasHeader&&position<mViewCount-1&&position<dataSize){
                //没有header 有footer
                holder.setData(mData.get(position));
            }else if (position>0&&position<mViewCount-1&&position-1<dataSize){
                //header和footer都有
                holder.setData(mData.get(position-1));
            }
        }
        //最后一个可见的item时 加载更多，解决remove时bug
        if (mLoadMoreEnable&&!mIsNoMoring&&
        !mIsLoadMoring&&isValidLoadMore(position)){
            performLoadMore();
        }
    }

    private   void performLoadMore(){
        mIsLoadMoring = true;
        setViewVisible(mLoadMoreLayout,true);
        setViewVisible(mLoadMoreView,true);
        setViewVisible(mLoadMoreError,false);
        setViewVisible(mNoMoreView,false);
        for (Action action:mLoadMoreActions){
            action.onAction();
        }
    }

    private   void setViewVisible(View view, boolean b){
        if (view!=null){
            view.setVisibility(b?View.VISIBLE:View.GONE);
        }
    }

    /**
     * load more 当前位置view是否有效
     * @param position
     * @return
     */
    private   boolean isValidLoadMore(int position){
        if (hasEndStatusView()){
            //倒数第二个item就load more ,提前触发（解决一些极短case导致bug 并提前加载数据 ，提高加载效率）
            if (hasHeader){
                return position>1&&position==mViewCount-3&&mViewCount!=2;
            }else {
                return position>0&&position==mViewCount-2&&mViewCount!=1;
            }
        }else {
            return false;
        }
    }

    /**
     * ViewHolder 根据item 的位置选择ViewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //header
        if (hasHeader&&position==0){
            return HEADER_TYPE;
        }
        //footer
        if (hasFooter&&hasEndStatusView()&&position==mViewCount-2){
            return FOOTER_TYPE;
        }else if (hasFooter&&!hasEndStatusView()&&position == mViewCount-1){
            return FOOTER_TYPE;
        }
        //status
        if (hasEndStatusView()&&position == mViewCount-1){
            return STATUS_TYPE;
        }
        return super.getItemViewType(position);
    }

    public void showNoMore(){
        if (!mNoMoreEnable){
            return;
        }
        mIsNoMoring = true;
        mHandler.sendEmptyMessage(MSG_SHOW_NO_MORE);
    }
    public void showLoadMoreError(){
        if (!mLoadMoreEnable){
            return;
        }
        mHandler.sendEmptyMessage(MSG_SHOW_LOAD_MORE_ERROR);
    }
    public void addLoadMoreErrorAction(Action action){
        if (action == null){
            return;
        }
        mErrorActions.add(action);
    }
    public void openLoadMore(){
        mIsNoMoring = false;
        mHandler.sendEmptyMessage(MSG_SHOW_LOAD_MORE);
    }
    public void addLoadMoreAction(Action action){
        if (action == null){
            return;
        }
        mLoadMoreActions.add(action);
    }
    public void add(T obj){
        if (!mIsNoMoring&&obj!=null){
            mIsLoadMoring = false;
            mData.add(obj);
            int position;
            if (hasFooter&&hasEndStatusView()){
                position = mViewCount-2;
            }else if (hasFooter&&!hasEndStatusView()){
                position = mViewCount-1;
            }else if (!hasFooter&&hasEndStatusView()){
                position = mViewCount-1;
            }else {
                position = mViewCount;
            }
            mViewCount++;
            notifyItemInserted(position);
        }
    }
    public void insert(T object, int itemPosition) {
        int maxPosition = hasEndStatusView() ? mViewCount - 2 : mViewCount - 1;
        if (mData != null && itemPosition < maxPosition && object != null) {
            int dataPosition;
            if (hasHeader) {
                dataPosition = itemPosition - 1;
            } else {
                dataPosition = itemPosition;
            }
            mData.add(dataPosition, object);
            mViewCount++;
            notifyItemInserted(itemPosition);
        }
    }
    public void addAll(List<T> data) {
        if (data == null) {
            return;
        }
        int size = data.size();
        if (!mIsNoMoring && size > 0) {
            mIsLoadMoring = false;
            mData.addAll(data);
            int positionStart;
            if (hasFooter && hasEndStatusView()) {
                positionStart = mViewCount - 2;
            } else if (hasFooter && !hasEndStatusView()) {
                positionStart = mViewCount - 1;
            } else if (!hasFooter && hasEndStatusView()) {
                positionStart = mViewCount - 1;
            } else {
                positionStart = mViewCount;
            }
            mViewCount += size;
            notifyItemRangeInserted(positionStart, size);
        }
    }
    public void addAll(T[] objects) {
        addAll(Arrays.asList(objects));
    }
    public void replace(T object, int itemPosition) {
        if (mData != null && object != null) {
            int dataPosition;
            if (hasHeader) {
                dataPosition = itemPosition - 1;
            } else {
                dataPosition = itemPosition;
            }
            if (dataPosition < mData.size()) {
                mData.set(dataPosition, object);
                mViewCount++;
                notifyItemChanged(itemPosition);
            }
        }
    }
    //position start with 0
    public void remove(T object) {
        if (object != null && !mData.contains(object)) {
            return;
        }
        int dataPosition = mData.indexOf(object);
        int itemPosition;
        if (hasHeader) {
            itemPosition = dataPosition + 1;
        } else {
            itemPosition = dataPosition;
        }
        remove(itemPosition);
    }
    /**
     * positionItem start with 0
     */
    public void remove(int itemPosition) {
        int dataPosition;
        int dataSize = mData.size();
        if (hasHeader) {
            dataPosition = itemPosition - 1;
            if (dataPosition >= 0 && dataPosition < dataSize) {
                mData.remove(dataPosition);
                notifyItemRemoved(itemPosition);
                mViewCount--;
            } else if (dataPosition >= dataSize) {
                throw new IllegalArgumentException("itemPosition is greater than data size");
            } else {
                throw new IndexOutOfBoundsException("RecyclerView has header,position is should more than 0 ." +
                        "if you want remove header , pleasure user removeHeader()");
            }
        } else {
            dataPosition = itemPosition;
            if (dataPosition >= dataSize) {
                throw new IllegalArgumentException("itemPosition is greater than data size");
            } else {
                mData.remove(dataPosition);
                notifyItemRemoved(itemPosition);
                mViewCount--;
            }
        }
    }

    public void clear() {
        mData.clear();
        mViewCount = hasEndStatusView() ? 1 : 0;
        if (hasHeader) {
            mViewCount++;
        }
        if (hasFooter) {
            mViewCount++;
        }
        mIsNoMoring = false;
        mIsLoadMoring = false;
        setViewVisible(mLoadMoreLayout, false);
        setViewVisible(mNoMoreView, false);
        notifyDataSetChanged();
    }

    /**
     * header 不负责数据加载，不会调用 onBindViewHolder
     */
    public void setHeader(View header) {
        hasHeader = true;
        headerView = header;
        mViewCount++;
    }

    public void setHeader(@LayoutRes int res) {
        setHeader(LayoutInflater.from(mContext).inflate(res, null));
    }

    public View getHeader() {
        return headerView;
    }

    public View getFooter() {
        return footerView;
    }

    public void setFooter(View footer) {
        hasFooter = true;
        footerView = footer;
        mViewCount++;
    }

    /**
     * Footer 这里不负责数据的加载
     *
     * @param res
     */
    public void setFooter(@LayoutRes int res) {
        setFooter(LayoutInflater.from(mContext).inflate(res, null));
    }

    public void removeHeader() {
        if (hasHeader) {
            hasHeader = false;
            notifyItemRemoved(0);
        }
    }

    public void removeFooter() {
        if (hasFooter) {
            hasFooter = false;
            if (hasEndStatusView() && mViewCount > 1) {
                notifyItemRemoved(mViewCount - 2);
            } else if (!hasEndStatusView() && mViewCount > 0) {
                notifyItemRemoved(mViewCount - 1);
            }
        }
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void handMsg(Message message) {
        switch (message.what) {
            case MSG_SHOW_LOAD_MORE_ERROR:
                // true 阻止 load more 执行
                mIsLoadMoring = true;
                setViewVisible(mLoadMoreLayout, true);
                setViewVisible(mLoadMoreView, false);
                setViewVisible(mLoadMoreError, true);
                setViewVisible(mNoMoreView, false);
                break;
            case MSG_SHOW_LOAD_MORE:
                mIsLoadMoring = true;
                setViewVisible(mLoadMoreLayout, true);
                setViewVisible(mLoadMoreView, true);
                setViewVisible(mLoadMoreError, false);
                setViewVisible(mNoMoreView, false);
                break;
            case MSG_SHOW_NO_MORE:
                mIsLoadMoring = false;
                setViewVisible(mLoadMoreLayout, false);
                setViewVisible(mNoMoreView, true);
                break;
            default:
                break;
        }
    }
}
