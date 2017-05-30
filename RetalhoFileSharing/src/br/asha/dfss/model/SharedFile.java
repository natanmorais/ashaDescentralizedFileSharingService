package br.asha.dfss.model;

import br.asha.dfss.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedFile implements Serializable {
    @SerializedName("ip")
    public String ip;
    @SerializedName("nome")
    public String nome;
    @SerializedName("sha")
    public String sha;
    @SerializedName("updateAt")
    public long dataDaUltimaAtualicao;

    public SharedFile() {
    }

    public SharedFile(String ip, File file)
            throws Exception {
        this.ip = ip;
        this.dataDaUltimaAtualicao = file.lastModified();
        this.nome = file.getPath();
        this.sha = Utils.generateSHA1ForFile(file);
    }

    public SharedFile(String ip, String nome, String sha, long dataDaUltimaAtualicao) {
        this.ip = ip;
        this.nome = nome;
        this.sha = sha;
        this.dataDaUltimaAtualicao = dataDaUltimaAtualicao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SharedFile that = (SharedFile) o;

        if (!ip.equals(that.ip)) return false;
        //if (!nome.equals(that.nome)) return false;
        return sha.equals(that.sha);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        //result = 31 * result + nome.hashCode();
        result = 31 * result + sha.hashCode();
        return result;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm:SS");
        return String.format("%s [%s]",
                new File(nome).getName(),
                format.format(new Date(dataDaUltimaAtualicao)));
    }
}
