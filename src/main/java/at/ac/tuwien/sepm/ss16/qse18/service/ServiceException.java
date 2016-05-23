package at.ac.tuwien.sepm.ss16.qse18.service;

/**
 * This is a simple exception class, which is thrown in the service layer,
 * that helps to differentiate the exceptions that are thrown in every layer of this project.
 *
 * @author Haixiang Zhang
 */
public class ServiceException extends Exception{
    /**
     * constructor initializes DaoException with the given String
     * @param msg the message that should be displayed when the exception is thrown
     * */
    public ServiceException(String msg){
        super(msg);
    }

    /**
     * constructor initializes DaoException with the given Exception
     * @param e the exception that shall be mocked
     * */
    public ServiceException(Exception e){
        super(e);
    }
}
