package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.gui.FxmlLoadException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
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
 * This class represents a custom list item in the resource list view.
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) class ResourceCell
    extends ListCell<ObservableResource> {

    @Autowired SpringFXMLLoader springFXMLLoader;

    private static final Logger logger = LogManager.getLogger();

    @Override public void updateItem(ObservableResource resource, boolean empty) {
        super.updateItem(resource, empty);
        if (resource != null) {
            ResourceItemController itemController = getController();
            setControllerProperties(resource, itemController);
        }

    }

    private void setControllerProperties(ObservableResource resource,
        ResourceItemController itemController) {
        itemController.setResource(resource);
        setGraphic(itemController.getRoot());
    }

    private ResourceItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, ResourceItemController> editResourceController;
        try {
            editResourceController = springFXMLLoader
                .loadAndWrap("/fxml/resource/resourceItem.fxml", ResourceItemController.class);
            return editResourceController.getController();
        } catch (IOException e) {
            logger.error(e);
            throw new FxmlLoadException("Error loading subject item", e);
        }
    }

}
