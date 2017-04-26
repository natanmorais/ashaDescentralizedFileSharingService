package br.asha.dfss.repository;

import br.asha.dfss.model.LocalFile;

public class LocalFileRepository extends ListRepository<LocalFile>
{

    public LocalFileRepository(String filename)
    {
        super(filename);
    }

    public boolean add(String basePath, String name, String sha)
    {
        return add(new LocalFile(basePath, name, sha));
    }

    public LocalFile getByName(String name)
    {
        for(LocalFile file : this)
        {
            if(file.getName().equalsIgnoreCase(name))
            {
                return file;
            }
        }

        return null;
    }
}
