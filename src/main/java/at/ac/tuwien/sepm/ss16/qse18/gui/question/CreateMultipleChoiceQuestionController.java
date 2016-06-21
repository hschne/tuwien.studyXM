package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.LatexRenderService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controller for managing creation of multiple choice questions <p> Created by Julian on
 * 15.05.2016.
 */
@Component public class CreateMultipleChoiceQuestionController extends QuestionController {
    @FXML private TextArea textAreaQuestion;
    @FXML private CheckBox checkBoxAnswerOne;
    @FXML private CheckBox checkBoxAnswerTwo;
    @FXML private CheckBox checkBoxAnswerThree;
    @FXML private CheckBox checkBoxAnswerFour;
    @FXML private ChoiceBox<String> choiceBoxQuestionTime;
    @FXML private ToggleGroup tagToggleGroupMultiple;

    @FXML private ToggleButton texSyntaxButton;

    @Autowired private LatexRenderService latexRenderService;

    @Autowired public CreateMultipleChoiceQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        super(questionService, resourceQuestionService);
    }

    @FXML public void handleCreateQuestion() {
        if (createQuestion()) {
            return;
        }
        if (checkBoxContinue.isSelected()) {
            questionNavigation.handleMultipleChoiceQuestion(this.topic);
        } else {
            mainFrameController.handleSubjects();
        }
        showSuccess("Inserted new question into database.");
    }

    @FXML public void toggleSelected() {
        setColorOfOtherButtonsToGrey(tagToggleGroupMultiple);
    }

    private boolean createQuestion() {
        logger.info("Now creating new question");
        try {
            if (texSyntaxButton.isSelected()) {
                createLatexQuestion();
            } else {
                createQuestionAndAnswers();
            }
        } catch (ServiceException | DtoValidatorException e) {
            showError(e);
            return true;
        }
        return false;
    }

    private void createLatexQuestion() throws ServiceException, DtoValidatorException {
        logger.debug("Collecting question from field.");
        BufferedImage image = latexRenderService.createImageFrom(textAreaQuestion.getText());
        String filePath = trySaveImage(image);
        Question question = new Question(filePath, QuestionType.NOTECARD,
            Integer.parseInt(choiceBoxQuestionTime.getValue().substring(0, 1)), getSelectedTag());
        createQuestionAndAnswers(question);
    }

    private String trySaveImage(BufferedImage image) throws ServiceException {
        String filePath = generateFileName();
        File outputfile = new File(filePath);
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Could not save file.", e);
        }
        return filePath;
    }

    private String generateFileName() {
        logger.debug("Checking for duplicate image files");
        String dir = "src/main/resources/images/";
        logger.debug("Duplicate found, generating new file name");
        return dir + UUID.randomUUID().toString() + ".png";

    }

    @Override protected void initializeToggleGroup() {
        tagToggleGroupMultiple.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });
        setColorOfOtherButtonsToGrey(tagToggleGroupMultiple);
    }

    @Override protected void fillFieldsAndCheckboxes() {
        this.textAreaQuestion.setText(inputs == null ? "" : (String) inputs.get(0));

        fillAnswerFields(1);

        this.checkBoxAnswerOne.setSelected(inputs != null && (boolean) inputs.get(5));
        this.checkBoxAnswerTwo.setSelected(inputs != null && (boolean) inputs.get(6));
        this.checkBoxAnswerThree.setSelected(inputs != null && (boolean) inputs.get(7));
        this.checkBoxAnswerFour.setSelected(inputs != null && (boolean) inputs.get(8));

        this.checkBoxContinue.setSelected(inputs != null && (boolean) inputs.get(9));

        if (inputs != null) {
            this.choiceBoxQuestionTime.setValue(inputs.get(10).toString());
        }

        this.resource = inputs == null ? null : (ObservableResource) inputs.get(11);
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

    @Override protected void saveChoiceBoxQuestionTime(List inputs) {
        inputs.add(choiceBoxQuestionTime.getValue());
    }

    @Override protected QuestionType getQuestionType() {
        return QuestionType.MULTIPLECHOICE;
    }

    @Override protected List<Boolean> createCheckBoxResults() {
        List<Boolean> result = new ArrayList<>();
        result.add(checkBoxAnswerOne.isSelected());
        result.add(checkBoxAnswerTwo.isSelected());
        result.add(checkBoxAnswerThree.isSelected());
        result.add(checkBoxAnswerFour.isSelected());
        return result;
    }

    @Override protected Question newQuestionFromFields() {
        logger.debug("Collecting question from field.");
        return new Question(textAreaQuestion.getText(), getQuestionType(),
            Integer.parseInt(choiceBoxQuestionTime.getValue().substring(0, 1)), getSelectedTag());
    }
}
