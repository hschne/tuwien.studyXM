package at.ac.tuwien.sepm.ss16.qse18.gui;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise.DoExamController;
import at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.exam.ShowQuestionsController;
import at.ac.tuwien.sepm.ss16.qse18.gui.exam.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.Navigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.question.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceOverviewController;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectOverviewController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class MainFrameController extends
    Navigation {

    @FXML public Pane paneContent;
    @FXML private Button buttonHome;
    @FXML private Button buttonSubjects;
    @FXML private Button buttonResources;



    @Autowired
    public MainFrameController(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }

    @FXML public void handleHome() {
        logger.debug("Loading home view");
        try {
            setSubView("/fxml/exam/examOverview.fxml", ShowExamsController.class,paneContent);
        } catch (IOException e) {
            handleException(e);
        }
    }

    @FXML public void handleSubjects() {
        logger.debug("Loading subject view");
        try {
            setSubView("/fxml/subject/subjectOverview.fxml", SubjectOverviewController.class,paneContent);
        } catch (IOException e) {
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
                    ResourceOverviewController.class,paneContent);
            resourceOverviewController.setInput(inputs, resourceLabel, questionType);
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
            setSubView("/fxml/question/questionOverview.fxml", QuestionOverviewController.class,paneContent);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleShowExerciseExams(ObservableExam exam) {
        try {
            ShowExerciseExamsController controller = setSubView("/fxml/exam/exerciseExamOverview.fxml",
                ShowExerciseExamsController.class,paneContent);
            controller.setExam(exam);

        } catch(Exception e) {
            handleException(e);
        }
    }


    public void handleExams() {
        logger.debug("Loading exam view");
        try {
            setSubView("/fxml/exam/examOverview.fxml", ShowExamsController.class,paneContent);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void handleStartExam(ExerciseExam exam){
        logger.debug("Loading doExam screen");
        try{
            DoExamController controller = setSubView("/fxml/exam/doExam.fxml", DoExamController.class,paneContent);
            controller.initialize(exam);
        }
        catch (IOException e){
            handleException(e);
        }
    }

    public void handleShowExamResult(){
        logger.debug("Loading doExam screen");
        try{
            setSubView("/fxml/exam/showResult.fxml", ShowResultController.class, paneContent);
        }
        catch (IOException e){
            handleException(e);
        }
    }

    public void handleShowQuestions() {
        logger.debug("Loading ShowQuestions screen");
        try {
            setSubView("/fxml/exam/showQuestions.fxml", ShowQuestionsController.class,paneContent);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void handleStudy(ObservableExam exam) {
        logger.debug("Loading study screen");
        try {
            StudyNowController controller = setSubView("/fxml/exam/studyNowOrExportExam.fxml", StudyNowController.class,paneContent);
            controller.setExam(exam);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void handleCreateExerciseExam(ObservableExam exam) {
        logger.debug("Loading create exam screen");
        try {
            NewExerciseExamController controller = setSubView("/fxml/exam/newExerciseExam.fxml", NewExerciseExamController.class, paneContent);
            controller.setExam(exam);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleCreateExerciseExamPrint(ObservableExam exam) {
        logger.debug("Loading create exam screen");
        try {
            NewExerciseExamPrintController controller = setSubView("/fxml/exam/newExerciseExamPrint.fxml", NewExerciseExamPrintController.class, paneContent);
            controller.setExam(exam);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public <T extends GuiController> T handleMultipleChoice(AnchorPane subPane){
        T controller = null;
        try {
            controller =
                setSubView("/fxml/exam/answerChoiceQuestion.fxml",
                    AnswerChoiceQuestionController.class,subPane);

        }
        catch (IOException e){
            logger.error(e.getMessage());
            handleException(e);
        }
        return controller;
    }


    public  <T extends GuiController> T handleNoteCard(AnchorPane subPane){
        T controller = null;
        try{
            controller = setSubView("/fxml/exam/answerImageQuestion.fxml",
                AnswerImageQuestionController.class,subPane);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            handleException(e);
        }
        return controller;
    }

    public  <T extends GuiController> T handleOpenQuestion(AnchorPane subPane){
        T controller = null;
        try{
            controller = setSubView("/fxml/exam/answerOpenQuestion.fxml",
                AnswerOpenQuestionController.class,subPane);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            handleException(e);
        }
        return controller;
    }

    public Pane getPaneContent() {
        return paneContent;
    }

    public Button getButtonHome() {
        return buttonHome;
    }

    public Button getButtonSubjects() {
        return buttonSubjects;
    }

    public Button getButtonResources() {
        return buttonResources;
    }

    private <T extends GuiController> T setSubView(String fxmlPath, Class T, Pane paneContent) throws IOException {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper = fxmlLoader.loadAndWrap(fxmlPath, T);
        T controller = mfWrapper.getController();
        configureSubPane(mfWrapper,paneContent);
        return controller;
    }

    private <T extends GuiController> void configureSubPane(
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper,  Pane paneContent) {
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



}
