package br.asha.retalho.dfss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFile extends Remote
{
    /**
     * Manda ou recebe um arquivo para ser escrito.
     *
     * @param data dados do arquivo.
     * @param name nome do arquivo.
     * @return true se foi escrito; caso contário, false.
     */
    boolean writeFile(byte[] data, String name)
            throws RemoteException;

    /**
     * Requisita um arquivo.
     *
     * @param name nome do arquivo.
     * @return conteúdo do arquivo.
     */
    byte[] requestFile(String name)
            throws RemoteException;
}
