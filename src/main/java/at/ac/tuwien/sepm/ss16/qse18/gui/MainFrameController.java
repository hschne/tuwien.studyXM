package at.ac.tuwien.sepm.ss16.qse18.gui;

import at.ac.tuwien.sepm.ss16.qse18.gui.exam.CreateExamController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.question.CreateImageQuestionController;
import at.ac.tuwien.sepm.ss16.qse18.gui.question.CreateMultipleChoiceQuestionController;
import at.ac.tuwien.sepm.ss16.qse18.gui.question.QuestionOverviewController;
import at.ac.tuwien.sepm.ss16.qse18.gui.question.WhichQuestionController;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectOverviewController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The root controller, which loads subviews into the center pane
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class MainFrameController {

    private final Logger logger = LogManager.getLogger();
    @FXML public Pane paneContent;
    private SpringFXMLLoader fxmlLoader;
    private AlertBuilder alertBuilder;
    private Stage primaryStage;

    @Autowired void setSpringFXMLLoader(SpringFXMLLoader loader) {
        this.fxmlLoader = loader;
    }

    @Autowired void setAlertBuilder(AlertBuilder alertBuilder) {
        this.alertBuilder = alertBuilder;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private <T extends GuiController> T setSubView(String fxmlPath, Class T) throws IOException {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper = fxmlLoader.loadAndWrap(fxmlPath, T);
        T controller = mfWrapper.getController();
        controller.setPrimaryStage(primaryStage);
        configureSubPane(mfWrapper);
        return controller;
    }

    private <T extends GuiController> void configureSubPane(
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper) {
        paneContent.getChildren().clear();
        Pane pane = (Pane) mfWrapper.getLoadedObject();
        pane.setPrefWidth(paneContent.getWidth());
        pane.setPrefHeight(paneContent.getHeight());
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        paneContent.getChildren().add(pane);
    }

    @FXML public void handleHome() {
        logger.debug("Loading home view");
        try {
            setSubView("/fxml/exam/createExam.fxml", CreateExamController.class);
        } catch (IOException e) {
            handleException(e);
        }
    }

    @FXML public void handleSubjects() {
        logger.debug("Loading subject view");
        try {
            setSubView("/fxml/subject/subjectOverview.fxml", SubjectOverviewController.class);
        } catch (IOException e) {
            handleException(e);
        }
    }

    @FXML public void handleResources() {
        //TODO: Display view here
    }

    @FXML public void handleStatistics() {
        //TODO: Display view here
    }

    public void handleQuestionOverview(ObservableSubject subject) {
        logger.debug("Loading question overview for " + subject.getName());
        try {
            QuestionOverviewController controller =
                setSubView("/fxml/question/questionOverview.fxml",
                    QuestionOverviewController.class);
            controller.setSubject(subject);
        } catch (Exception e) {
            handleException(e);
        }
    }


    public void handleMultipleChoiceQuestion() {
        logger.debug("Loading Multiple Choice question screen ");
        try {
            setSubView("/fxml/question/createMultipleChoiceQuestion.fxml", CreateMultipleChoiceQuestionController.class);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleSingleChoiceQuestion() {
        logger.debug("Loading Single Choice question screen ");
        try {
            //TODO: create fxml and controller
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleOpenQuestion() {
        logger.debug("Loading Open question screen ");
        try {
            //TODO: create fxml and controller
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleCreateImageQuestion() {
        logger.debug("Loading Image question screen ");
        try {
            setSubView("/fxml/question/createImageQuestion.fxml", CreateImageQuestionController.class);
        } catch (Exception e) {
            handleException(e);
        }
    }





    private void handleException(Exception e) {
        logger.error(e);
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("Could not load sub view.")
            .contentText("Unexpected Error. Please view logs for details.").build();
        alert.showAndWait();
    }

}
