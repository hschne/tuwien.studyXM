package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import javafx.scene.control.ToggleGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing creation of multiple choice questions <p> Created by Julian on
 * 15.05.2016.
 */
@Component public class CreateMultipleChoiceQuestionController extends QuestionController {
    @FXML private TextArea textAreaQuestion;
    @FXML private CheckBox checkBoxAnswerOne;
    @FXML private CheckBox checkBoxAnswerTwo;
    @FXML private CheckBox checkBoxAnswerThree;
    @FXML private CheckBox checkBoxAnswerFour;
    @FXML private ChoiceBox<String> choiceBoxQuestionTime;
    @FXML private ToggleGroup tagToggleGroupMultiple;

    @Autowired public CreateMultipleChoiceQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        super(questionService, resourceQuestionService);
    }

    @Override protected void initializeToggleGroup() {
        tagToggleGroupMultiple.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });
        setColorOfOtherButtonsToGrey(tagToggleGroupMultiple);
    }

    @FXML public void handleCreateQuestion() {
        if (createQuestion()) {
            return;
        }
        if (checkBoxContinue.isSelected()) {
            questionNavigation.handleMultipleChoiceQuestion(this.topic);
        } else {
            mainFrameController.handleSubjects();
        }
        showSuccess("Inserted new question into database.");
    }

    @FXML public void toggleSelected() {
        setColorOfOtherButtonsToGrey(tagToggleGroupMultiple);
    }

    @Override protected void fillFieldsAndCheckboxes() {
        this.textAreaQuestion.setText(inputs == null ? "" : (String) inputs.get(0));

        fillAnswerFields(1);

        this.checkBoxAnswerOne.setSelected(inputs != null && (boolean) inputs.get(5));
        this.checkBoxAnswerTwo.setSelected(inputs != null && (boolean) inputs.get(6));
        this.checkBoxAnswerThree.setSelected(inputs != null && (boolean) inputs.get(7));
        this.checkBoxAnswerFour.setSelected(inputs != null && (boolean) inputs.get(8));

        this.checkBoxContinue.setSelected(inputs != null && (boolean) inputs.get(9));

        if (inputs != null) {
            this.choiceBoxQuestionTime.setValue(inputs.get(10).toString());
        }

        this.resource = inputs == null ? null : (ObservableResource) inputs.get(11);
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

    @Override protected void saveChoiceBoxQuestionTime(List inputs) {
        inputs.add(choiceBoxQuestionTime.getValue());
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

    @Override protected List<Boolean> createCheckBoxResults() {
        List<Boolean> result = new ArrayList<>();
        result.add(checkBoxAnswerOne.isSelected());
        result.add(checkBoxAnswerTwo.isSelected());
        result.add(checkBoxAnswerThree.isSelected());
        result.add(checkBoxAnswerFour.isSelected());
        return result;
    }

    @Override protected Question newQuestionFromFields() {
        logger.debug("Collecting question from field.");
        return new Question(textAreaQuestion.getText(), getQuestionType(),
            Integer.parseInt(choiceBoxQuestionTime.getValue().substring(0, 1)), getSelectedTag());
    }
}
