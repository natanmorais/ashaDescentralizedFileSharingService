package br.asha.retalho.dfss;

import java.io.IOException;

import br.asha.retalho.dfss.provider.SuperNodesProvider;

public class MasterDfssServer extends DfssServer
{
    public MasterDfssServer(String name, String ip)
            throws IOException, InstantiationException, IllegalAccessException
    {
        super(name, ip);

        SuperNodesProvider.SuperNode sn = new SuperNodesProvider.SuperNode();
        sn.ip = ip;
        sn.subnetName = name;
        mSuperNodeList.add(sn);
    }
}
