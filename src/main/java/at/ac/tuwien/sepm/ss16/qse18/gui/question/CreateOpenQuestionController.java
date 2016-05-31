package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller for managing the creation of open questions
 * <p>
 * Created by Julian on 15.05.2016.
 */
@Component public class CreateOpenQuestionController extends QuestionController {

    @FXML private TextArea textAreaQuestion;

    @Autowired
    public CreateOpenQuestionController(QuestionService questionService,
        SpringFXMLLoader fxmlLoader) {
        super(questionService, fxmlLoader);
    }

    @Override protected void fillFieldsAndCheckboxes() {
        this.textAreaQuestion.setText(inputs == null ? "" : (String) inputs.get(0));

        fillAnswerFields(1);

        this.checkBoxContinue.setSelected(inputs == null || (boolean) inputs.get(5));

        this.resource = (inputs == null ? null : (ObservableResource) inputs.get(6));
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
        // There are no correct or incorrect answers given for this
        // type of question therefore there are no other checkboxes to save.

        inputs.add(checkBoxContinue.isSelected());
    }

    @Override protected QuestionType getQuestionType() {
        return QuestionType.OPENQUESTION;
    }

    /**
     * Handles the button event of the "create question" button.
     */
    @FXML public void handleCreateQuestion() {
        if (!createQuestion()) {
            return;
        }
        if (checkBoxContinue.isSelected()) {
            mainFrameController.handleOpenQuestion(this.topic);
        } else {
            mainFrameController.handleSubjects();
        }
        showSuccess("Inserted new question into database.");
    }

    /**
     * Creates a new question.
     *
     * @return true if creation was successful, false else.
     */
    private boolean createQuestion() {
        logger.info("Creating new question");
        Question newQuestion;
        try {
            List<Answer> answers = newAnswersFromField();
            newQuestion = questionService.createQuestion(newQuestionFromField(), topic.getT());
            questionService.setCorrespondingAnswers(newQuestion, answers);
        } catch (ServiceException e) {
            showError(e);
            return false;
        }
        return true;
    }

    /**
     * Returns a question generated by the user input.
     *
     * @return generated question
     * @throws ServiceException
     */
    private Question newQuestionFromField() throws ServiceException {
        logger.info("Collecting question from field.");
        if (textAreaQuestion.getText().isEmpty()) {
            throw new ServiceException("The question must not be empty.");
        }
        return new Question(textAreaQuestion.getText(), QuestionType.OPENQUESTION, 1L);
    }

    /**
     * Returns a list of answers (keywords) generated by the user input.
     * List size can be [0,4] depending on the modified text fields.
     *
     * @return list of answers (keywords)
     * @throws ServiceException
     */
    private List<Answer> newAnswersFromField() throws ServiceException {
        logger.info("Collecting keywords from fields");
        List<Answer> newAnswers = new LinkedList<>();
        if (!textFieldAnswerOne.getText().isEmpty()) {
            checkKeywordForSpaces(textFieldAnswerOne.getText());
            newAnswers
                .add(new Answer(QuestionType.OPENQUESTION, textFieldAnswerOne.getText(), true));
        }
        if (!textFieldAnswerTwo.getText().isEmpty()) {
            checkKeywordForSpaces(textFieldAnswerTwo.getText());
            newAnswers
                .add(new Answer(QuestionType.OPENQUESTION, textFieldAnswerTwo.getText(), true));
        }
        if (!textFieldAnswerThree.getText().isEmpty()) {
            checkKeywordForSpaces(textFieldAnswerThree.getText());
            newAnswers
                .add(new Answer(QuestionType.OPENQUESTION, textFieldAnswerThree.getText(), true));
        }
        if (!textFieldAnswerFour.getText().isEmpty()) {
            checkKeywordForSpaces(textFieldAnswerFour.getText());
            newAnswers
                .add(new Answer(QuestionType.OPENQUESTION, textFieldAnswerFour.getText(), true));
        }
        if (newAnswers.isEmpty()) {
            logger.info("No keyword was given.");
            throw new ServiceException("At least one keyword must be given.");
        }
        return newAnswers;
    }

    /**
     * Checks if a keyword is valid. A keyword is valid if it contains no white spaces.
     *
     * @param keyword keyword to check
     * @throws ServiceException
     */
    private void checkKeywordForSpaces(String keyword) throws ServiceException {
        logger.info("Validating keyword: " + keyword);
        Pattern p = Pattern.compile("\\s");
        Matcher m = p.matcher(keyword);
        if (m.find()) {
            logger.info(keyword + "is not a valid keyword.");
            throw new ServiceException(keyword + " is not a valid keyword. ");
        }
        logger.info(keyword + "is a valid keyword.");
    }
}

