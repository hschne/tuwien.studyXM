package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.QuestionServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    @FXML private Button finishExamButton;

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
    @Autowired private AlertBuilder alertBuilder;
    private int currentQuestionNumber = 0;
    private AnswerMultipleChoiceQuestionController mcController;
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
        if (questions.get(currentQuestionNumber).getType() == QuestionType.MULTIPLECHOICE) {
                try {
                    examService.update(exam.getExamid(), questions.get(currentQuestionNumber).getQuestionId(),
                        mcController.isCorrect(), true);
                } catch (ServiceException e) {
                    logger.error(e.getMessage());
                    showError(e);
                }
            }
        progress.setValue(progress.intValue() + 1);
        currentQuestionNumber++;
        if(currentQuestionNumber < questions.size()) {
            loadCorrectSubScreen(questions.get(currentQuestionNumber).getType());
        }
        else{
            nextQuestionButton.setVisible(false);
            finishExamButton.setVisible(true);
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
        }
        else if(answers.size() == 3){
            answer2 = answers.get(1);
            answer3 = answers.get(2);
        }
        else{
            answer2 = answers.get(1);
            answer3 = answers.get(2);
            answer4 = answers.get(3);
        }
    }

    private void loadMultipleChoice(){
        try {
            mcController =
                setSubView("/fxml/exam/answerMultipleChoiceQuestion.fxml",
                    AnswerMultipleChoiceQuestionController.class);
            setAnswers(currentQuestionNumber);
            mcController.initialize(this.exam, questions.get(currentQuestionNumber),answer1,answer2,
                answer3,answer4);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            showError(e);
        }
    }

    private void loadNoteCard(){
        //TODO implement this class
    }

    private void countDown(){
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(starttime+1),
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
    }

}
