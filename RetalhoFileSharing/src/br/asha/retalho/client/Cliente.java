package br.asha.retalho.client;

import br.asha.retalho.dfss.DfssClient;
import br.asha.retalho.dfss.provider.SharedFilesProvider;
import br.asha.retalho.dfss.provider.SuperNodesProvider;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Cliente
{
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException
    {
        DfssClient cliente = new DfssClient("Natan", "200.235.88.221");
        /*
        * CRIA SUB REDE
        * cliente.criarUmNovaSubRede("Sub-Rede do Natan 2");
        */
        /*
        * PRINTA TODAS SUB-REDES EXISTENTES
        * for(SuperNodesProvider.SuperNode sn: cliente.pegarListaSubRedesDisponiveis()){
        *     System.out.println("Rede: " + sn.subnetName + " tem o super-nó: " + sn.ip);
        * }
        */
        /*
        * ENTRA EM SUB-REDE EXISTENTE
        * cliente.entrarSubRede("200.235.88.221");
        */
        //cliente.novoArquivo("200.235.88.221", "1000001", "Um Arquivo muito legal", "Novo Arquivo");
        for(SharedFilesProvider.SharedFile sf: cliente.pegarListaArquivosDisponiveis("200.235.88.221")){
            cliente.requestFile(sf.ip, sf.name);
        }
    }
}
