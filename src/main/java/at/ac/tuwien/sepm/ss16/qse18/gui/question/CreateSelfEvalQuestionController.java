package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Ganiu
 */
@Component public class CreateSelfEvalQuestionController extends CreateImageQuestionController {
    @FXML private TextArea questionTextArea;


    @Autowired public CreateSelfEvalQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        super(questionService, resourceQuestionService);
    }

    @Override @FXML public void handleCreateQuestion() {
        if (createQuestion()) {
            return;
        }
        if (checkBoxContinue.isSelected()) {
            questionNavigation.handleSelfEvalQuestion(this.topic,null);
        } else {
            mainFrameController.handleSubjects();
        }
        showSuccess("Question is now in the database");
    }

    @Override @FXML public void handleAddImage() {
        super.handleAddImage();
        this.textFieldAnswerOne.setText(textFieldImagePath.getText());
    }

    @Override protected void fillFieldsAndCheckboxes() {
        this.questionTextArea.setText(inputs == null ? "" : (String) inputs.get(0));
        this.textFieldImagePath.setText(inputs == null ? "" : (String) inputs.get(1));
        fillAnswerFields(textFieldImagePath.getText());
        this.imageViewSelectedImage.setImage(inputs == null
            ? new Image("/images/imagePlaceholder.png") :(Image)inputs.get(2));

        this.checkBoxContinue.setSelected(inputs != null && (boolean) inputs.get(7));

        if (inputs != null) {
            this.choiceBoxQuestionTime.setValue(inputs.get(8).toString());
        }
        this.resource = inputs == null ? null : (ObservableResource) inputs.get(9);
        this.resourceLabel.setText(resource == null ? "none" : resource.getName());
    }

    @Override protected QuestionType getQuestionType() {
        return QuestionType.SELF_EVALUATION;
    }


    @Override protected void saveQuestionInput(List inputs) {
        inputs.add(questionTextArea.getText().isEmpty() ? "" : questionTextArea.getText() );
    }

    @Override protected void saveAnswerFields(List listForInputs) {
        saveQuestionInput(listForInputs);
        if (textFieldImagePath != null) {
            listForInputs.add(textFieldImagePath.getText());
            listForInputs.add(imageViewSelectedImage.getImage());
        } else {
            inputs.add(null);
        }

    }

    @Override protected Question newQuestionFromFields() {
        logger.debug("Creating new question");
        return new Question(questionTextArea.getText() , getQuestionType(),
            Integer.parseInt(choiceBoxQuestionTime.getValue().substring(0, 1)));
    }

    @Override protected List<Boolean> createCheckBoxResults() {
        List<Boolean> result = new ArrayList<>();
        result.add(true);
        result.add(true);
        result.add(true);
        result.add(true);
        return result;
    }


}

