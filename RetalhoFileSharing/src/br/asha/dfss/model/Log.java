package br.asha.dfss.model;

import java.io.Serializable;

public class Log implements Serializable {

    private String ip;
    private long date;
    private String filename;

    public Log(String ip, long date, String filename) {
        this.ip = ip;
        this.date = date;
        this.filename = filename;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
