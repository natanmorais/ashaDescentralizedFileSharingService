package br.asha.dfss.repository;

import br.asha.dfss.model.Log;

public class LogRepository extends ListRepository<Log> {

    private static LogRepository mInstance;

    public LogRepository() {
        super("log.asha");
    }

    public synchronized static LogRepository getInstance() {
        if (mInstance == null) mInstance = new LogRepository();
        return mInstance;
    }
}
