package br.asha.dfss.repository;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Repositório.
 */
public abstract class Repository<T extends Serializable> extends ArrayList<T> implements Iterable<T> {

    private transient static final Gson GSON = new Gson();
    protected static final HashMap<String, Repository<?>> INSTANCIAS = new HashMap<>();
    private transient String filename;

    public Repository(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void carregar() {
        //Caminho para o arquivo.
        File file = new File(getFilename());
        //Se não existir, cria um.
        if (!file.exists()) {
            save();
        }
        //Lê o arquivo.
        try (FileInputStream fis = new FileInputStream(file)) {
            //Pega o conteúdo em String.
            String json = IOUtils.toString(fis, "UTF-8");
            //Adiciona todos os itens.
            addAll(GSON.fromJson(json, getClass().getGenericSuperclass()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean save() {
        try {
            //Converte objeto em json.
            String json = GSON.toJson(this);
            //Salva em um arquivo.
            try (OutputStream os = new FileOutputStream(getFilename())) {
                IOUtils.write(json, os, "UTF-8");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean add(T item) {
        try {
            return !contains(item) && super.add(item);
        } finally {
            save();
        }
    }

    public boolean remove(T item) {
        try {
            return super.remove(item);
        } finally {
            save();
        }
    }

    public void replace(Repository<T> repository) {
        try {
            clear();
            addAll(repository);
        } finally {
            save();
        }
    }
}
