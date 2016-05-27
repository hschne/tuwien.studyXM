package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectItemController;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.ListCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Hans-Joerg Schroedl
 */
@Component  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResourceCell extends ListCell<ObservableResource> {


    @Autowired SpringFXMLLoader springFXMLLoader;

    private Logger logger = LogManager.getLogger();

    @Override public void updateItem(ObservableResource subject, boolean empty) {
        super.updateItem(subject, empty);
        if (subject != null) {
            ResourceItemController itemController = getController();
            setControllerProperties(subject, itemController);
        }

    }

    private void setControllerProperties(ObservableResource resource,
        ResourceItemController itemController) {
        setGraphic(itemController.getRoot());
    }

    private ResourceItemController getController() {
        return null;
    }

}
