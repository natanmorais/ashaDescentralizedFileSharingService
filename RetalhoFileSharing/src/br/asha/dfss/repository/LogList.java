package br.asha.dfss.repository;

import br.asha.dfss.model.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista de sub-redes existentes.
 */
public class LogList extends Repository<Log> {

    public LogList(String filename) {
        super(filename);
    }

    public synchronized static LogList getInstance(String alias) {
        String key = alias + "-logdetransferencia.asha";
        if (!INSTANCIAS.containsKey(key)) {
            INSTANCIAS.put(key, new LogList(key));
        }
        return (LogList) INSTANCIAS.get(key);
    }

    public synchronized boolean add(String ip, long data, String nomeArquivo) {
        return super.add(new Log(ip, data, nomeArquivo));
    }

    public synchronized List<Log> getByIp(String ip) {
        List<Log> logs = new ArrayList<>();

        for (Log log : this) {
            if (log.ip.equals(ip)) {
                logs.add(log);
            }
        }

        return logs;
    }
}
