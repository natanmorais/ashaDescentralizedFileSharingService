package br.asha.dfss;

import br.asha.dfss.rmi.RmiServer;

public interface IHub {

    @LocalMethod
    RmiServer getServidor();

    @LocalMethod
    String getNome();

    @LocalMethod
    String getMeuIp();
}
