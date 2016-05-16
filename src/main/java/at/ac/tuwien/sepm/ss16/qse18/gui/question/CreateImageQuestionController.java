package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * A controller for the creation of image-questions.
 *
 * @author Julian on 14.05.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CreateImageQuestionController implements GuiController {

    @FXML public Button buttonCreateQuestion;
    @FXML public Button buttonAddImage;

    @FXML public ImageView imageViewQuestionImage;
    @FXML public ImageView imageViewIcon;

    @FXML public TextField textFieldAnswerOne;
    @FXML public TextField textFieldAnswerTwo;
    @FXML public TextField textFieldAnswerThree;
    @FXML public TextField textFieldAnswerFour;
    @FXML public TextField textFieldImagePath;

    @FXML public CheckBox checkBoxAnswerOne;
    @FXML public CheckBox checkBoxAnswerTwo;
    @FXML public CheckBox checkBoxAnswerThree;
    @FXML public CheckBox checkBoxAnswerFour;

    private Logger logger = LoggerFactory.getLogger(CreateImageQuestionController.class);
    private AlertBuilder alertBuilder;

    private QuestionService questionService;

    @Autowired public CreateImageQuestionController(QuestionService questionService,
        AlertBuilder alertBuilder) {
        this.questionService = questionService;
        this.alertBuilder = alertBuilder;
    }

    @FXML public void handleCreateQuestion() throws IOException {
        logger.debug("Creating new Question");
        try {
            Question q = newQuestionFromFields();
            questionService.createQuestion(q);
            questionService.setCorrespondingAnswers(q, newAnswersFromFields());
        } catch (ServiceException e) {
            showAlert(e);
        }
    }

    private Question newQuestionFromFields() {
        String imagePath = textFieldImagePath.getText();
        return new Question(imagePath, QuestionType.NOTECARD, 0);
    }

    private List<Answer> newAnswersFromFields() {
        List<Answer> answers = new LinkedList<>();

        if (!textFieldAnswerOne.getText().isEmpty()) {
            answers.add(new Answer(QuestionType.NOTECARD, textFieldAnswerOne.getText(),
                checkBoxAnswerOne.isSelected()));
        }
        if (!textFieldAnswerTwo.getText().isEmpty()) {
            answers.add(new Answer(QuestionType.NOTECARD, textFieldAnswerTwo.getText(),
                checkBoxAnswerTwo.isSelected()));
        }
        if (!textFieldAnswerThree.getText().isEmpty()) {
            answers.add(new Answer(QuestionType.NOTECARD, textFieldAnswerThree.getText(),
                checkBoxAnswerThree.isSelected()));
        }
        if (!textFieldAnswerFour.getText().isEmpty()) {
            answers.add(new Answer(QuestionType.NOTECARD, textFieldAnswerFour.getText(),
                checkBoxAnswerFour.isSelected()));
        }
        return answers;
    }


    @FXML public void handleAddImage() throws IOException {
        logger.debug("Adding new image.");
        selectImage();
    }

    /**
     * loads a new image into the imageViewQuestionImage and displays the path in the textFieldImagePath.
     *
     * @throws IOException
     */
    public void selectImage() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image img = new Image(selectedFile.toURI().toString());
            imageViewQuestionImage.setImage(img);
            textFieldImagePath.setText(selectedFile.toURI().toString());
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        //Not yet needed!
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occured").contentText(e.getMessage()).build();
        alert.showAndWait();
    }
}


