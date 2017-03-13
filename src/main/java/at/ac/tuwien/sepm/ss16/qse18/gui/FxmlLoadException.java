package at.ac.tuwien.sepm.ss16.qse18.gui;

/**
 * This is a custom runtime exception to be thrown if an FXML cannot be loaded in a list view during
 * runtime. Replaces the generic runtime exception in this case.
 *
 * @author Hans-Joerg Schroedl
 */
public class FxmlLoadException extends RuntimeException {

    public FxmlLoadException(String message, Exception e){
        super(message,e);
    }
}
