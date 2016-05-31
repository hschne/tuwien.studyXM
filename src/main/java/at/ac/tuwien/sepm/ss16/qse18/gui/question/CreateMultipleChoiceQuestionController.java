package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.IllegalFormatCodePointException;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for managing creation of multiple choice questions
 * <p>
 * Created by Julian on 15.05.2016.
 */
@Component public class CreateMultipleChoiceQuestionController extends QuestionController {
    @FXML private TextArea textAreaQuestion;
    @FXML private CheckBox checkBoxAnswerOne;
    @FXML private CheckBox checkBoxAnswerTwo;
    @FXML private CheckBox checkBoxAnswerThree;
    @FXML private CheckBox checkBoxAnswerFour;

    @Autowired public CreateMultipleChoiceQuestionController(QuestionService questionService, SpringFXMLLoader fxmlLoader) {
        super(questionService, fxmlLoader);
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

        fillAnswerFields(1);

        this.checkBoxAnswerOne.setSelected(inputs != null && (boolean) inputs.get(5));
        this.checkBoxAnswerTwo.setSelected(inputs != null && (boolean) inputs.get(6));
        this.checkBoxAnswerThree.setSelected(inputs != null && (boolean) inputs.get(7));
        this.checkBoxAnswerFour.setSelected(inputs != null && (boolean) inputs.get(8));

        this.checkBoxContinue.setSelected(inputs == null || (boolean) inputs.get(9));

        this.resource = (inputs == null ? null : (ObservableResource) inputs.get(10));
        this.resourceLabel.setText(resource == null ? "none" : resource.getName());
    }

    @Override protected void saveQuestionInput(List inputs) {
        if (textAreaQuestion != null) {
            inputs.add(textAreaQuestion.getText());
        } else {
            inputs.add(null);
        }
    }

    @Override protected void saveCheckboxesAndRadiobuttons(List inputs) {
        inputs.add(checkBoxAnswerOne.isSelected());
        inputs.add(checkBoxAnswerTwo.isSelected());
        inputs.add(checkBoxAnswerThree.isSelected());
        inputs.add(checkBoxAnswerFour.isSelected());
        inputs.add(checkBoxContinue.isSelected());
    }

    @Override protected QuestionType getQuestionType() {
        return QuestionType.MULTIPLECHOICE;
    }

    private boolean createQuestion() {
        logger.info("Now creating new question");
        Question newQuestion;
        try {
            List<Answer> answers = newAnswersFromField();
            newQuestion = questionService.createQuestion(newQuestionFromField(), topic.getT());
            questionService.setCorrespondingAnswers(newQuestion, answers);
        } catch (ServiceException | IllegalArgumentException e) {
            showError(e);
            return true;
        }
        return false;
    }

    private Question newQuestionFromField() {
        logger.info("Collecting question from field.");
        if (textAreaQuestion.getText().isEmpty()) {
            throw new IllegalArgumentException("The question must not be empty.");
        }

        return new Question(textAreaQuestion.getText(), QuestionType.MULTIPLECHOICE, 1L);
    }

    private List<Answer> newAnswersFromField() {
        logger.debug("Collecting all answers");
        List<Answer> newAnswers = new LinkedList<>();
        if (!textFieldAnswerOne.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textFieldAnswerOne.getText(),
                checkBoxAnswerOne.isSelected()));
        }
        if (!textFieldAnswerTwo.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textFieldAnswerTwo.getText(),
                checkBoxAnswerTwo.isSelected()));
        }
        if (!textFieldAnswerThree.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textFieldAnswerThree.getText(),
                checkBoxAnswerThree.isSelected()));
        }
        if (!textFieldAnswerFour.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textFieldAnswerFour.getText(),
                checkBoxAnswerFour.isSelected()));
        }

        if (newAnswers.isEmpty()) {
            throw new IllegalArgumentException("At least one answer must be given.");
        }

        for (Answer a : newAnswers) {
            if (a.isCorrect()) {
                return newAnswers;
            }
        }

        throw new IllegalArgumentException("At least one answer must be true.");
    }
}
