package com.example.q.myapplication.tab2.model;

import java.io.Serializable;

public class Image implements Serializable {
    private String url;
    private String path;

    public Image() {
    }

    public Image(String path, String url) {
        this.path = path;
        this.url = url;
    }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

}
