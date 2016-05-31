package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.ArrayList;
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

    protected ObservableResource resource;

    @FXML protected TextField textFieldAnswerOne;
    @FXML protected TextField textFieldAnswerTwo;
    @FXML protected TextField textFieldAnswerThree;
    @FXML protected TextField textFieldAnswerFour;
    @FXML protected CheckBox checkBoxContinue;
    @FXML protected Label resourceLabel;

    @Autowired protected MainFrameController mainFrameController;

    @Autowired public QuestionController(QuestionService questionService, AlertBuilder alertBuilder,
        SpringFXMLLoader fxmlLoader) {
        this.questionService = questionService;
        this.alertBuilder = alertBuilder;
        this.fxmlLoader = fxmlLoader;
    }

    protected abstract void fillFieldsAndCheckboxes();

    protected abstract void saveQuestionInput(List inputs);

    protected void fillAnswerFields(int startWith) {
        int counter = startWith;
        this.textFieldAnswerOne.setText(inputs == null ? "" : (String) inputs.get(counter));
        this.textFieldAnswerTwo.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerThree.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerFour.setText(inputs == null ? "" : (String) inputs.get(++counter));
    }

    protected List getUserInput() {
        List inputs = new ArrayList<>();

        saveAnswerFields(inputs);
        saveCheckboxesAndRadiobuttons(inputs);

        inputs.add(resource);

        return inputs;
    }

    protected void saveAnswerFields(List inputs) {
        saveQuestionInput(inputs);

        if (textFieldAnswerOne.getText() != null) {
            inputs.add(textFieldAnswerOne.getText());
        } else {
            inputs.add(null);
        }

        if (textFieldAnswerTwo.getText() != null) {
            inputs.add(textFieldAnswerTwo.getText());
        } else {
            inputs.add(null);
        }

        if (textFieldAnswerThree.getText() != null) {
            inputs.add(textFieldAnswerThree.getText());
        } else {
            inputs.add(null);
        }

        if (textFieldAnswerFour.getText() != null) {
            inputs.add(textFieldAnswerFour.getText());
        } else {
            inputs.add(null);
        }
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
            childController.saveUserInput(inputs, getQuestionType());
            childController.setResourceLabel(resourceLabel);

            stage.setTitle("Add resource");
            stage.setScene(new Scene((Parent) resourceChooserWrapper.getLoadedObject(), 500, 400));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        }

        // Save the chosen or newly created resource
        resource = (ObservableResource) inputs.get(inputs.size()-1);
    }

    protected abstract void saveCheckboxesAndRadiobuttons(List inputs);

    protected abstract QuestionType getQuestionType();

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

    protected void showAlert(Exception e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).title("Error")
            .headerText("An error occurred").contentText(e.getMessage()).build();
        alert.showAndWait();
    }


    protected void showSuccess(String msg) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).title("Success")
            .headerText("The operation was successful!").contentText(msg).build();
        alert.showAndWait();
    }
}
