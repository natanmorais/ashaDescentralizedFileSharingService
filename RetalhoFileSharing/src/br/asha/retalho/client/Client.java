/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.asha.retalho.client;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import br.asha.retalho.message.FileRequestMessage;
import br.asha.retalho.utils.Utils;

/**
 * @author fir3destr0yer
 */
public class Client
{
    public static void main(String[] args)
            throws IOException
    {
        int filesize = 1022386;
        int bytesRead;
        int currentTot = 0;
        String ip = Utils.getGlobalIp();
        Socket socket = new Socket(ip, 15123);
        byte[] bytearray = new byte[filesize];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream("copy.pptx");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(bytearray, 0, bytearray.length);
        currentTot = bytesRead;

        do
        {
            bytesRead =
                    is.read(bytearray, currentTot, (bytearray.length - currentTot));
            if(bytesRead >= 0) currentTot += bytesRead;
        } while(bytesRead > -1);

        bos.write(bytearray, 0, currentTot);
        bos.flush();
        bos.close();
        socket.close();
    }

    public static void sendFileRequest(String filename)
    {
        FileRequestMessage msg = new FileRequestMessage();
        msg.filename = filename;
        Gson gson = new Gson();
        String json = gson.toJson(msg);
    }
}
