package com.ychong.kankan;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ImageBean {
    @Override
    public String toString() {
        return "ImageBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Id(autoincrement = true)
    private Long id;
    public String name;
    public String path;
    public String uploadTime;
    public String title;
    public int userId;
    @Generated(hash = 1966518988)
    public ImageBean(Long id, String name, String path, String uploadTime,
            String title, int userId) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.uploadTime = uploadTime;
        this.title = title;
        this.userId = userId;
    }
    @Generated(hash = 645668394)
    public ImageBean() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUploadTime() {
        return this.uploadTime;
    }
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
