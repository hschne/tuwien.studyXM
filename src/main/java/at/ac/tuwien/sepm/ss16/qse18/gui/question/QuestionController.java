package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceChooserController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.List;

/**
 * Abstract base class to be used by any controllers for question creation
 *
 * @author Hans-Joerg Schroedl
 */

@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public abstract class QuestionController
    implements GuiController {

    protected final static Logger logger = LogManager.getLogger();
    protected final QuestionService questionService;
    protected final AlertBuilder alertBuilder;
    protected SpringFXMLLoader fxmlLoader;

    protected ObservableTopic topic;
    protected List inputs;

    @Autowired protected MainFrameController mainFrameController;

    @Autowired public QuestionController(QuestionService questionService, AlertBuilder alertBuilder,
        SpringFXMLLoader fxmlLoader) {
        this.questionService = questionService;
        this.alertBuilder = alertBuilder;
        this.fxmlLoader = fxmlLoader;
    }

    protected abstract void fillFieldsAndCheckboxes();

    protected abstract List getUserInput();

    /**
     * Sets the topic in which the question should be created
     *
     * @param topic The topic
     */
    public void setTopic(ObservableTopic topic) {
        this.topic = topic;
    }

    public void setInput(List inputs) {
        this.inputs = inputs;
        fillFieldsAndCheckboxes();
    }

    @FXML public void handleAddResource() {
        // saving input from user to be able to recover later
        inputs = getUserInput();

        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, ResourceChooserController> resourceChooserWrapper =
            null;

        try {
            resourceChooserWrapper = fxmlLoader.loadAndWrap("/fxml/resource/resourceChooser.fxml",
                ResourceChooserController.class);
        } catch (IOException e) {
            logger.error("Could not load resourceChooser.fxml", e);
        }

        ResourceChooserController childController = null;

        if (resourceChooserWrapper != null) {
            childController = resourceChooserWrapper.getController();
            childController.setStage(stage);
            childController.saveUserInput(inputs, QuestionType.MULTIPLECHOICE);

            stage.setTitle("Add resource");
            stage.setScene(new Scene((Parent) resourceChooserWrapper.getLoadedObject(), 500, 400));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        }
    }

    protected void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occurred").contentText(e.getMessage()).build();
        alert.showAndWait();
    }


    protected void showSuccess(String msg) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).title("Success")
            .headerText("The operation was successful!").contentText(msg).build();
        alert.showAndWait();
    }
}
