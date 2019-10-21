package com.ychong.kankan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

public class ImageBean {
    @Override
    public String toString() {
        return "ImageBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
    private Long id;
    public String name;
    public String path;
    public String uploadTime;
    public String title;
    public int userId;

    public int zanNum;//赞数
    public int caiNum;//踩数
    public int commentNum;//评论数
}
