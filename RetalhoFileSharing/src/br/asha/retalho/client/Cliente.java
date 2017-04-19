package br.asha.retalho.client;

import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.Socket;

import br.asha.retalho.message.FileRequestMessage;

public class Cliente
{
    Gson gson = new Gson();

    public static void main(String[] args)
    {
        new Cliente().sendFileRequest("");
    }

    public void sendFileRequest(String filename)
    {
        FileRequestMessage msg = new FileRequestMessage();
        msg.filename = filename;
        String json = gson.toJson(msg);

        try
        {
            Socket socket = new Socket("", 15123);
            OutputStream os = socket.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
