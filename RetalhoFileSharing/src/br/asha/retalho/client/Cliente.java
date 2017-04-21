package br.asha.retalho.client;

import br.asha.retalho.dfss.DfssClient;
import br.asha.retalho.dfss.helpers.FileHelper;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente
{
    public static void main(String[] args)
    {
        DfssClient cliente = new DfssClient();
        cliente.requestFile("200.235.88.221", "Aula11.rar");
    }
}
