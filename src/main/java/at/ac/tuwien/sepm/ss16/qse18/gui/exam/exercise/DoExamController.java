package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.DoExerciseExamNavigation;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExerciseExamServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.QuestionServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Philipp Ganiu
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class DoExamController extends
    BaseController {
    @FXML private Label timeLeftLabel;
    @FXML private Label titleLabel;
    @FXML private Label progressLabel;
    @FXML private AnchorPane subPane;
    @FXML private Timeline timeline;
    @FXML private Button nextQuestionButton;
    @FXML private Button skipQuesitonButton;
    @FXML private Button showResultsButton;
    @FXML private Button pauseExamButton;

    @Autowired SubjectServiceImpl subjectService;
    @Autowired ExerciseExamServiceImpl examService;
    @Autowired QuestionServiceImpl questionService;
    @Autowired DoExerciseExamNavigation doExerciseExamNavigation;
    @Autowired ShowResultController showResultController;

    private int timeInMinutes;
    private int timeInSeconds;
    private ExerciseExam exam;
    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer answer4;
    private int currentQuestionNumber = 0;
    private AnswerQuestionController controller;
    private IntegerProperty progress;


    @FXML public void initialize(ExerciseExam exam){
        mainFrameController.getButtonHome().setDisable(true);
        mainFrameController.getButtonSubjects().setDisable(true);
        mainFrameController.getButtonResources().setDisable(true);
        this.exam = exam;
        timeInMinutes = (int) exam.getExamTime();
        timeInSeconds = 0;
        timeLeftLabel.setText(timeInMinutes + ":0" + timeInSeconds + " min left");
        doExerciseExamNavigation.setPane(subPane);
        countDown();

        try {
            setProgressLabel();
            setTitleLabel();
            List<Integer> questionIds = examService.getNotAnsweredQuestionsOfExam(exam.getExamid());

            for (Integer i : questionIds) {
                    questions.add(questionService.getQuestion(i));
            }
            if(questions.size() == 1){
                skipQuesitonButton.setVisible(false);
                nextQuestionButton.setVisible(false);
                showResultsButton.setVisible(true);
            }
        }
        catch (ServiceException e){
            logger.error("An error occured",e);
            showError(e);
        }
        loadCorrectSubScreen(questions.get(currentQuestionNumber).getType());
    }

    public void handleNextQuestionButton(){
        if(controller.noButtonSelected()){
            showInformation("If you don't want to answer the question right away click on skip question");
            return;
        }
        update();
        progress.setValue(progress.intValue() + 1);
        currentQuestionNumber++;
        if(currentQuestionNumber == questions.size() - 1){
            skipQuesitonButton.setVisible(false);
            nextQuestionButton.setVisible(false);
            showResultsButton.setVisible(true);
        }
        if(currentQuestionNumber < questions.size()) {
            loadCorrectSubScreen(questions.get(currentQuestionNumber).getType());
        }
        else{
            timeline.stop();
        }
    }

    public void handleSkipQuestionButton(){
        questions.add(questions.remove(currentQuestionNumber));
        loadCorrectSubScreen(questions.get(currentQuestionNumber).getType());

    }

    public void handleShowResultsButton(){
        if(controller.noButtonSelected()){
            showInformation("You have not selected and answer.");
            return;
        }
        update();
        mainFrameController.getButtonHome().setDisable(false);
        mainFrameController.getButtonSubjects().setDisable(false);
        mainFrameController.getButtonResources().setDisable(false);
        timeline.stop();
        doExerciseExamNavigation.handleShowExamResult();
        showResultController.initialize(this.exam);
    }

    public void handlePauseExamButton(){
        if(showConfirmation("Do you want to pause the exam? You can resume it later.")){
            mainFrameController.handleHome();
        }
        else {
            return;
        }
        timeline.stop();
        mainFrameController.getButtonHome().setDisable(false);
        mainFrameController.getButtonSubjects().setDisable(false);
        mainFrameController.getButtonResources().setDisable(false);
        try {
            examService.update(exam.getExamid(), timeInSeconds < 30 ? timeInMinutes : timeInMinutes + 1);
        }
        catch (ServiceException e){
            logger.error(e);
            showError(e.getMessage());
        }


    }

    private void setAnswers(int currentQuestionNumber){
        try {
            answers = questionService.getCorrespondingAnswers(questions.get(currentQuestionNumber));
        }
        catch (ServiceException e){
            logger.error("An error occured",e);
            showError(e);
        }

        answer1 = answers.get(0);
        if(answers.size() == 2){
            answer2 = answers.get(1);
            answer3 = null;
            answer4 = null;
        }
        else if(answers.size() == 3){
            answer2 = answers.get(1);
            answer3 = answers.get(2);
            answer4 = null;
        }
        else if(answers.size() == 4){
            answer2 = answers.get(1);
            answer3 = answers.get(2);
            answer4 = answers.get(3);
        }
    }

    private void countDown(){
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e ->{
            if(timeInMinutes <= 0 && timeInSeconds <= 0){
                timeline.stop();
                showInformation("Unfortunatly you are out of time!");
                return;
            }
            if(timeInSeconds == 0){
                timeInMinutes--;
                timeInSeconds = 59;
                timeLeftLabel.setText(timeInMinutes + ":" + timeInSeconds + " min left");
            }
            else {
                timeInSeconds--;
                if(timeInSeconds < 10){
                timeLeftLabel.setText(timeInMinutes + ":0" + timeInSeconds + "min left");
                }
                else {
                    timeLeftLabel.setText(timeInMinutes + ":" + timeInSeconds + " min left");
                }
            }

        }));
        timeline.playFromStart();

    }

    private void loadCorrectSubScreen(QuestionType type){
        controller = doExerciseExamNavigation.loadQuestionView(type);
        setAnswers(currentQuestionNumber);
        controller.initialize(this.exam, questions.get(currentQuestionNumber),answer1,answer2,
            answer3,answer4);
    }

    private void update(){
        try {
            examService.update(exam.getExamid(), questions.get(currentQuestionNumber).getQuestionId(),
                controller.isCorrect(), true);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            showError(e);
        }

    }

    private void setProgressLabel() throws ServiceException{
        int numberOfAnsweredQuestions = examService.getAnsweredQuestionsOfExam(exam.getExamid()).size();
        progress = new SimpleIntegerProperty(numberOfAnsweredQuestions); //set progress

        int totalNumberOfQuestions = examService.getAllQuestionsOfExam(exam.getExamid()).size();
        progressLabel.textProperty().bind(new SimpleStringProperty("Progress ")
            .concat(progress.asString()).concat("/").concat(totalNumberOfQuestions)); //set progressLabel
    }

    private void setTitleLabel() throws ServiceException{
        Subject subject = subjectService.getSubject(exam.getSubjectID());
        if (subject != null) {
            titleLabel.setText("Exam in " + subject.getName()); //set titelLabel with subject to exam
        }
    }


}
