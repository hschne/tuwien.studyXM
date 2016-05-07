package at.ac.tuwien.sepm.ss16.qse18.dao;

/**
 * This is a simple exception class that helps to differentiate the exceptions that are thrown in
 * every layer of this project.
 *
 * @author Cem Bicer
 */
public class DaoException extends Exception {
    public DaoException (String msg) {
        super(msg);
    }
}
