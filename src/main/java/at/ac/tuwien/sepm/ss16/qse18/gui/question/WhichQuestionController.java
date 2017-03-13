package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.QuestionNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

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
    extends BaseController {

    @FXML public Button buttonCreateQuestion;
    @FXML public RadioButton radioButtonMultipleChoice;
    @FXML public RadioButton radioButtonSingleChoice;
    @FXML public RadioButton radioButtonOpenQuestion;
    @FXML public RadioButton radioButtonNotecard;
    @FXML public RadioButton radioButtonSelfEval;

    private ObservableTopic topic;

    @Autowired QuestionNavigation questionNavigation;

    @FXML public void initialize() {
        logger.debug("Initializing whichQuestion screen");
        ToggleGroup tg = new ToggleGroup();
        radioButtonMultipleChoice.setToggleGroup(tg);
        radioButtonSingleChoice.setToggleGroup(tg);
        radioButtonOpenQuestion.setToggleGroup(tg);
        radioButtonNotecard.setToggleGroup(tg);
        radioButtonSelfEval.setToggleGroup(tg);
        radioButtonMultipleChoice.setSelected(true);
        questionNavigation.refreshMainPane();

    }

    @FXML public void handleCreateQuestion() throws IOException {
        logger.debug("Opening creation screen");

        if(radioButtonMultipleChoice.isSelected()) {
            questionNavigation.handleMultipleChoiceQuestion(this.topic);
        } else if(radioButtonSingleChoice.isSelected()) {
            questionNavigation.handleSingleChoiceQuestion(this.topic);
        } else if(radioButtonOpenQuestion.isSelected()) {
            questionNavigation.handleOpenQuestion(this.topic);
        } else if(radioButtonNotecard.isSelected()) {
            questionNavigation.handleImageQuestion(topic);
        }
        else {
            questionNavigation.handleSelfEvalQuestion(topic, null);
        }

    }

    public void setTopic(ObservableTopic topic) {
        this.topic = topic;
    }


}
