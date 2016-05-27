package at.ac.tuwien.sepm.ss16.qse18.dao;

import java.sql.SQLException;

/**
 * This is a simple exception class, which is thrown in the dao layer,
 * that helps to differentiate the exceptions that are thrown in every layer of this project.
 *
 * @author Cem Bicer
 */
public class DaoException extends Exception {
    /**
     * constructor initializes DaoException with the given String
     * @param msg the message that should be displayed when the exception is thrown
     * */
    public DaoException(String msg) {
        super(msg);
    }

    /**
     * constructor initializes DaoException with the given Exception
     * @param e the exception that shall be mocked
     * */
    public DaoException(Exception e) {
        super(e);
    }

    //TODO: Sollte konstruktor mit nur exception ersetzen!
    public DaoException(String s, SQLException e) {
        super(s,e);
    }
}
