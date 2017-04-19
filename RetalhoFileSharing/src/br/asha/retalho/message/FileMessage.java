package br.asha.retalho.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileMessage
{
    public byte[] data;

    public FileMessage(byte[] data)
    {
        this.data = data;
    }

    public FileMessage(File file)
    {
        try
        {
            InputStream is = new FileInputStream(file);
            data = new byte[(int)file.length()];
            is.read(data);
            is.close();
        }
        catch(Exception e)
        {
        }
    }

    public void save(String filename)
    {
        try
        {
            OutputStream os = new FileOutputStream(filename);
            os.write(data);
            os.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
