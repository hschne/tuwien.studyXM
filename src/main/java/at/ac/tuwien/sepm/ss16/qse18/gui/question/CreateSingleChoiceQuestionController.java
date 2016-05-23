package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
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

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Felix on 19.05.2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class CreateSingleChoiceQuestionController
    implements GuiController {

    private Logger logger = LogManager.getLogger(CreateSingleChoiceQuestionController.class);
    private Stage primaryStage;
    private AlertBuilder alertBuilder;
    private QuestionService questionService;
    private SpringFXMLLoader springFXMLLoader;

    @FXML private TextArea textAreaQuestion;
    @FXML private TextField textfieldAnswerOne;
    @FXML private TextField textfieldAnswerTwo;
    @FXML private TextField textfieldAnswerThree;
    @FXML private TextField textfieldAnswerFour;
    @FXML private RadioButton radioButtonAnswerOne;
    @FXML private RadioButton radioButtonAnswerTwo;
    @FXML private RadioButton radioButtonAnswerThree;
    @FXML private RadioButton radioButtonAnswerFour;
    @Autowired MainFrameController mainFrameController;
    private ObservableTopic topic;

    /**
     * Creates a controller for the single choice question creation.
     *
     * @param springFXMLLoader The autowired spring framework FXML loader.
     * @param questionService The question service which saves a given question and answers
     *                        persistently.
     * @param alertBuilder An alert builder which wraps pop ups for user interaction.
     */
    @Autowired
    public CreateSingleChoiceQuestionController(SpringFXMLLoader springFXMLLoader,
        QuestionService questionService, AlertBuilder alertBuilder) {
        this.springFXMLLoader = springFXMLLoader;
        this.questionService = questionService;
        this.alertBuilder = alertBuilder;
    }

    public void setTopic(ObservableTopic topic) {
        this.topic = topic;
    }

    @FXML public void createQuestion() {
        logger.info("Now creating new question");
        Question newQuestion;
        try {
            newQuestion = questionService.createQuestion(newQuestionFromField(), topic.getT());
            questionService.setCorrespondingAnswers(newQuestion, newAnswersFromField());
        } catch (ServiceException e) {
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

        return new Question(textAreaQuestion.getText(), QuestionType.SINGLECHOICE, 0L);
    }

    private List<Answer> newAnswersFromField() throws ServiceException {
        logger.debug("Collecting all answers");
        List<Answer> newAnswers = new LinkedList<>();

        if(!textfieldAnswerOne.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE,
                textfieldAnswerOne.getText(),
                radioButtonAnswerOne.isSelected()));
        }
        if(!textfieldAnswerTwo.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE,
                textfieldAnswerTwo.getText(),
                radioButtonAnswerTwo.isSelected()));
        }
        if(!textfieldAnswerThree.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE,
                textfieldAnswerThree.getText(),
                radioButtonAnswerThree.isSelected()));
        }
        if(!textfieldAnswerFour.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE,
                textfieldAnswerFour.getText(),
                radioButtonAnswerFour.isSelected()));
        }

        if(newAnswers.isEmpty()) {
            throw new ServiceException("At least one answer must be given.");
        }

        for(Answer a : newAnswers) {
            if(a.isCorrect()) {
                return newAnswers;
            }
        }

        throw new ServiceException("At least one given answer must be true.");
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
