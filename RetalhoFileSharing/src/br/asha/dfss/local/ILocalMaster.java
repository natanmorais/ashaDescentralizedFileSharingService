package br.asha.dfss.local;

import br.asha.dfss.LocalMethod;

public interface ILocalMaster extends ILocalSuperNode
{
    @LocalMethod
    String getUID();
}
