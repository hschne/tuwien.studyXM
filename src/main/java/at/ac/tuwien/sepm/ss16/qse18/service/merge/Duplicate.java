package at.ac.tuwien.sepm.ss16.qse18.service.merge;

/**
 * @author Hans-Joerg Schroedl
 */
public class Duplicate<T>{

    private final T existing;

    public T getImported() {
        return imported;
    }

    private final T imported;

    public Duplicate(T existing, T imported){
        this.existing = existing;
        this.imported = imported;
    }

    public T getExisting() {
        return existing;
    }
}
