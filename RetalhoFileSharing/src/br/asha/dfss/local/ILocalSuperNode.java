package br.asha.dfss.local;

import br.asha.dfss.LocalMethod;

public interface ILocalSuperNode extends ILocalNode
{
    @LocalMethod
    boolean createNewSubNet(String masterIp, String subNetName);

    @LocalMethod
    String getSubNetName();
}
