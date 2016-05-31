package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller for managing creation of single choice questions
 * <p>
 * Created by Felix on 19.05.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CreateSingleChoiceQuestionController extends QuestionController {
    @FXML private TextArea textAreaQuestion;
    @FXML private TextField textfieldAnswerOne;
    @FXML private TextField textfieldAnswerTwo;
    @FXML private TextField textfieldAnswerThree;
    @FXML private TextField textfieldAnswerFour;
    @FXML private RadioButton radioButtonAnswerOne;
    @FXML private RadioButton radioButtonAnswerTwo;
    @FXML private RadioButton radioButtonAnswerThree;
    @FXML private RadioButton radioButtonAnswerFour;
    @FXML private CheckBox checkBoxContinue;

    /**
     * Creates a controller for the single choice question creation.
     *
     * @param questionService The question service which saves a given question and answers
     *                        persistently.
     * @param alertBuilder    An alert builder which wraps pop ups for user interaction.
     */
    @Autowired public CreateSingleChoiceQuestionController(QuestionService questionService,
        AlertBuilder alertBuilder, SpringFXMLLoader fxmlLoader) {
        super(questionService, alertBuilder, fxmlLoader);

    }

    @Override protected void fillFieldsAndCheckboxes() {
        // TODO: make this view fill its fields and checkboxes
    }

    @Override protected List getUserInput() {
        // TODO: read user input and save it in inputs
        return null;
    }

    @FXML public void handleCreateQuestion() {
        if (createQuestion()) {
            return;
        }
        if (checkBoxContinue.isSelected()) {
            mainFrameController.handleSingleChoiceQuestion(this.topic);
        } else {
            mainFrameController.handleSubjects();
        }
        showSuccess("Inserted new question into database.");
    }

    private boolean createQuestion() {
        logger.info("Now creating new question");
        Question newQuestion;
        try {
            List<Answer> answers = newAnswersFromField();
            newQuestion = questionService.createQuestion(newQuestionFromField(), topic.getT());
            questionService.setCorrespondingAnswers(newQuestion, answers);
        } catch (ServiceException e) {
            logger.error("Could not create new question", e);
            showAlert(e);
            return true;
        }
        return false;
    }

    private Question newQuestionFromField() throws ServiceException {
        logger.info("Collecting question from field.");
        if (textAreaQuestion.getText().isEmpty()) {
            throw new ServiceException("The question must not be empty.");
        }
        return new Question(textAreaQuestion.getText(), QuestionType.SINGLECHOICE, 1L);
    }

    private List<Answer> newAnswersFromField() throws ServiceException {
        logger.debug("Collecting all answers");
        List<Answer> newAnswers = new LinkedList<>();

        if (!textfieldAnswerOne.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE, textfieldAnswerOne.getText(),
                radioButtonAnswerOne.isSelected()));
        }
        if (!textfieldAnswerTwo.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE, textfieldAnswerTwo.getText(),
                radioButtonAnswerTwo.isSelected()));
        }
        if (!textfieldAnswerThree.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE, textfieldAnswerThree.getText(),
                radioButtonAnswerThree.isSelected()));
        }
        if (!textfieldAnswerFour.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.SINGLECHOICE, textfieldAnswerFour.getText(),
                radioButtonAnswerFour.isSelected()));
        }

        if (newAnswers.isEmpty()) {
            throw new ServiceException("At least one answer must be given.");
        }

        for (Answer a : newAnswers) {
            if (a.isCorrect()) {
                return newAnswers;
            }
        }

        throw new ServiceException("At least one given answer must be true.");
    }
}
