package br.asha.dfss.repository;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ListRepository<T extends Serializable> implements Iterable<T> {
    private List<T> mList = new ArrayList<>(0);
    private String mFilename;

    public ListRepository(String filename) {
        mFilename = filename;
        mList = new ArrayList<>();
        load();
    }

    public String getFilename() {
        return mFilename;
    }

    public void load() {
        File file = new File(getFilename());

        if (!file.exists()) {
            save();
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            String json = IOUtils.toString(fis, "UTF-8");
            Gson gson = new Gson();
            mList = (List<T>) gson.fromJson(json, mList.getClass());
            System.out.println(mList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean save() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(mList);
            IOUtils.write(json, new FileOutputStream(getFilename()), "UTF-8");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contains(T item) {
        return mList.contains(item);
    }

    public boolean add(T item) {
        return /*!contains(item) && */mList.add(item);
    }

    public boolean remove(T item) {
        return mList.remove(item);
    }

    public int size() {
        return mList.size();
    }

    public List<T> toList() {
        return mList;
    }

    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

    public void clear() {
        mList.clear();
    }

    public void replace(List<T> newList) {
        clear();
        mList.addAll(newList);
    }

    public void replace(ListRepository<T> repo) {
        replace(repo.toList());
    }
}
