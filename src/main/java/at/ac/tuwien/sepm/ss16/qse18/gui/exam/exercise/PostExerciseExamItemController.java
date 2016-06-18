package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;

import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableAnswer;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zhang Haixiang on 15.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PostExerciseExamItemController extends BaseController {
    @FXML private Node root;
    @FXML private ListView<String> answerListView;
    @FXML private Label questionLabel;
    @FXML private ImageView questionImageView;
    @FXML private Button showResourceButton;

    @Autowired private QuestionService questionService;
    @Autowired private ResourceQuestionService resourceQuestionService;

    private ObservableQuestion question;
    private ObservableList<String> answerList;
    private Resource resource;


    @FXML public void initialize(ObservableQuestion question) {
        try {
            this.question = question;

            this.resource = this.resourceQuestionService
                .getResourceFromQuestion(this.question.getQuestionInstance());

            List<ObservableAnswer> observableAnswers = questionService.
                getCorrespondingAnswers(question.getQuestionInstance()).
                stream().map(ObservableAnswer::new).collect(Collectors.toList());

            answerList = FXCollections.observableList(fillWithAnswerName(observableAnswers));
            answerListView.setItems(answerList);


        } catch (ServiceException e) {
            logger.error(
                "Service Exception in PostExerciseExamItemController initialize with parameters",
                question);
            showError(e);
        }
    }

    @FXML protected void onClick() {
        logger.debug("entering onClick()");
        if (this.question.getQuestionInstance().getType().equals(QuestionType.NOTECARD)) {
            showFile(this.question.getQuestion());
        }
    }

    @FXML protected void showResource() {
        logger.debug("entering showResource()");
        showFile(this.resource.getReference());
    }

    public void loadFields() {
        questionLabel.setText(question.getQuestion().replaceAll("(.{140})", "$1\n"));


        if (this.question.getQuestionInstance().getType().equals(QuestionType.NOTECARD)) {
            questionLabel.setVisible(false);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(question.getQuestion()));
            questionImageView
                .setImage(new Image(fileChooser.getInitialDirectory().toURI().toString()));

        }

        if (!this.question.getAnsweredCorrectly()) {
            questionLabel.setTextFill(Color.BISQUE);
        }

        if (this.resource == null) {
            showResourceButton.setVisible(false);
        }

    }

    protected Node getRoot() {
        return root;
    }

    private List<String> fillWithAnswerName(List<ObservableAnswer> observableAnswers) {
        List<String> answers = new ArrayList<>();
        for (ObservableAnswer a : observableAnswers) {
            answers.add((a.getAnswer() + "       [" + a.correctProperty().getValue() + "]")
                .replaceAll("(.{100})", "$1\n"));
        }
        return answers;
    }

    private void showFile(String path) {
        logger.debug("entering showFile()");
        String operatingSystem = System.getProperty("os.name");
        if(operatingSystem.contains("Windows") || operatingSystem.contains("Mac")) {

            if (Desktop.isDesktopSupported()) {
                try {
                    File file = new File(path);
                    Desktop.getDesktop().open(file);

                } catch (IOException e) {
                    logger.error("no standard program for file type selected");
                    showError("Unable to open file, " +
                        "please select a standard program for this file type." +
                        e.getMessage());
                }
            }
        }else {
            logger.error("unsupported operating system");
            showError("Show files is only available on windows and mac.");
        }
    }

}
