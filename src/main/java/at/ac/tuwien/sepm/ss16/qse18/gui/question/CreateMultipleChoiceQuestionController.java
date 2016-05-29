package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceChooserController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for managing creation of multiple choice questions
 * <p>
 * Created by Julian on 15.05.2016.
 */
@Component public class CreateMultipleChoiceQuestionController extends QuestionController {

    private Logger logger = LogManager.getLogger(CreateMultipleChoiceQuestionController.class);

    @FXML private TextArea textAreaQuestion;
    @FXML private TextField textfieldAnswerOne;
    @FXML private TextField textfieldAnswerTwo;
    @FXML private TextField textfieldAnswerThree;
    @FXML private TextField textfieldAnswerFour;
    @FXML private CheckBox checkBoxAnswerOne;
    @FXML private CheckBox checkBoxAnswerTwo;
    @FXML private CheckBox checkBoxAnswerThree;
    @FXML private CheckBox checkBoxAnswerFour;
    @FXML private CheckBox checkBoxContinue;

    @Autowired private SpringFXMLLoader fxmlLoader;

    @Autowired public CreateMultipleChoiceQuestionController(QuestionService questionService,
        AlertBuilder alertBuilder) {
        super(questionService, alertBuilder);
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

    @FXML public void handleAddResource() {
        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, ResourceChooserController> resourceChooserWrapper =
            null;
        try {
            resourceChooserWrapper = fxmlLoader.loadAndWrap("/fxml/resource/resourceChooser.fxml",
                ResourceChooserController.class);
        } catch (IOException e) {
            logger.error("Could not load resourceChooser.fxml", e);
        }
        ResourceChooserController childController = null;
        if (resourceChooserWrapper != null) {
            childController = resourceChooserWrapper.getController();
            childController.setStage(stage);
            stage.setTitle("Add resource");
            stage.setScene(new Scene((Parent) resourceChooserWrapper.getLoadedObject(), 500, 400));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        }

    }

    private boolean createQuestion() {
        logger.info("Now creating new question");
        Question newQuestion;
        try {
            List<Answer> answers = newAnswersFromField();
            newQuestion = questionService.createQuestion(newQuestionFromField(), topic.getT());
            questionService.setCorrespondingAnswers(newQuestion, answers);
        } catch (ServiceException e) {
            showAlert(e);
            return true;
        }
        return false;
    }

    private Question newQuestionFromField() throws ServiceException {
        logger.info("Collecting question from field.");
        if (textAreaQuestion.getText().isEmpty()) {
            throw new ServiceException("The question must not be empty.");
        }

        return new Question(textAreaQuestion.getText(), QuestionType.MULTIPLECHOICE, 1L);
    }



    private List<Answer> newAnswersFromField() throws ServiceException {
        logger.debug("Collecting all answers");
        List<Answer> newAnswers = new LinkedList<>();
        if (!textfieldAnswerOne.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerOne.getText(),
                checkBoxAnswerOne.isSelected()));
        }
        if (!textfieldAnswerTwo.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerTwo.getText(),
                checkBoxAnswerTwo.isSelected()));
        }
        if (!textfieldAnswerThree.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerThree.getText(),
                checkBoxAnswerThree.isSelected()));
        }
        if (!textfieldAnswerFour.getText().isEmpty()) {
            newAnswers.add(new Answer(QuestionType.MULTIPLECHOICE, textfieldAnswerFour.getText(),
                checkBoxAnswerFour.isSelected()));
        }

        if (newAnswers.isEmpty()) {
            throw new ServiceException("At least one answer must be given.");
        }

        for (Answer a : newAnswers) {
            if (a.isCorrect()) {
                return newAnswers;
            }
        }

        throw new ServiceException("At least one answer must be true.");
    }
}
