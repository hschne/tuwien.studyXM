package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 07.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ExerciseExamItemController extends
    BaseController {
    @FXML public Node root;
    @FXML Label examIdentifier;
    @FXML Label labelPassed;
    @FXML Label labelQuestionCounter;
    @FXML Button buttonRestart;
    @FXML Button buttonStartExam;
    @Autowired ExerciseExamService exerciseExamService;
    private ObservableExerciseExam exam;

    @Autowired ExerciseExamItemController(ExerciseExamService exerciseExamService) {
        this.exerciseExamService = exerciseExamService;
    }

    public void loadFields() {
        List<Integer> allQuestionList, answeredQuestions;
        examIdentifier.setText("Exercise exam by " + this.exam.getAuthor());
        try {
            allQuestionList = exerciseExamService.getAllQuestionsOfExam(exam.getExamid());
            answeredQuestions = exerciseExamService.getAnsweredQuestionsOfExam(exam.getExamid());
        } catch(ServiceException e) {
            logger.error("Could not fetch questions of exercise exam " + exam);
            return;
        }

        boolean allAnswered = (allQuestionList.size() == answeredQuestions.size());

        //TODO put correctly answered question count here instead of if all questions were answered
        labelPassed.setText(allAnswered ? "Done" : "Incomplete");
        labelQuestionCounter.setText(answeredQuestions.size() + "/" + allQuestionList.size()
            + " Questions answered");

        buttonStartExam.setDisable(allAnswered);

        if(answeredQuestions.size() > 0 && !allAnswered) {
            buttonStartExam.setText("resume exam");
        }

        //TODO: restart exercise exam
        buttonRestart.setDisable(true);

    }

    @FXML public void handleStartExam() {
        //TODO: integrate exam application
    }

    @FXML public void restartExam() {
        //TODO
    }

    public void setExam(ObservableExerciseExam exam) {
        this.exam = exam;
    }

    public Node getRoot() {
        return this.root;
    }
}
