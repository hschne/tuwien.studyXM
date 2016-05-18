package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import at.ac.tuwien.sepm.util.AlertBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Julian on 15.05.2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class CreateMultipleChoiceQuestionController
    implements GuiController {

    private Logger logger = LogManager.getLogger(CreateMultipleChoiceQuestionController.class);
    private Stage primaryStage;
    private AlertBuilder alertBuilder;
    private QuestionService questionService;
    private SpringFXMLLoader springFXMLLoader;

    @FXML private TextArea textAreaQuestion;
    @FXML private TextField textfieldAnswerOne;
    @FXML private TextField textfieldAnswerTwo;
    @FXML private TextField textfieldAnswerThree;
    @FXML private TextField textfieldAnswerFour;
    @FXML private CheckBox checkBoxAnswerOne;
    @FXML private CheckBox checkBoxAnswerTwo;
    @FXML private CheckBox checkBoxAnswerThree;
    @FXML private CheckBox checkBoxAnswerFour;
    @FXML private Button buttonCreateQuestion;
    @Autowired MainFrameController mainFrameController;

    @Override
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Autowired
    public CreateMultipleChoiceQuestionController(SpringFXMLLoader springFXMLLoader,
        QuestionService questionService, AlertBuilder alertBuilder) {
        this.springFXMLLoader = springFXMLLoader;
        this.questionService = questionService;
        this.alertBuilder = alertBuilder;
    }

    @FXML public void createQuestion() {
        logger.info("Now creating new question");
        Question newQuestion = null;
        try {
            newQuestion = questionService.createQuestion(newQuestionFromField());
        } catch (ServiceException e) {
            showAlert(e);
            return;
        }
        try {
            questionService.setCorrespondingAnswers(newQuestion, newAnswersFromField());
        } catch(ServiceException e) {
            showAlert(e);
            return;
        }

        showSuccess("Inserted new question into database.");
        mainFrameController.handleHome();
    }

    private Question newQuestionFromField() throws ServiceException {
        logger.info("Collecting question from field.");
        if(textAreaQuestion.getText().isEmpty()) {
            throw new ServiceException("The question must not be empty.");
        }

        return new Question(textAreaQuestion.getText(), QuestionType.MULTIPLECHOICE, 0L);
    }

    private List<Answer> newAnswersFromField() throws ServiceException {
        logger.debug("Collecting all answers");
        List<Answer> newAnswers = new LinkedList<>();
        if(!textfieldAnswerOne.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE,
                textfieldAnswerOne.getText(),
                checkBoxAnswerOne.isSelected()));
        }
        if(!textfieldAnswerTwo.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE,
                textfieldAnswerTwo.getText(),
                checkBoxAnswerTwo.isSelected()));
        }
        if(!textfieldAnswerThree.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE,
                textfieldAnswerThree.getText(),
                checkBoxAnswerThree.isSelected()));
        }
        if(!textfieldAnswerFour.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE,
                textfieldAnswerFour.getText(),
                checkBoxAnswerFour.isSelected()));
        }

        if(newAnswers.isEmpty()) {
            throw new ServiceException("At least one answer must be given.");
        }

        for(Answer a : newAnswers) {
            if(a.isCorrect()) {
                return newAnswers;
            }
        }

        throw new ServiceException("At least one question must be true.");
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder
            .alertType(Alert.AlertType.ERROR)
            .title("Error")
            .headerText("An error occurred")
            .contentText(e.getMessage())
            .build();
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = alertBuilder
            .alertType(Alert.AlertType.INFORMATION)
            .title("Success")
            .headerText("The operation was successful!")
            .contentText(msg)
            .build();
        alert.showAndWait();
    }
}
