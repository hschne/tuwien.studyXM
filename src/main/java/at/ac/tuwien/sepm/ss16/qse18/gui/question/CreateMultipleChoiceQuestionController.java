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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Autowired public CreateMultipleChoiceQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService,
        SpringFXMLLoader fxmlLoader) {
        super(questionService, resourceQuestionService, fxmlLoader);
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
        inputs.addAll(createCheckBoxResults());
        inputs.add(checkBoxContinue.isSelected());
    }

    @Override protected QuestionType getQuestionType() {
        return QuestionType.MULTIPLECHOICE;
    }

    private boolean createQuestion() {
        logger.info("Now creating new question");
        try {
            createQuestionAndAnswers();
        } catch (ServiceException | DtoValidatorException e) {
            showError(e);
            return true;
        }
        return false;
    }


    @Override
    protected List<Boolean> createCheckBoxResults(){
        List<Boolean> result = new ArrayList<>();
        result.add(checkBoxAnswerOne.isSelected());
        result.add(checkBoxAnswerTwo.isSelected());
        result.add(checkBoxAnswerThree.isSelected());
        result.add(checkBoxAnswerFour.isSelected());
        return result;
    }

    @Override protected Question newQuestionFromFields() {
        logger.debug("Collecting question from field.");
        return new Question(textAreaQuestion.getText(), getQuestionType(), 1L);
    }
}
