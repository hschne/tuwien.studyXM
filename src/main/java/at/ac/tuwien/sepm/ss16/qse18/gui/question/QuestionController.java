package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.QuestionNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

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
    @FXML protected ImageView imageViewFile;
    @FXML protected ChoiceBox choiceBoxQuestionTime;
    @FXML protected ToggleButton toggleEasy;
    @FXML protected ToggleButton toggleNormal;
    @FXML protected ToggleButton toggleHard;
    @FXML protected ToggleButton toggleImportant;
    @FXML protected Label tagLabel;

    List inputs;

    @Autowired QuestionNavigation questionNavigation;

    @Autowired public QuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        this.questionService = questionService;
        this.resourceQuestionService = resourceQuestionService;
    }

    @FXML public void intialize() {
        questionNavigation.refreshMainPane();
    }

    @FXML public void cancel() {
        questionNavigation.handleCreateQuestion(topic);
    }

    @FXML public void setTagLabelText() {
        Tag selected = getSelectedTag();
        if (selected != null) {
            tagLabel.setText(selected.toString());
        } else {
            tagLabel.setText("no tag selected");
        }
    }

    /**
     * Saves all textfields and checkboxes/radiobuttons and the resource the user typed in or
     * selected so the current state can be restored after exiting the view. This is especially
     * useful when the user gets redirected to the resource mask when selecting a resource.
     *
     * @return a list of all inputs from the user (textfields/checkboxes/radiobuttons) Note:   if a
     * textfield is empty this method saves null instead of nothing to have a consistent list
     * structure
     */
    protected List getUserInput() {
        List tmpInputs = new ArrayList<>();

        saveAnswerFields(tmpInputs);
        saveCheckboxesAndRadiobuttons(tmpInputs);
        saveChoiceBoxQuestionTime(tmpInputs);
        tmpInputs.add(resource);

        return tmpInputs;
    }

    /**
     * This abstract method fills every textfield, checkbox and radiobutton. The content of these
     * are given via the global "inputs" list. <p> The method is abstract because not every question
     * type has checkboxes and therefore it can not be unified.
     */
    protected abstract void fillFieldsAndCheckboxes();

    /**
     * Saves the question input. This method has to be abstract because the openquestion type has an
     * image AND an filepath to save.
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
     * Saves the state of the ChoiceBoxQuestionTime
     *
     * @param inputs The list to save the input
     */
    protected void saveChoiceBoxQuestionTime(List inputs) {
        inputs.add(choiceBoxQuestionTime.getValue());
    }

    /**
     * Fills all answerfields if the previous input from the user is saved.
     *
     * @param startWith Because the input-list has a consistent structure (question, answerfields,
     *                  checkboxes/radiobuttons, continue-chechbox, resource) the method needs the
     *                  index where the answerfields should start
     */
    protected void fillAnswerFields(int startWith) {
        int counter = startWith;
        this.textFieldAnswerOne.setText(inputs == null ? "" : (String) inputs.get(counter));
        this.textFieldAnswerTwo.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerThree.setText(inputs == null ? "" : (String) inputs.get(++counter));
        this.textFieldAnswerFour.setText(inputs == null ? "" : (String) inputs.get(++counter));
    }

    protected void fillAnswerFields(String text) {
        textFieldAnswerOne.setText(text);
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

        if (textFieldAnswerTwo != null){
            if(textFieldAnswerTwo.getText() != null) {
                listForInputs.add(textFieldAnswerTwo.getText());
            }
        } else {
            listForInputs.add(null);
        }

        if (textFieldAnswerThree != null){
            if(textFieldAnswerThree.getText() != null) {
                listForInputs.add(textFieldAnswerThree.getText());
            }
        } else {
            listForInputs.add(null);
        }

        if (textFieldAnswerFour != null){
            if(textFieldAnswerFour.getText() != null) {
                listForInputs.add(textFieldAnswerFour.getText());
            }
        } else {
            listForInputs.add(null);
        }
    }

    @FXML public void handleAddResource() {
        // saving input from user to be able to recover later
        inputs = getUserInput();
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
        setFileIcon();
    }

    private void setFileIcon() {
        if (inputs != null) {
            if (inputs.get(inputs.size() - 1) instanceof ObservableResource) {
                Resource tmp = ((ObservableResource) inputs.get(inputs.size() - 1)).getResource();

                switch (tmp.getType()) {
                    case PDF:
                        imageViewFile.setImage(new Image("/icons/highrespdf.png"));
                        break;
                    default:
                        imageViewFile.setImage(new Image("/icons/file_icon.png"));
                        break;
                }
            }
        } else {
            imageViewFile.setImage(new Image("/icons/cross.png"));
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

    protected abstract List<Boolean> createCheckBoxResults();

    protected abstract Question newQuestionFromFields();

    protected Tag getSelectedTag() {
        if (toggleEasy.isSelected()) {
            return Tag.EASY;
        } else if (toggleNormal.isSelected()) {
            return Tag.NORMAL;
        } else if (toggleHard.isSelected()) {
            return Tag.HARD;
        } else if (toggleImportant.isSelected()) {
            return Tag.IMPORTANT;
        }
        // return null if user did not select a tag
        return null;
    }

    /**
     * Returns a list of answers generated by the user input. List size can be [0,4] depending on
     * the modified text fields.
     *
     * @param checkBoxResults List containing the values of the available 4 checkboxes
     * @return list of answers
     */
    private List<Answer> newAnswersFromFields(List<Boolean> checkBoxResults) {
        List<Answer> answers = new LinkedList<>();
        QuestionType questionType = getQuestionType();
        if (!textFieldAnswerOne.getText().isEmpty()) {
            answers.add(
                new Answer(questionType, textFieldAnswerOne.getText(), checkBoxResults.get(0)));
        }
        if(textFieldAnswerTwo != null) {
            if (!textFieldAnswerTwo.getText().isEmpty()) {
                answers.add(
                    new Answer(questionType, textFieldAnswerTwo.getText(), checkBoxResults.get(1)));
            }
        }
        if(textFieldAnswerThree != null) {
            if (!textFieldAnswerThree.getText().isEmpty()) {
                answers.add(new Answer(questionType, textFieldAnswerThree.getText(), checkBoxResults.get(2)));
            }
        }

        if(textFieldAnswerFour != null) {
            if (!textFieldAnswerFour.getText().isEmpty()) {
                answers.add(new Answer(questionType, textFieldAnswerFour.getText(), checkBoxResults.get(3)));
            }
        }

        return answers;
    }




}
