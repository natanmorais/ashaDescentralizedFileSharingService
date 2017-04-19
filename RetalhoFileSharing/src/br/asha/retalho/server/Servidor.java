package br.asha.retalho.server;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import br.asha.retalho.message.FileMessage;
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
        System.out.println(ip);
        ss = new ServerSocket(15123, 50, Inet4Address.getByName(ip));
    }

    public static void main(String[] args)
            throws IOException
    {
        new Servidor().start();
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
                        System.out.println(MSG[0]);
                        System.out.println(new String(MSG, 2, length - 2, "utf-8"));
                        FileRequestMessage msg =
                                gson.fromJson(new String(MSG, 2, length - 2, "utf-8"), FileRequestMessage.class);
                        System.out.println(msg.filename);

                        OutputStream os = s.getOutputStream();
                        os.write(gson.toJson(new FileMessage(new File(msg.filename))).getBytes());
                        os.flush();
                    }
                }

                s.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
