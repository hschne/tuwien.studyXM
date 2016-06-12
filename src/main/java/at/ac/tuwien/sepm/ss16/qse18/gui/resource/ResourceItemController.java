package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ResourceItemController
    extends BaseController {

    @FXML public Node root;
    @FXML public Label resourceName;
    @FXML public ImageView resourceIcon;
    @Autowired ResourceService resourceService;
    private ObservableResource resource;

    public Node getRoot() {
        return root;
    }

    public void setResource(ObservableResource resource) {
        this.resource = resource;
        resourceName.setText(resource.getName());
        loadResourceIcon();
    }

    @FXML public void handleOpen() {
        logger.debug("Opening resource");
        try {
            resourceService.openResource(resource.getResource());
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }

    }

    @FXML public void removeResource() {
        logger.debug("Removing resource");
    }

    private void loadResourceIcon() {
        ResourceType type = resource.getResourceType();
        switch (type) {
            case PDF:
                loadImage("/icons/pdf.png");
                break;
            default:
                return;
        }
    }

    private void loadImage(String name) {
        InputStream stream = ResourceItemController.class.getResourceAsStream(name);
        Image image = new Image(stream);
        resourceIcon.setImage(image);
    }
}
