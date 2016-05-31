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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for managing creation of multiple choice questions
 * <p>
 * Created by Julian on 15.05.2016.
 */
@Component public class CreateMultipleChoiceQuestionController extends QuestionController {
    @FXML private TextArea textAreaQuestion;
    @FXML private TextField textfieldAnswerOne;
    @FXML private TextField textfieldAnswerTwo;
    @FXML private TextField textfieldAnswerThree;
    @FXML private TextField textfieldAnswerFour;
    @FXML private CheckBox checkBoxAnswerOne;
    @FXML private CheckBox checkBoxAnswerTwo;
    @FXML private CheckBox checkBoxAnswerThree;
    @FXML private CheckBox checkBoxAnswerFour;
    @FXML private CheckBox checkBoxContinue;

    @Autowired public CreateMultipleChoiceQuestionController(QuestionService questionService,
        AlertBuilder alertBuilder, SpringFXMLLoader fxmlLoader) {
        super(questionService, alertBuilder, fxmlLoader);
    }

    @FXML public void handleCreateQuestion() {
        if (createQuestion()) {
            return;
        }
        if (checkBoxContinue.isSelected()) {
            mainFrameController.handleMultipleChoiceQuestion(this.topic);
        } else {
            mainFrameController.handleSubjects();
        }
        showSuccess("Inserted new question into database.");
    }

    protected void fillFieldsAndCheckboxes() {
        this.textAreaQuestion.setText(inputs == null ? "" : (String) inputs.get(0));

        this.textfieldAnswerOne.setText(inputs == null ? "" : (String) inputs.get(1));
        this.textfieldAnswerTwo.setText(inputs == null ? "" : (String) inputs.get(2));
        this.textfieldAnswerThree.setText(inputs == null ? "" : (String) inputs.get(3));
        this.textfieldAnswerFour.setText(inputs == null ? "" : (String) inputs.get(4));

        this.checkBoxAnswerOne.setSelected(inputs != null && (boolean) inputs.get(5));
        this.checkBoxAnswerTwo.setSelected(inputs != null && (boolean) inputs.get(6));
        this.checkBoxAnswerThree.setSelected(inputs != null && (boolean) inputs.get(7));
        this.checkBoxAnswerFour.setSelected(inputs != null && (boolean) inputs.get(8));

        this.checkBoxContinue.setSelected(inputs == null || (boolean) inputs.get(9));
    }

    private boolean createQuestion() {
        logger.info("Now creating new question");
        Question newQuestion;
        try {
            List<Answer> answers = newAnswersFromField();
            newQuestion = questionService.createQuestion(newQuestionFromField(), topic.getT());
            questionService.setCorrespondingAnswers(newQuestion, answers);
        } catch (ServiceException e) {
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

        return new Question(textAreaQuestion.getText(), QuestionType.MULTIPLECHOICE, 1L);
    }

    @Override protected List getUserInput() {
        List inputs = new ArrayList<>();

        saveAnswerFields(inputs);
        saveCheckboxes(inputs);

        return inputs;
    }

    private void saveAnswerFields(List inputs) {
        if (textAreaQuestion.getText() != null) {
            inputs.add(textAreaQuestion.getText());
        } else {
            inputs.add(null);
        }

        if (textfieldAnswerOne.getText() != null) {
            inputs.add(textfieldAnswerOne.getText());
        } else {
            inputs.add(null);
        }

        if (textfieldAnswerTwo.getText() != null) {
            inputs.add(textfieldAnswerTwo.getText());
        } else {
            inputs.add(null);
        }

        if (textfieldAnswerThree.getText() != null) {
            inputs.add(textfieldAnswerThree.getText());
        } else {
            inputs.add(null);
        }

        if (textfieldAnswerFour.getText() != null) {
            inputs.add(textfieldAnswerFour.getText());
        } else {
            inputs.add(null);
        }
    }

    private void saveCheckboxes(List inputs) {
        inputs.add(checkBoxAnswerOne.isSelected());
        inputs.add(checkBoxAnswerTwo.isSelected());
        inputs.add(checkBoxAnswerThree.isSelected());
        inputs.add(checkBoxAnswerFour.isSelected());

        inputs.add(checkBoxContinue.isSelected());
    }

    private List<Answer> newAnswersFromField() throws ServiceException {
        logger.debug("Collecting all answers");
        List<Answer> newAnswers = new LinkedList<>();
        if (!textfieldAnswerOne.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerOne.getText(),
                checkBoxAnswerOne.isSelected()));
        }
        if (!textfieldAnswerTwo.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerTwo.getText(),
                checkBoxAnswerTwo.isSelected()));
        }
        if (!textfieldAnswerThree.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerThree.getText(),
                checkBoxAnswerThree.isSelected()));
        }
        if (!textfieldAnswerFour.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerFour.getText(),
                checkBoxAnswerFour.isSelected()));
        }

        if (newAnswers.isEmpty()) {
            throw new ServiceException("At least one answer must be given.");
        }

        for (Answer a : newAnswers) {
            if (a.isCorrect()) {
                return newAnswers;
            }
        }

        throw new ServiceException("At least one answer must be true.");
    }
}
