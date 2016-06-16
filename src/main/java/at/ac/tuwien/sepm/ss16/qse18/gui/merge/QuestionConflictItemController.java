package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestionConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.ConflictResolution;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuestionConflictItemController extends BaseController {

    @Autowired QuestionService questionService;
    private ObservableQuestionConflict questionConflict;

    @FXML private Node root;

    @FXML private Pane existingPane;

    @FXML private Pane importedPane;

    @FXML private Label questionText;

    @FXML private Label existingAnswerOne;
    @FXML private Label existingAnswerTwo;
    @FXML private Label existingAnswerThree;
    @FXML private Label existingAnswerFour;
    @FXML private CheckBox existingCorrectOne;
    @FXML private CheckBox existingCorrectTwo;
    @FXML private CheckBox existingCorrectThree;
    @FXML private CheckBox existingCorrectFour;
    @FXML private Label importedAnswerOne;
    @FXML private Label importedAnswerTwo;
    @FXML private Label importedAnswerThree;
    @FXML private Label importedAnswerFour;
    @FXML private CheckBox importedCorrectOne;
    @FXML private CheckBox importedCorrectTwo;
    @FXML private CheckBox importedCorrectThree;
    @FXML private CheckBox importedCorrectFour;

    public void setQuestionConflict(ObservableQuestionConflict questionConflict) {
        this.questionConflict = questionConflict;
        try {
            initializeFields();
        } catch (ServiceException e) {
            logger.error(e);
        }
    }

    @FXML public void handleSelectExisting() {
        questionConflict.getConflict().setResolution(ConflictResolution.EXISTING);
        existingPane.getStyleClass().add("selected-question-pane");
        importedPane.getStyleClass().clear();
    }

    @FXML public void handleSelectImported() {
        questionConflict.getConflict().setResolution(ConflictResolution.IMPORTED);
        existingPane.getStyleClass().clear();
        importedPane.getStyleClass().add("selected-question-pane");
    }

    public Node getRoot() {
        return root;
    }

    private void initializeFields() throws ServiceException {
        Question existingQuestion = questionConflict.getConflict().getExistingQuestion();
        ExportQuestion importedQuestion = questionConflict.getConflict().getImportedQuestion();
        questionText.setText(existingQuestion.getQuestion());
        setFieldsForExisting(existingQuestion);
        setFieldsForImported(importedQuestion);
    }

    private void setFieldsForExisting(Question getExistingQuestion) throws ServiceException {
        List<Label> existingLabels =
            createLabels(existingAnswerOne, existingAnswerTwo, existingAnswerThree,
                existingAnswerFour);
        List<CheckBox> checkboxes =
            createCheckboxes(existingCorrectOne, existingCorrectTwo, existingCorrectThree,
                existingCorrectFour);
        List<Answer> existingAnswers = questionService.getCorrespondingAnswers(getExistingQuestion);
        setCheckboxesAndLabels(existingLabels, checkboxes, existingAnswers);
    }


    private void setFieldsForImported(ExportQuestion importedQuestion) {
        List<Label> importedLabels =
            createLabels(importedAnswerOne, importedAnswerTwo, importedAnswerThree,
                importedAnswerFour);
        List<CheckBox> checkboxes =
            createCheckboxes(importedCorrectOne, importedCorrectTwo, importedCorrectThree,
                importedCorrectFour);
        List<Answer> existingAnswers = importedQuestion.getAnswers();
        setCheckboxesAndLabels(importedLabels, checkboxes, existingAnswers);
    }

    private List<Label> createLabels(Label labelOne, Label labelTwo, Label labelThree,
        Label labelFour) {
        List<Label> importedLabels = new ArrayList<>();
        importedLabels.add(labelOne);
        importedLabels.add(labelTwo);
        importedLabels.add(labelThree);
        importedLabels.add(labelFour);
        return importedLabels;
    }


    private List<CheckBox> createCheckboxes(CheckBox checkBoxOne, CheckBox checkBoxTwo,
        CheckBox checkBoxThree, CheckBox checkBoxFour) {
        List<CheckBox> checkboxes = new ArrayList<>();
        checkboxes.add(checkBoxOne);
        checkboxes.add(checkBoxTwo);
        checkboxes.add(checkBoxThree);
        checkboxes.add(checkBoxFour);
        return checkboxes;
    }


    private void setCheckboxesAndLabels(List<Label> labels, List<CheckBox> checkboxes,
        List<Answer> existingAnswers) {
        for (int i = 0; i < 4; i++) {
            Label currentLabel = labels.get(i);
            CheckBox checkBox = checkboxes.get(i);
            checkBox.setDisable(true);
            checkBox.setStyle("-fx-opacity: 1");
            if (i >= existingAnswers.size()) {
                currentLabel.setVisible(false);
                checkBox.setVisible(false);
                continue;
            }
            Answer answer = existingAnswers.get(i);
            labels.get(i).setText(answer.getAnswer());
            checkBox.setSelected(answer.isCorrect());

        }
    }


}
