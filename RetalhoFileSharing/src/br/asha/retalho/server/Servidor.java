package br.asha.retalho.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import br.asha.retalho.message.FileRequestMessage;
import br.asha.retalho.utils.Utils;

public class Servidor
{
    private static final int PORT = 15123;
    private static final int MAX_SIZE = 1024 * 1024;
    private static final byte[] MSG = new byte[MAX_SIZE];
    Gson gson = new Gson();
    private ServerSocket ss;

    public Servidor()
            throws IOException
    {
        String ip = Utils.getGlobalIp();
        ss = new ServerSocket(15123, 50, Inet4Address.getByName(ip));
    }

    public void start()
    {
        while(true)
        {
            try
            {
                //Aceitou a conexao
                Socket s = ss.accept();
                System.out.println("accept" + s);
                InputStream is = s.getInputStream();
                int length = is.read(MSG);
                if(length > 0)
                {
                    int msgType = MSG[0];
                    if(msgType == 1)
                    {
                        FileRequestMessage msg =
                                gson.fromJson(new String(MSG, 1, length), FileRequestMessage.class);
                        System.out.println(msg.filename);
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
