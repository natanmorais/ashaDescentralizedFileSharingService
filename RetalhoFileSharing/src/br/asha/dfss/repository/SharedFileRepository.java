package br.asha.dfss.repository;

import br.asha.dfss.model.SharedFile;

public class SharedFileRepository extends ListRepository<SharedFile>
{

    public SharedFileRepository(String filename)
    {
        super(filename);
    }

    @Override
    public boolean add(SharedFile item)
    {
        return super.add(item);
    }
}
