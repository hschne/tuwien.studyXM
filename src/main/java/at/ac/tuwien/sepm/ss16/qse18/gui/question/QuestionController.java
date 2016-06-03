package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceChooserController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * Abstract base class to be used by any controllers for question creation
 *
 * @author Hans-Joerg Schroedl
 */

@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public abstract class QuestionController
    extends BaseController {

    final QuestionService questionService;
    final ResourceQuestionService resourceQuestionService;
    protected ObservableTopic topic;
    protected ObservableResource resource;
    @FXML protected TextField textFieldAnswerOne;
    @FXML protected TextField textFieldAnswerTwo;
    @FXML protected TextField textFieldAnswerThree;
    @FXML protected TextField textFieldAnswerFour;
    @FXML protected CheckBox checkBoxContinue;
    @FXML protected Label resourceLabel;
    List inputs;
    private SpringFXMLLoader fxmlLoader;

    @Autowired public QuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService, SpringFXMLLoader fxmlLoader) {
        this.questionService = questionService;
        this.resourceQuestionService = resourceQuestionService;

        this.fxmlLoader = fxmlLoader;
    }

    @FXML public void handleAddResource() {
        // saving input from user to be able to recover later
        inputs = getUserInput();

        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, ResourceChooserController> resourceChooserWrapper =
            null;

        try {
            resourceChooserWrapper = fxmlLoader.loadAndWrap("/fxml/resource/resourceChooser.fxml",
                ResourceChooserController.class);
        } catch (IOException e) {
            logger.error("Could not load resourceChooser.fxml", e);
        }

        ResourceChooserController childController;

        if (resourceChooserWrapper != null) {
            childController = resourceChooserWrapper.getController();
            childController.setStage(stage);
            childController.saveUserInput(inputs, getQuestionType());
            childController.setResourceLabel(resourceLabel);

            stage.setTitle("Add resource");
            stage.setScene(new Scene((Parent) resourceChooserWrapper.getLoadedObject(), 500, 400));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        }

        // Save the chosen or newly created resource
        resource = (ObservableResource) inputs.get(inputs.size() - 1);
    }

    /**
     * Sets the topic in which the question should be created
     *
     * @param topic The topic
     */
    public void setTopic(ObservableTopic topic) {
        if (topic != null) {
            this.topic = topic;
        }
    }

    public void setInput(List inputs) {
        this.inputs = inputs;
        fillFieldsAndCheckboxes();
    }

    protected abstract void fillFieldsAndCheckboxes();

    protected abstract void saveQuestionInput(List inputs);

    void fillAnswerFields(int startWith) {
        int counter = startWith;
        this.textFieldAnswerOne.setText(inputs == null ? "" : (String) inputs.get(counter));
        this.textFieldAnswerTwo.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerThree.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerFour.setText(inputs == null ? "" : (String) inputs.get(++counter));
    }

    List getUserInput() {
        List inputs = new ArrayList<>();

        saveAnswerFields(inputs);
        saveCheckboxesAndRadiobuttons(inputs);

        inputs.add(resource);

        return inputs;
    }

    void saveAnswerFields(List inputs) {
        saveQuestionInput(inputs);

        if (textFieldAnswerOne.getText() != null) {
            inputs.add(textFieldAnswerOne.getText());
        } else {
            inputs.add(null);
        }

        if (textFieldAnswerTwo.getText() != null) {
            inputs.add(textFieldAnswerTwo.getText());
        } else {
            inputs.add(null);
        }

        if (textFieldAnswerThree.getText() != null) {
            inputs.add(textFieldAnswerThree.getText());
        } else {
            inputs.add(null);
        }

        if (textFieldAnswerFour.getText() != null) {
            inputs.add(textFieldAnswerFour.getText());
        } else {
            inputs.add(null);
        }
    }


    void createQuestionAndAnswers() throws DtoValidatorException, ServiceException {
        Question question = newQuestionFromFields();
        validate(question);
        List<Answer> answers = newAnswersFromFields(createCheckBoxResults());
        validate(answers);
        Question createdQuestion = questionService.createQuestion(question, topic.getT());
        questionService.setCorrespondingAnswers(createdQuestion, answers);

        if (resource != null) {
            resourceQuestionService.createReference(resource.getResource(), createdQuestion);
        }
    }


    protected abstract void saveCheckboxesAndRadiobuttons(List inputs);

    protected abstract QuestionType getQuestionType();

    protected abstract List<Boolean> createCheckBoxResults();

    protected abstract Question newQuestionFromFields();

    /**
     * Returns a list of answers generated by the user input.
     * List size can be [0,4] depending on the modified text fields.
     *
     * @return list of answers
     */
    List<Answer> newAnswersFromFields(List<Boolean> checkBoxResults) {
        List<Answer> answers = new LinkedList<>();
        QuestionType questionType = getQuestionType();
        if (!textFieldAnswerOne.getText().isEmpty()) {
            answers.add(
                new Answer(questionType, textFieldAnswerOne.getText(), checkBoxResults.get(0)));
        }
        if (!textFieldAnswerTwo.getText().isEmpty()) {
            answers.add(
                new Answer(questionType, textFieldAnswerTwo.getText(), checkBoxResults.get(1)));
        }
        if (!textFieldAnswerThree.getText().isEmpty()) {
            answers.add(
                new Answer(questionType, textFieldAnswerThree.getText(), checkBoxResults.get(2)));
        }
        if (!textFieldAnswerFour.getText().isEmpty()) {
            answers.add(
                new Answer(questionType, textFieldAnswerFour.getText(), checkBoxResults.get(3)));
        }
        return answers;
    }

}
