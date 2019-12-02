package com.ychong.picandvideo.entity;

public class LocalVideoBean {
    @Override
    public String toString() {
        return "LocalVideoBean{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", resolution='" + resolution + '\'' +
                ", size=" + size +
                ", date=" + date +
                ", duration=" + duration +
                '}';
    }

    public LocalVideoBean(){

    }
    public LocalVideoBean(int id, String path, String name, String resolution, long size, long date, long duration) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
    }

    public int id = 0;
    public String path = null;
    public String name = null;
    public String resolution = null;// 分辨率
    public long size = 0;
    public long date = 0;
    public long duration = 0;

}
