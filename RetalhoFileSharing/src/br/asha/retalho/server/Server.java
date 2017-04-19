/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.asha.retalho.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import br.asha.retalho.utils.Utils;

/**
 * @author fir3destr0yer
 */
public class Server
{
    public static void main(String[] args)
    {
        try
        {
            String ip = Utils.getGlobalIp();
            ServerSocket ss = new ServerSocket(15123, 50, Inet4Address.getByName(ip));
            Socket s = ss.accept();
            System.out.println("accept" + s);
            OutputStream os = s.getOutputStream();
            File file = new File("/home/tiago/Downloads/COM222_aula11.pptx");
            InputStream is = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.read(data);
            System.out.println("enviando");
            os.write(data);
            os.flush();
            s.close();
            System.out.println("complete");
        }
        catch(Exception e)
        {

        }
    }
}