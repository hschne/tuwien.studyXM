package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing the creation of open questions
 * <p>
 * Created by Julian on 15.05.2016.
 */
@Component public class CreateOpenQuestionController extends QuestionController {

    @FXML private TextArea textAreaQuestion;
    @FXML private ChoiceBox<String> choiceBoxQuestionTime;

    @Autowired public CreateOpenQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        super(questionService, resourceQuestionService);
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
        try {
            createQuestionAndAnswers();
        } catch (ServiceException | DtoValidatorException e) {
            showError(e);
            return false;
        }
        return true;
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

    @Override protected List<Boolean> createCheckBoxResults() {
        List<Boolean> result = new ArrayList<>();
        result.add(true);
        result.add(true);
        result.add(true);
        result.add(true);
        return result;
    }

    @Override protected Question newQuestionFromFields() {
        logger.debug("Collecting question from field.");
        return new Question(textAreaQuestion.getText(), getQuestionType()
                , Integer.parseInt(choiceBoxQuestionTime.getValue().substring(0,1)));
    }



}

