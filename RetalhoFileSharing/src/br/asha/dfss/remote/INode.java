package br.asha.dfss.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.asha.dfss.RemoteMethod;

public interface INode extends Remote
{
    @RemoteMethod
    String requestName()
            throws RemoteException;

    @RemoteMethod
    byte[] requestDataFile(String name)
        throws RemoteException;
}
