package com.ychong.kankan.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Material implements Parcelable {
    public String mLogo;

    public String title;

    public String time;

    public String filePath;

    public boolean isChecked;

    public long fileSize;

    public long fileId;

    public long uploadedSize;

    public int fileType;

    public boolean uploaded;

    public int progress; //上传进度

    public String timeStamps; //时间戳

    public int flag;//上传标志 0-正常 1--网络错误 2--超时(除了0以为均为上传失败标识)

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mLogo);
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeString(this.filePath);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeLong(this.fileSize);
        dest.writeLong(this.fileId);
        dest.writeLong(this.uploadedSize);
        dest.writeInt(this.fileType);
        dest.writeByte(this.uploaded ? (byte) 1 : (byte) 0);
        dest.writeInt(this.progress);
        dest.writeString(this.timeStamps);
        dest.writeInt(this.flag);
    }

    public Material() {
    }

    protected Material(Parcel in) {
        this.mLogo = in.readString();
        this.title = in.readString();
        this.time = in.readString();
        this.filePath = in.readString();
        this.isChecked = in.readByte() != 0;
        this.fileSize = in.readLong();
        this.fileId = in.readLong();
        this.uploadedSize = in.readLong();
        this.fileType = in.readInt();
        this.uploaded = in.readByte() != 0;
        this.progress = in.readInt();
        this.timeStamps = in.readString();
        this.flag = in.readInt();
    }

    public static final Parcelable.Creator<Material> CREATOR = new Parcelable.Creator<Material>() {
        @Override
        public Material createFromParcel(Parcel source) {
            return new Material(source);
        }

        @Override
        public Material[] newArray(int size) {
            return new Material[size];
        }
    };
}
