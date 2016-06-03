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

    private final QuestionService questionService;
    private final ResourceQuestionService resourceQuestionService;
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

    /**
     * Saves all textfields and checkboxes/radiobuttons and the resource the user typed in
     * or selected so the current state can be restored after exiting the view.
     * This is especially useful when the user gets redirected to the resource mask
     * when selecting a resource.
     *
     * @return  a list of all inputs from the user (textfields/checkboxes/radiobuttons)
     *          Note:   if a textfield is empty this method saves null instead of nothing to
     *                  have a consistent list structure
     */
    protected List getUserInput() {
        List tmpInputs = new ArrayList<>();

        saveAnswerFields(tmpInputs);
        saveCheckboxesAndRadiobuttons(tmpInputs);

        tmpInputs.add(resource);

        return tmpInputs;
    }

    /**
     * This abstract method fills every textfield, checkbox and radiobutton. The content of these
     * are given via the global "inputs" list.
     *
     * The method is abstract because not every question type has checkboxes
     * and therefore it can not be unified.
     */
    protected abstract void fillFieldsAndCheckboxes();

    /**
     * Saves the question input. This method has to be abstract because the openquestion type
     * has an image AND an filepath to save.
     *
     * @param inputs The list to save the input
     */
    protected abstract void saveQuestionInput(List inputs);

    /**
     * Saves the state of the checkboxes and/or radiobuttons. this method has to be abstract because
     * the singlechoicequestion type has radiobuttons instead of checkboxes.
     *
     * @param inputs The list to save the input
     */
    protected abstract void saveCheckboxesAndRadiobuttons(List inputs);

    /**
     * Fills all answerfields if the previous input from the user is saved.
     *
     * @param startWith Because the input-list has a consistent structure
     *                  (question, answerfields, checkboxes/radiobuttons,
     *                  continue-chechbox, resource) the method needs the index where the
     *                  answerfields should start
     */
    protected void fillAnswerFields(int startWith) {
        int counter = startWith;
        this.textFieldAnswerOne.setText(inputs == null ? "" : (String) inputs.get(counter));
        this.textFieldAnswerTwo.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerThree.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerFour.setText(inputs == null ? "" : (String) inputs.get(++counter));
    }

    /**
     * Saves the answerfield texts. If a textfield is empty it saves null instead of nothing to be
     * consistent.
     *
     * @param listForInputs The list to save the input
     */
    protected void saveAnswerFields(List listForInputs) {
        saveQuestionInput(listForInputs);

        if (textFieldAnswerOne.getText() != null) {
            listForInputs.add(textFieldAnswerOne.getText());
        } else {
            listForInputs.add(null);
        }

        if (textFieldAnswerTwo.getText() != null) {
            listForInputs.add(textFieldAnswerTwo.getText());
        } else {
            listForInputs.add(null);
        }

        if (textFieldAnswerThree.getText() != null) {
            listForInputs.add(textFieldAnswerThree.getText());
        } else {
            listForInputs.add(null);
        }

        if (textFieldAnswerFour.getText() != null) {
            listForInputs.add(textFieldAnswerFour.getText());
        } else {
            listForInputs.add(null);
        }
    }

    @FXML public void handleAddResource() {
        // saving input from user to be able to recover later
        inputs = getUserInput();

        /*
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
        */

        mainFrameController.handleChooseResource(inputs, resourceLabel, getQuestionType());

        // Save the chosen or newly created resource
        resource = (ObservableResource) inputs.get(inputs.size() - 1);
    }

    protected abstract QuestionType getQuestionType();

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

    private List getUserInput() {
        List result = new ArrayList<>();

        saveAnswerFields(result);
        saveCheckboxesAndRadiobuttons(result);

        result.add(resource);

        return result;
    }

    private void saveAnswerFields(List inputs) {
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
    private List<Answer> newAnswersFromFields(List<Boolean> checkBoxResults) {
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
