package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.AnswerService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A controller for the creation of image-questions.
 *
 * @author Julian on 14.05.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CreateImageQuestionController extends QuestionController {

    private static final String W = "Warning";

    @FXML public Button buttonAddImage;

    @FXML public ImageView imageViewQuestionImage;
    @FXML public ImageView imageViewIcon;

    @FXML public TextField textFieldImagePath;

    @FXML public CheckBox checkBoxAnswerOne;
    @FXML public CheckBox checkBoxAnswerTwo;
    @FXML public CheckBox checkBoxAnswerThree;
    @FXML public CheckBox checkBoxAnswerFour;

    private AnswerService answerService;
    private File out;

    @Autowired public CreateImageQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService, AnswerService answerService, SpringFXMLLoader fxmlLoader) {
        super(questionService, resourceQuestionService, fxmlLoader);
        this.answerService = answerService;

    }

    @Override protected void fillFieldsAndCheckboxes() {
        this.imageViewQuestionImage.setImage(
            inputs == null ? new Image("/images/imagePlaceholder.png") : (Image) inputs.get(0));
        this.textFieldImagePath.setText(inputs == null ? "" : (String) inputs.get(1));

        fillAnswerFields(2);

        this.checkBoxAnswerOne.setSelected(inputs != null && (boolean) inputs.get(6));
        this.checkBoxAnswerTwo.setSelected(inputs != null && (boolean) inputs.get(7));
        this.checkBoxAnswerThree.setSelected(inputs != null && (boolean) inputs.get(8));
        this.checkBoxAnswerFour.setSelected(inputs != null && (boolean) inputs.get(9));

        this.checkBoxContinue.setSelected(inputs == null || (boolean) inputs.get(10));

        this.resource = (inputs == null ? null : (ObservableResource) inputs.get(11));
        this.resourceLabel.setText(resource == null ? "none" : resource.getName());
    }

    @Override protected void saveQuestionInput(List inputs) {
        inputs.add(imageViewQuestionImage.getImage());

        if (textFieldImagePath != null) {
            inputs.add(textFieldImagePath.getText());
        } else {
            inputs.add(null);
        }
    }

    @Override protected void saveCheckboxesAndRadiobuttons(List inputs) {
        inputs.add(checkBoxAnswerOne.isSelected());
        inputs.add(checkBoxAnswerTwo.isSelected());
        inputs.add(checkBoxAnswerThree.isSelected());
        inputs.add(checkBoxAnswerFour.isSelected());

        inputs.add(checkBoxContinue.isSelected());
    }

    @Override protected QuestionType getQuestionType() {
        return QuestionType.NOTECARD;
    }

    /**
     * Creates a new image question from the users input and stores it in the database.
     */
    @FXML public void handleCreateQuestion() {
        if (createQuestion()) {
            return;
        }
        if (checkBoxContinue.isSelected()) {
            mainFrameController.handleImageQuestion(this.topic);
        } else {
            mainFrameController.handleSubjects();
        }
        showSuccess("Question is now in the database");
    }

    /**
     * Handles the event of the "add image" button.
     * Opens a dialogue to give the user the opportunity to choose an image file.
     * When an image ist selected the image is loaded into the imageViewQuestionImage
     * The path is also displayed in the textFieldImagePath
     */
    @FXML public void handleAddImage() {
        logger.debug("Opening file chooser dialogue");

        FileChooser fileChooser = new FileChooser();
        String defaultPath = "src/main/resources/images/";
        File defaultDirectory = new File(defaultPath);
        fileChooser.setInitialDirectory(defaultDirectory);

        fileChooser.setTitle("Add image");
        fileChooser.getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            logger.debug("A File was selected");
            Image img = new Image(selectedFile.toURI().toString());
            imageViewQuestionImage.setImage(img);
            out = new File(defaultPath + generateFileName(selectedFile.getName()));
            textFieldImagePath.setText(defaultPath + selectedFile.getName());
        }
    }

    private boolean createQuestion() {
        if (!validateInput()) {
            logger.debug("User input is not valid, can't create question.");
            return true;
        }
        logger.debug("Creating new question from valid user input.");
        try {
            copySelectedImage();

            Question newQuestion = newQuestionFromFields();
            Question createdQuestion = questionService.createQuestion(newQuestion, topic.getT());

            if (resource != null) {
                resourceQuestionService
                    .createReference(resource.getResource(), createdQuestion);
            }

            List<Answer> answerList = newAnswersFromFields();
            for (Answer a : answerList) {
                a.setQuestion(newQuestion);
                answerService.createAnswer(a);
            }
            questionService.setCorrespondingAnswers(newQuestion, answerList);

        } catch (IOException e) {
            logger.error(e);
            showError("Unable to copy image. Please view logs for more details.");
            return true;
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
            return true;
        }
        return false;
    }

    /**
     * Returns a question generated by the user input
     *
     * @return generated question
     */
    private Question newQuestionFromFields() {
        return new Question(textFieldImagePath.getText(), QuestionType.NOTECARD, 1);
    }

    /**
     * Returns a list of answers generated by the user input.
     * List size can be [0,4] depending on the modified text fields.
     *
     * @return list of answers
     */
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

    /**
     * Checks if the user has selected an image.
     *
     * @return true if an image was selected, false else.
     */
    private boolean checkIfImageWasSelected() {
        logger.debug("Checking if image was selected.");
        return !textFieldImagePath.getText().isEmpty();
    }

    /**
     * Checks if the user has provided an answer to this question.
     *
     * @return true if answer was given, false else.
     */
    private boolean checkIfAnswerWasGiven() {
        logger.debug("Checking if answer was given.");
        return !(textFieldAnswerOne.getText().isEmpty() &&
            textFieldAnswerTwo.getText().isEmpty() &&
            textFieldAnswerThree.getText().isEmpty() &&
            textFieldAnswerFour.getText().isEmpty());
    }

    /**
     * Checks if one of thee given answers is selected as correct.
     *
     * @return true if at least one answer is selected as correct, false else.
     */
    private boolean checkIfCorrectAnswerWasGiven() {
        logger.debug("Checking if correct answer was given.");
        return checkBoxAnswerOne.isSelected() || checkBoxAnswerTwo.isSelected() ||
            checkBoxAnswerThree.isSelected() || checkBoxAnswerFour.isSelected();
    }

    /**
     * Checks if the user has provided text to a checkbox selected as correct.
     *
     * @return true if all checkboxes which are selected as correct have text
     * in their text fields. False else.
     */
    private boolean checkIfCorrectAnswerHasText() {
        logger.debug("Checking if correct answers have text.");
        if (checkBoxAnswerOne.isSelected() && textFieldAnswerOne.getText().isEmpty()) {
            return false;
        }
        if (checkBoxAnswerTwo.isSelected() && textFieldAnswerTwo.getText().isEmpty()) {
            return false;
        }
        if (checkBoxAnswerThree.isSelected() && textFieldAnswerThree.getText().isEmpty()) {
            return false;
        }
        if (checkBoxAnswerFour.isSelected() && textFieldAnswerFour.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the user has provided enough information to create a valid question.
     *
     * @return true if the user input is valid, false else.
     */
    private boolean validateInput() {
        logger.debug("Validating user input.");

        if (!checkIfImageWasSelected()) {
            logger.error("No image was selected.");
            showError("Please select an image for this question.");
            return false;
        }

        if (!checkIfAnswerWasGiven()) {
            logger.error("No answer was given.");
            showError("Please give at least one answer to this question.");
            return false;
        }

        if (!checkIfCorrectAnswerWasGiven()) {
            logger.error("No correct answer was given.");
            showError("Please give at least one correct answer to this question.");
            return false;
        }

        if (!checkIfCorrectAnswerHasText()) {
            logger.error("The answer selected as correct has no text.");
            showError("Please select an image for this question.");
            return false;
        }
        return true;
    }

    /**
     * Stores a copy of the selected image in src/main/resources/images/ when
     * a new question is created.
     */
    private void copySelectedImage() throws IOException {
        logger.debug("tying to copy image to src/main/resources/images/");
        ImageIO
            .write(SwingFXUtils.fromFXImage(imageViewQuestionImage.getImage(), null), "png", out);
    }

    /**
     * Generates a new image file name if the passed name is already present in the image directory.
     *
     * @param fileName name which should be checked for duplicates
     * @return new file name when a duplicate was found, fileName else.
     */
    private String generateFileName(String fileName) {
        logger.debug("Checking for duplicate image files");
        File dir = new File("src/main/resources/images/");
        String[] files = dir.list();
        Set<String> fileSet = new HashSet<>();
        fileSet.addAll(Arrays.asList(files));

        if (fileSet.contains(fileName)) {
            logger.debug("Duplicate found, generating new file name");
            return UUID.randomUUID().toString() + ".png";
        } else {
            logger.debug("No duplicate found");
            return fileName;
        }
    }


}


