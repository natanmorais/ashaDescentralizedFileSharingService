package test.java;

import java.rmi.RemoteException;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalNode;

public class NodeTest
{
    public ILocalNode mHub;

    public NodeTest()
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        mHub = new SuperNodeHub("N1", "192.168.0.5");
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        new NodeTest();
    }
}
