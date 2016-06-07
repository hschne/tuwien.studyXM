package at.ac.tuwien.sepm.ss16.qse18.gui;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.exam.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.question.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceEditController;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceOverviewController;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectEditController;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectOverviewController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * The root controller, which loads subviews into the center pane
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class MainFrameController {

    private static final Logger logger = LogManager.getLogger();
    @FXML public Pane paneContent;
    private SpringFXMLLoader fxmlLoader;
    private AlertBuilder alertBuilder;

    @FXML public void handleHome() {
        logger.debug("Loading home view");
        try {
            setSubView("/fxml/exam/examOverview.fxml", ShowExamsController.class);
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

    public void handleCreateSubject(ObservableSubject subject) {
        logger.debug("Loading create subject view");
        try {
            SubjectEditController controller =
                setSubView("/fxml/subject/subjectEditView.fxml", SubjectEditController.class);
            controller.setSubject(subject);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void handleNewExam() {
        logger.debug("Loading new exam view");
        try {
            setSubView("/fxml/exam/newExam.fxml", NewExamController.class);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML public void handleResources() {
        logger.debug("Loading resource view");
        handleChooseResource(null, null, null);
    }

    public void handleChooseResource(List inputs, Label resourceLabel, QuestionType questionType) {
        logger.debug("Loading resource view with input");
        try {
            ResourceOverviewController resourceOverviewController =
                setSubView("/fxml/resource/resourceOverview.fxml",
                    ResourceOverviewController.class);
            resourceOverviewController.setInput(inputs, resourceLabel, questionType);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void handleCreateResource(List inputs, QuestionType questionType) {
        logger.debug("Loading create resource with input list");
        try {
            ResourceEditController resourceEditController =
                setSubView("/fxml/resource/resourceEditView.fxml", ResourceEditController.class);
            resourceEditController.setInput(inputs, questionType);
        } catch (IOException e) {
            handleException(e);
        }
    }

    @FXML public void handleStatistics() {
        //TODO: Display view here
    }

    public void handleQuestionOverview(ObservableSubject subject) {
        logger.debug("Loading question overview for " + subject.getName());
        try {
            setSubView("/fxml/question/questionOverview.fxml", QuestionOverviewController.class);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleMultipleChoiceQuestion(ObservableTopic topic) {
        handleMultipleChoiceQuestion(topic, null);
    }

    /**
     * Load Multiple Choice Question screen with saved input
     *
     * @param topic  The topic for which the multiplechoice question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleMultipleChoiceQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Multiple Choice question screen");
        try {
            CreateMultipleChoiceQuestionController multipleChoiceQuestionController =
                setSubView("/fxml/question/createMultipleChoiceQuestion.fxml",
                    CreateMultipleChoiceQuestionController.class);

            if (topic != null) {
                multipleChoiceQuestionController.setTopic(topic);
            }

            multipleChoiceQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleSingleChoiceQuestion(ObservableTopic topic) {
        handleSingleChoiceQuestion(topic, null);
    }

    /**
     * Load Single Choice Question screen with saved input
     *
     * @param topic  The topic for which the singlechoice question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleSingleChoiceQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Single Choice question screen ");
        try {
            CreateSingleChoiceQuestionController singleChoiceQuestionController =
                setSubView("/fxml/question/createSingleChoiceQuestion.fxml",
                    CreateSingleChoiceQuestionController.class);

            if (topic != null) {
                singleChoiceQuestionController.setTopic(topic);
            }

            singleChoiceQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleOpenQuestion(ObservableTopic topic) {
        handleOpenQuestion(topic, null);
    }

    /**
     * Load Open Question screen with saved input
     *
     * @param topic  The topic for which the open question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleOpenQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Open question screen ");
        try {
            CreateOpenQuestionController openQuestionController =
                setSubView("/fxml/question/createOpenQuestion.fxml",
                    CreateMultipleChoiceQuestionController.class);

            if (topic != null) {
                openQuestionController.setTopic(topic);
            }

            openQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleImageQuestion(ObservableTopic topic) {
        handleImageQuestion(topic, null);
    }

    /**
     * Load Image Question screen with saved input
     *
     * @param topic  The topic for which the image question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleImageQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Image question screen ");
        try {
            CreateImageQuestionController imageQuestionController =
                setSubView("/fxml/question/createImageQuestion.fxml",
                    CreateImageQuestionController.class);
            imageQuestionController.setTopic(topic);
            imageQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleShowExerciseExams(ObservableExam exam) {
        try {
            ShowExerciseExamsController controller = setSubView("/fxml/exam/showExerciseExams.fxml",
                ShowExerciseExamsController.class);
            controller.setExam(exam);
        } catch(Exception e) {
            handleException(e);
        }
    }

    public void handleCreateExam(ObservableExam exam) {
        logger.debug("Loading create exam screen");
        try {
            NewExerciseExamController controller = setSubView("/fxml/exam/newExerciseExam.fxml", NewExerciseExamController.class);
            controller.setExam(exam);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleCreateQuestion(ObservableTopic topic) {
        logger.debug("Loading create question screen");
        try {
            WhichQuestionController whichQuestionController =
                setSubView("/fxml/question/whichQuestion.fxml", WhichQuestionController.class);
            whichQuestionController.setTopic(topic);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleExams() {
        logger.debug("Loading exam view");
        try {
            setSubView("/fxml/exam/examOverview.fxml", ShowExamsController.class);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void handleShowQuestions() {
        logger.debug("Loading ShowQuestions screen");
        try {
            setSubView("/fxml/exam/showQuestions.fxml", ShowQuestionsController.class);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void handleStudy(ObservableExam exam) {
        logger.debug("Loading study screen");
        try {
            StudyNowController controller = setSubView("/fxml/exam/studyNowOrExportExam.fxml", StudyNowController.class);
            controller.setExam(exam);
        } catch (IOException e) {
            handleException(e);
        }
    }

    private <T extends GuiController> T setSubView(String fxmlPath, Class T) throws IOException {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper = fxmlLoader.loadAndWrap(fxmlPath, T);
        T controller = mfWrapper.getController();
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

    private void handleException(Exception e) {
        logger.error("Exception thrown", e);
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("Could not load sub view.")
            .contentText("Unexpected Error. Please view logs for details.").build();
        alert.showAndWait();
    }

    @Autowired private void setSpringFXMLLoader(SpringFXMLLoader loader) {
        this.fxmlLoader = loader;
    }

    @Autowired private void setAlertBuilder(AlertBuilder alertBuilder) {
        this.alertBuilder = alertBuilder;
    }



}
