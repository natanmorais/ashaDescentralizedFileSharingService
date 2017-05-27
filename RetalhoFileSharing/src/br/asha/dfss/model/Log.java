package br.asha.dfss.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Log implements Serializable {

    @SerializedName("ip")
    public String ip;
    @SerializedName("date")
    public long date;
    @SerializedName("filename")
    public String filename;

    public Log(String ip, long date, String filename) {
        this.ip = ip;
        this.date = date;
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Log log = (Log) o;

        if (date != log.date) return false;
        if (!ip.equals(log.ip)) return false;
        return filename.equals(log.filename);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + filename.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Log {" +
                "ip='" + ip + '\'' +
                ", date=" + date +
                ", filename='" + filename + '\'' +
                '}';
    }
}
