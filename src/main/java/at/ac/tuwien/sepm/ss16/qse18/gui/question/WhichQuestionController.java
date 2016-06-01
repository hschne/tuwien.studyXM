package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A controller which allows the user to choose the type of question he want's to create.
 *
 * @author Julian on 15.05.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class WhichQuestionController
    implements GuiController {

    @FXML public Button buttonCreateQuestion;
    @FXML public RadioButton radioButtonMultipleChoice;
    @FXML public RadioButton radioButtonSingleChoice;
    @FXML public RadioButton radioButtonOpenQuestion;
    @FXML public RadioButton radioButtonNotecard;
    @Autowired MainFrameController mainFrameController;

    private ToggleGroup tg;

    private static Logger logger = LogManager.getLogger();
    private SpringFXMLLoader springFXMLLoader;
    private AlertBuilder alertBuilder;
    private Stage primaryStage;
    private ObservableTopic topic;


    @Autowired
    public WhichQuestionController(SpringFXMLLoader springFXMLLoader, AlertBuilder alertBuilder) {
        this.alertBuilder = alertBuilder;
        this.springFXMLLoader = springFXMLLoader;
    }

    @FXML public void initialize() {
        logger.debug("Initializing whichQuestion screen");
        tg = new ToggleGroup();
        radioButtonMultipleChoice.setToggleGroup(tg);
        radioButtonSingleChoice.setToggleGroup(tg);
        radioButtonOpenQuestion.setToggleGroup(tg);
        radioButtonNotecard.setToggleGroup(tg);
        radioButtonMultipleChoice.setSelected(true);

    }

    @FXML public void handleCreateQuestion() throws IOException {
        logger.debug("Opening creation screen");

        if(radioButtonMultipleChoice.isSelected()) {
            mainFrameController.handleMultipleChoiceQuestion(this.topic);
        } else if(radioButtonSingleChoice.isSelected()) {
            mainFrameController.handleSingleChoiceQuestion(this.topic);
        } else if(radioButtonOpenQuestion.isSelected()) {
           mainFrameController.handleOpenQuestion(this.topic);
        } else {
            mainFrameController.handleCreateImageQuestion(topic);
        }

    }

    public void setTopic(ObservableTopic topic) {
        this.topic = topic;
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder
                .alertType(Alert.AlertType.ERROR)
                .title("Error")
                .headerText("An error occured")
                .contentText(e.getMessage())
                .build();
        alert.showAndWait();
    }

}
