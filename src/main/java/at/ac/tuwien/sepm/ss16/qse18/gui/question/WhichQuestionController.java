package at.ac.tuwien.sepm.ss16.qse18.gui.question;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sun.applet.Main;

import java.io.IOException;

/**
 * A controller which allows the user to choose the type of question he want's to create.
 *
 * @author Julian on 15.05.2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class WhichQuestionController implements
    GuiController {

    @FXML
    public Button buttonCreateQuestion;
    @FXML
    public RadioButton radioButtonMultipleChoice;
    @FXML
    public RadioButton radioButtonSingleChoice;
    @FXML
    public RadioButton radioButtonOpenQuestion;
    @FXML
    public RadioButton radioButtonNotecard;
    @Autowired
    MainFrameController mainFrameController;

    private ToggleGroup tg;

    private Logger logger = LoggerFactory.getLogger(CreateImageQuestionController.class);
    private SpringFXMLLoader springFXMLLoader;
    private AlertBuilder alertBuilder;
    private Stage primaryStage;


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
        radioButtonSingleChoice.setDisable(true); // disabled
        radioButtonOpenQuestion.setToggleGroup(tg);
        radioButtonOpenQuestion.setDisable(true); // disabled
        radioButtonNotecard.setToggleGroup(tg);
        radioButtonMultipleChoice.setSelected(true);

    }

    @FXML public void handleCreateQuestion() throws IOException {
        logger.debug("Opening creation screen");

        if(radioButtonMultipleChoice.isSelected())
        {
            mainFrameController.handleMultipleChoiceQuestion();
        }
        else if (radioButtonSingleChoice.isSelected())
        {
            //TODO: mainFrameController.handleSingleChoiceQuestion();
        }
        else if(radioButtonOpenQuestion.isSelected())
        {
            //TODO: mainFrameController.handleOpenQuestion();
        }
        else
        {
            mainFrameController.handleCreateImageQuestion();
        }

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
