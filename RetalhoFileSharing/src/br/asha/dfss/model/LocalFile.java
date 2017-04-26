package br.asha.dfss.model;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

public class LocalFile implements Serializable
{
    private String mBasePath;
    private String mName;
    private String mSha;

    public LocalFile(String basePath, String name, String sha)
    {
        mBasePath = basePath;
        mName = name;
        mSha = sha;
    }

    public String getBasePath()
    {
        return mBasePath;
    }

    public String getName()
    {
        return mName;
    }

    public String getSha()
    {
        return mSha;
    }

    public File getFile()
    {
        return new File(getBasePath(), getName());
    }

    public boolean exists()
    {
        return getFile().exists();
    }

    public byte[] getData()
    {
        File file = getFile();

        if(file.exists())
        {
            try(FileInputStream fis = new FileInputStream(file))
            {
                return IOUtils.readFully(fis, (int)file.length());
            }
            catch(Exception e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public int hashCode()
    {
        return mName.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof LocalFile &&
                ((LocalFile)o).getName().equals(getName());
    }
}
