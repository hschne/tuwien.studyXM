package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component public class ResourceChooserController {
    private static final Logger logger = LogManager.getLogger();

    private ResourceService resourceService;
    private AlertBuilder alertBuilder;
    private MainFrameController mainFrameController;
    private Stage stage;
    private SpringFXMLLoader fxmlLoader;

    @FXML public Button newButton;
    @FXML public Button cancelButton;
    @FXML public Button chooseButton;
    @FXML public ListView<ObservableResource> resourceListView;

    private ObservableList<ObservableResource> resourceList;

    @Autowired
    public ResourceChooserController(ResourceService resourceService, AlertBuilder alertBuilder,
        MainFrameController mainFrameController, SpringFXMLLoader fxmlLoader) {
        this.resourceService = resourceService;
        this.alertBuilder = alertBuilder;
        this.mainFrameController = mainFrameController;
        this.fxmlLoader = fxmlLoader;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML public void initialize() {
        try {
            List<ObservableResource> observableResources =
                resourceService.getResources().stream().map(ObservableResource::new)
                    .collect(Collectors.toList());
            resourceList = FXCollections.observableList(observableResources);
            resourceListView.setItems(resourceList);
            resourceListView.setCellFactory(lv -> new ListCell<ObservableResource>() {
                @Override public void updateItem(ObservableResource item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        String text =
                            item.getResource().getResourceId() + ": " + item.getName() + "(" + item
                                .getReference() + ")";
                        setText(text);
                    }
                }
            });
        } catch (ServiceException e) {
            logger.error("Could not fill resource list", e);
            showAlert("Could not fill resource list",
                "Make sure that there is a connection to the database.");
        }
    }

    @FXML public void newResource() {
        logger.debug("Opening create resource window");
        stage.close();
        mainFrameController.handleCreateResource();
    }

    @FXML public void chooseResource() {
        // TODO: implement choose resource
    }

    @FXML public void cancel() {
        logger.debug("Closing ResourceChooser window");
        stage.close();
    }

    private void showAlert(String headerMsg, String contentMsg) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).headerText(headerMsg)
            .contentText(contentMsg).build();
        alert.showAndWait();
    }
}
