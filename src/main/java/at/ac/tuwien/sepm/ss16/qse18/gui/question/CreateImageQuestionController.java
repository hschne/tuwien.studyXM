package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
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

    @FXML public Button buttonAddImage;

    @FXML public ImageView imageViewQuestionImage;
    @FXML public ImageView imageViewIcon;

    @FXML public TextField textFieldImagePath;

    @FXML public CheckBox checkBoxAnswerOne;
    @FXML public CheckBox checkBoxAnswerTwo;
    @FXML public CheckBox checkBoxAnswerThree;
    @FXML public CheckBox checkBoxAnswerFour;

    private File out;

    @Autowired public CreateImageQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService, SpringFXMLLoader fxmlLoader) {
        super(questionService, resourceQuestionService, fxmlLoader);

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
        if (!validateImageSelected()) {
            logger.debug("User input is not valid, can't create question.");
            return true;
        }
        logger.debug("Creating new question from valid user input.");
        try {
            copySelectedImage();
            createQuestionAndAnswers();
        } catch (IOException e) {
            logger.error(e);
            showError("Unable to copy image. Please view logs for more details.");
            return true;
        } catch (ServiceException | DtoValidatorException e) {
            logger.error(e);
            showError(e);
            return true;
        }
        return false;
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
     * Checks if the user has provided enough information to create a valid question.
     *
     * @return true if the user input is valid, false else.
     */
    private boolean validateImageSelected() {
        logger.debug("Validating user input.");
        if (!checkIfImageWasSelected()) {
            logger.error("No image was selected.");
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
        inputs.addAll(createCheckBoxResults());
        inputs.add(checkBoxContinue.isSelected());
    }

    @Override protected QuestionType getQuestionType() {
        return QuestionType.NOTECARD;
    }

    /**
     * Returns a question generated by the user input
     *
     * @return generated question
     */
    @Override protected Question newQuestionFromFields() {
        logger.debug("Creating new question");
        return new Question(textFieldImagePath.getText(), getQuestionType(), 1);
    }

    @Override protected List<Boolean> createCheckBoxResults() {
        List<Boolean> result = new ArrayList<>();
        result.add(checkBoxAnswerOne.isSelected());
        result.add(checkBoxAnswerTwo.isSelected());
        result.add(checkBoxAnswerThree.isSelected());
        result.add(checkBoxAnswerFour.isSelected());
        return result;
    }


}


