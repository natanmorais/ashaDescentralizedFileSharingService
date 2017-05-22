package br.asha.dfss.repository;

import br.asha.dfss.model.SharedFile;
import br.asha.dfss.utils.Utils;

/**
 * Lista de arquivos que estao sendo compartilhados pelos nos. Isto fica em cada super-nó.
 */
public class SharedFileList extends Repository<SharedFile> {

    public SharedFileList(String filename) {
        super(filename);
    }

    public synchronized static SharedFileList getInstance(String alias) {
        String key = alias + "-arquivosparacompartilhar.asha";
        if (!INSTANCIAS.containsKey(key)) {
            INSTANCIAS.put(key, new SharedFileList(key));
        }
        return (SharedFileList) INSTANCIAS.get(key);
    }

    @Override
    public boolean add(SharedFile item) {
        Utils.log("Novo arquivo: %s", item.nome);
        for (int i = 0; i < size(); i++) {
            SharedFile sf = get(i);
            if (sf.nome.equals(item.nome) &&
                    sf.ip.equals(item.ip) &&
                    !sf.sha.equals(item.sha)) {
                remove(sf);
                Utils.log("%s foi removido", sf.nome);
                break;
            }
        }

        return super.add(item);
    }

    public synchronized boolean add(String ip, String nome, String sha, long dataDaUltimaModificacao) {
        return add(new SharedFile(ip, nome, sha, dataDaUltimaModificacao));
    }

    public synchronized SharedFile getByName(String name) {
        for (SharedFile sf : this) {
            if (sf.nome.equals(name)) {
                return sf;
            }
        }

        return null;
    }

    public synchronized SharedFile getByIp(String ip) {
        for (SharedFile sf : this) {
            if (sf.ip.equals(ip)) {
                return sf;
            }
        }

        return null;
    }

    public synchronized SharedFile getBySHA(String sha) {
        for (SharedFile sf : this) {
            if (sf.sha.equals(sha)) {
                return sf;
            }
        }

        return null;
    }
}
