package br.asha.retalho.dfss;

import java.io.FileOutputStream;
import java.io.OutputStream;

import br.asha.retalho.dfss.helpers.FileHelper;

public class DfssClient
{
    public void requestFile(String ip, String name)
    {
        try
        {
            byte[] data = FileHelper.requestFileFrom(ip, name);
            if(data != null && data.length > 0)
            {
                try(OutputStream os = new FileOutputStream(name))
                {
                    os.write(data);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
