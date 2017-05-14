package br.asha.dfss.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SimpleRepository<T extends Serializable>
{
    private String mFilename;
    private T mValue;

    public SimpleRepository(String filename)
    {
        mFilename = filename;
    }

    public void save()
    {
        if(mValue == null) return;

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(mFilename)))
        {
            oos.writeObject(mValue);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load()
    {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(mFilename)))
        {
            mValue = (T)ois.readObject();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public T get()
    {
        return mValue;
    }

    public void set(T value)
    {
        mValue = value;
    }
}
