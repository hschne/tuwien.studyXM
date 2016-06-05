package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.QuestionServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    @Autowired SpringFXMLLoader springFXMLLoader;
    @Autowired SubjectServiceImpl subjectService;
    @Autowired ExamServiceImpl examService;
    @Autowired QuestionServiceImpl questionService;

    private static final Logger logger = LogManager.getLogger(DoExamController.class);
    private int starttime;
    private IntegerProperty time;
    private Exam exam;
    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer answer4;
    private int currentQuestionNumber = 0;
    private AnswerQuestionController controller;
    private IntegerProperty progress = new SimpleIntegerProperty(0);


    @FXML public void initialize(Exam exam){
        this.exam = exam;
        starttime = (int)exam.getExamTime();
        time = new SimpleIntegerProperty(starttime);
        timeLeftLabel.textProperty().bind(Bindings.concat(time.asString())
                                                    .concat(new SimpleStringProperty(" min left")));
        countDown();

        try {
            Subject subject = subjectService.getSubject(exam.getSubjectID());
            if(subject != null) {
                titleLabel.setText("Exam in " + subject.getName());
            }
            List<Integer> questionIds = examService.getAllQuestionsOfExam(exam.getExamid());
            progressLabel.textProperty().bind(new SimpleStringProperty("Progress ")
                .concat(progress.asString()).concat("/").concat(questionIds.size()));

            for (Integer i : questionIds) {
                    questions.add(questionService.getQuestion(i));
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
        try {
            examService.update(exam.getExamid(), questions.get(currentQuestionNumber).getQuestionId(),
                controller.isCorrect(), true);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            showError(e);
        }

        progress.setValue(progress.intValue() + 1);
        currentQuestionNumber++;
        if(currentQuestionNumber < questions.size()) {
            loadCorrectSubScreen(questions.get(currentQuestionNumber).getType());
        }
        else{
            nextQuestionButton.setVisible(false);
            skipQuesitonButton.setVisible(false);
            showResultsButton.setVisible(true);
            loadExamFinished();
        }
    }

    public void handleSkipQuestionButton(){
        questions.add(questions.remove(currentQuestionNumber));
        loadCorrectSubScreen(questions.get(currentQuestionNumber).getType());

    }

    public void handleShowResultsButton(){
        //TODO implement
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

    private void loadMultipleChoice(){
        try {
            controller =
                setSubView("/fxml/exam/answerChoiceQuestion.fxml",
                    AnswerChoiceQuestionController.class);
            setAnswers(currentQuestionNumber);
            controller.initialize(this.exam, questions.get(currentQuestionNumber),answer1,answer2,
                answer3,answer4);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            showError(e);
        }
    }

    private void loadExamFinished(){
        try{
            setSubView("/fxml/exam/examFinished.fxml",ExamFinishedController.class);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            showError(e);
        }
    }

    private void loadNoteCard(){
        try{
            controller = setSubView("/fxml/exam/answerImageQuestion.fxml",
                AnswerImageQuestionController.class);
            setAnswers(currentQuestionNumber);
            controller.initialize(this.exam, questions.get(currentQuestionNumber),answer1,answer2,
                answer3,answer4);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            showError(e);
        }
    }

    private void loadOpenQuestion(){
        try{
            controller = setSubView("/fxml/exam/answerOpenQuestion.fxml",
                AnswerOpenQuestionController.class);
            setAnswers(currentQuestionNumber);
            controller.initialize(this.exam, questions.get(currentQuestionNumber),answer1,answer2,
                answer3,answer4);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            showError(e);
        }
    }

    private void countDown(){
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.minutes(starttime+1),
                                                                            new KeyValue(time, 0)));
        timeline.playFromStart();
        timeline.setOnFinished(e -> {showInformation("Unfortunatly you are out of time :(");});
    }

    private <T extends GuiController> T setSubView(String fxmlPath, Class T) throws IOException {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> wrapper = springFXMLLoader.loadAndWrap(fxmlPath, T);
        this.subPane.getChildren().clear();
        Pane newPane = (Pane) wrapper.getLoadedObject();
        newPane.setPrefWidth(this.subPane.getWidth());
        newPane.setPrefHeight(this.subPane.getHeight());
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        this.subPane.getChildren().add(newPane);
        return wrapper.getController();
    }

    private void loadCorrectSubScreen(QuestionType type){
        if(type == QuestionType.MULTIPLECHOICE || type == QuestionType.SINGLECHOICE){
            loadMultipleChoice();
        }
        else if(type == QuestionType.NOTECARD){
            loadNoteCard();
        }
        else if(type == QuestionType.OPENQUESTION){
            loadOpenQuestion();
        }
    }

}
