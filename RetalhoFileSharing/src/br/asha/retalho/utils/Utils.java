package br.asha.retalho.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Utils
{
    public static String getGlobalIp()
    {
        try
        {
            URL ipify = new URL("https://api.ipify.org?format=json");
            InputStream is = ipify.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ip = br.readLine();
            ip = ip.substring(7, ip.length() - 2);
            System.out.println(ip);
            return ip;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
