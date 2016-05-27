package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ResourceItemController {

    private final Logger logger = LogManager.getLogger();
    @FXML public Node root;
    @FXML public Label resourceName;
    @FXML public ImageView resourceIcon;
    private ObservableResource resource;

    public Node getRoot() {
        return root;
    }

    public void setResource(ObservableResource resource) {
        this.resource = resource;
        resourceName.setText(resource.getReference());
        loadResourceIcon();
    }

    @FXML public void handleOpen() {
        logger.debug("Opening resource");
        //TODO: Implement opening!
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
