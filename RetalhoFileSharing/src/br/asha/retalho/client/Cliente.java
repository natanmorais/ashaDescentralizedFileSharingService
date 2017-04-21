package br.asha.retalho.client;

import br.asha.retalho.dfss.DfssClient;

public class Cliente
{
    public static void main(String[] args)
    {
        DfssClient cliente = new DfssClient();
        cliente.requestFile("200.235.88.221", "Aula11.rar");
    }
}
