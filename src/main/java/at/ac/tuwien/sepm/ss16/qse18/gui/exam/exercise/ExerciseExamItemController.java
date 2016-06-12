package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
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
    @Autowired MainFrameController mainFrameController;

    @Autowired ExerciseExamItemController(ExerciseExamService exerciseExamService) {
        this.exerciseExamService = exerciseExamService;
    }

    public void loadFields() {
        List<Integer> allQuestionList = new ArrayList<>();
        List<Integer> answeredQuestions = new ArrayList<>();
        examIdentifier.setText("Exercise exam by " + this.exam.getAuthor());
        try {
            allQuestionList = exerciseExamService.getAllQuestionsOfExam(exam.getExamid());
            answeredQuestions = exerciseExamService.getAnsweredQuestionsOfExam(exam.getExamid());
        } catch(ServiceException e) {
            logger.error(e);
            showError(e);
        }

        boolean allAnswered = allQuestionList.size() == answeredQuestions.size();

        //TODO put correctly answered question count here instead of if all questions were answered
        labelPassed.setText(allAnswered ? "Done" : "Incomplete");
        labelQuestionCounter.setText(answeredQuestions.size() + "/" + allQuestionList.size()
            + " Questions answered");

        buttonStartExam.setDisable(allAnswered);

        if(!answeredQuestions.isEmpty() && !allAnswered) {
            buttonStartExam.setText("resume exam");
        }

        //TODO: restart exercise exam
        buttonRestart.setDisable(true);

    }

    @FXML public void handleStartExam() {
        logger.debug("Button Start exam clicked");
        mainFrameController.handleStartExam(this.exam.getExamInstance());
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
