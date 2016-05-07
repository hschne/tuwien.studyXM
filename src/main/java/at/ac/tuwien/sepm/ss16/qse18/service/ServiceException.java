package at.ac.tuwien.sepm.ss16.qse18.service;

/**
 * An Exception class thrown by service exceptions
 *
 * @author Haixiang Zhang
 */
public class ServiceException extends Exception{
    public ServiceException(String msg){
        super(msg);
    }
}
