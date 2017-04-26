package br.asha.dfss.model;

import java.io.Serializable;

public class SharedFile implements Serializable
{
    private String mIp;
    private String mName;
    private String mSha;
    private long mLastModifiedDate;

    public SharedFile(String ip, String name, String sha, long lastModifiedDate)
    {
        mIp = ip;
        mName = name;
        mSha = sha;
        mLastModifiedDate = lastModifiedDate;
    }

    public String getIp()
    {
        return mIp;
    }

    public String getName()
    {
        return mName;
    }

    public String getSha()
    {
        return mSha;
    }

    public long getLastModifiedDate()
    {
        return mLastModifiedDate;
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof SharedFile &&
                getName().equalsIgnoreCase(((SharedFile)o).getName()) &&
                getSha().equals(((SharedFile)o).getSha());
    }

    @Override
    public int hashCode()
    {
        //Arquivos em diferentes máquinas são ditos IGUAIS se tiverem o mesmo nome e o mesmo conteúdo(sha).
        int result = getName().toLowerCase().hashCode();
        result = 31 * result + getSha().hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "SharedFile {" +
                "ip='" + mIp + '\'' +
                ", name='" + mName + '\'' +
                ", sha='" + mSha + '\'' +
                ", lastModifiedDate=" + mLastModifiedDate +
                '}';
    }
}
