package br.asha.retalho.dfss;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.asha.retalho.dfss.provider.SharedFilesProvider;

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

    /**
     * Uma máquina adicionou um novo arquivo para o compartilhamente.
     *
     * @param id   O ID único do arquivo.
     * @param ip   O IP máquina que possui o arquivo.
     * @param desc Descrição do arquivo.
     * @param name O nome do arquivo.
     */
    int updateSharedFileList(String id, String ip, String desc, String name, boolean firstReceptor)
            throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    SharedFilesProvider.SharedFileList getSharedFileList()
            throws RemoteException;
}
