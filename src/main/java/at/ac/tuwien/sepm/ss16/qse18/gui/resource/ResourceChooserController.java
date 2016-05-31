package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bicer Cem
 */
@Component public class ResourceChooserController {
    private static final Logger logger = LogManager.getLogger();

    private ResourceService resourceService;
    private AlertBuilder alertBuilder;
    private MainFrameController mainFrameController;
    private Stage stage;
    private SpringFXMLLoader fxmlLoader;
    private Label resourceLabel;

    @FXML public Button newButton;
    @FXML public Button cancelButton;
    @FXML public Button chooseButton;
    @FXML public ListView<ObservableResource> resourceListView;

    private ObservableList<ObservableResource> resourceList;
    private List inputs;
    private QuestionType questionTypeOfResource;

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

    public void saveUserInput(List inputs, QuestionType questionType) {
        this.inputs = inputs;
        this.questionTypeOfResource = questionType;
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
        mainFrameController.handleCreateResource(inputs, questionTypeOfResource);
    }

    @FXML public void chooseResource() {
        logger.debug("Choose clicked");

        if (resourceListView.getSelectionModel().getSelectedItem() == null) {
            showAlert("No resource chosen",
                "You have to select the resource you want to refer to from the list.");
            return;
        }

        // Replace resource with chosen one
        inputs.remove(inputs.size()-1);
        inputs.add(resourceListView.getSelectionModel().getSelectedItem());

        resourceLabel.setText(resourceListView.getSelectionModel().getSelectedItem().getName());
        cancel();
    }

    @FXML public void cancel() {
        logger.debug("Closing ResourceChooser window");
        stage.close();
    }

    public void setResourceLabel(Label resourceLabel) {
        this.resourceLabel = resourceLabel;
    }

    private void showAlert(String headerMsg, String contentMsg) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).headerText(headerMsg)
            .contentText(contentMsg).build();
        alert.showAndWait();
    }
}
