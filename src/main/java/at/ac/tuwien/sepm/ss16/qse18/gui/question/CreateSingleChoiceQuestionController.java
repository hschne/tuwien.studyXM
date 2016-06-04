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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing creation of single choice questions
 * <p>
 * Created by Felix on 19.05.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CreateSingleChoiceQuestionController extends QuestionController {
    @FXML private TextArea textAreaQuestion;
    @FXML private RadioButton radioButtonAnswerOne;
    @FXML private RadioButton radioButtonAnswerTwo;
    @FXML private RadioButton radioButtonAnswerThree;
    @FXML private RadioButton radioButtonAnswerFour;
    @FXML private ChoiceBox<String> choiceBoxQuestionTime;

    /**
     * Creates a controller for the single choice question creation.
     *
     * @param questionService The question service which saves a given question and answers
     *                        persistently.
     */
    @Autowired public CreateSingleChoiceQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        super(questionService, resourceQuestionService);

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
        try {
            createQuestionAndAnswers();
        } catch (ServiceException | DtoValidatorException e) {
            logger.error("Could not create new question", e);
            showError(e);
            return true;
        }
        return false;
    }

    @Override protected void fillFieldsAndCheckboxes() {
        this.textAreaQuestion.setText(inputs == null ? "" : (String) inputs.get(0));

        fillAnswerFields(1);

        this.radioButtonAnswerOne.setSelected(inputs != null && (boolean) inputs.get(5));
        this.radioButtonAnswerTwo.setSelected(inputs != null && (boolean) inputs.get(6));
        this.radioButtonAnswerThree.setSelected(inputs != null && (boolean) inputs.get(7));
        this.radioButtonAnswerFour.setSelected(inputs != null && (boolean) inputs.get(8));

        this.checkBoxContinue.setSelected(inputs != null && (boolean) inputs.get(9));

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
        return QuestionType.SINGLECHOICE;
    }

    @Override protected Question newQuestionFromFields() {
        logger.info("Collecting question from field.");
        return new Question(textAreaQuestion.getText(), getQuestionType()
                , Integer.parseInt(choiceBoxQuestionTime.getValue().substring(0,1)));
    }

    @Override protected List<Boolean> createCheckBoxResults() {
        List<Boolean> result = new ArrayList<>();
        result.add(radioButtonAnswerOne.isSelected());
        result.add(radioButtonAnswerTwo.isSelected());
        result.add(radioButtonAnswerThree.isSelected());
        result.add(radioButtonAnswerFour.isSelected());
        return result;
    }

}
