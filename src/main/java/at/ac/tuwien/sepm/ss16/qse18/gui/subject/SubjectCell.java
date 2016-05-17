package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import javafx.scene.control.ListCell;

/**
 * @author Hans-Joerg Schroedl
 */
public class SubjectCell extends ListCell<ObservableSubject> {

    @Override
    public void updateItem(ObservableSubject subject, boolean empty){
        super.updateItem(subject, empty);

        if(subject != null){
            SubjectItemController itemController = new SubjectItemController(subject);
            itemController.getRoot();
            setGraphic(itemController.getRoot());
        }

    }
}
