package test.java;

import java.rmi.RemoteException;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalSuperNode;

public class SuperNodeTest
{
    public ILocalSuperNode mHub;

    public SuperNodeTest()
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        mHub = new SuperNodeHub("192.168.0.4");
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        new SuperNodeTest();
    }
}
