package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 01.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ExamItemController extends BaseController {
    @FXML public Node root;
    @Autowired ExerciseExamService exerciseExamService;
    @Autowired ExamService examService;
    @FXML private Label examIdentifier;
    @FXML private ProgressBar examProgress;
    @FXML private Button buttonStudy;
    private ObservableExam exam;

    @Autowired ExamItemController(ExerciseExamService exerciseExamService, ExamService examService) {
        this.exerciseExamService = exerciseExamService;
        this.examService = examService;
    }

    @FXML public void handleStudy() {
        mainFrameController.handleStudy(exam);
    }

    public void setExam(ObservableExam exam) {
        this.exam = exam;
    }

    public void loadFields() {
        logger.debug("Now showing exam: " + exam.getExamInstance());
        examIdentifier.setText(this.exam.getName() + " " + this.exam.getDueDate("dd-MM-YYYY"));
        List<Integer> allQuestionList = new ArrayList<>();
        List<Integer> answeredQuestions = new ArrayList<>();
        List<Integer> exerciseExamList;
        double percentageAnswered = 0d;
        try {
            exerciseExamList = examService.getAllExerciseExamsOfExam(exam.getExamInstance());
            logger.debug("Got list of exercise exams of size " + exerciseExamList.size());
            for(Integer i : exerciseExamList) {
                allQuestionList.addAll(exerciseExamService.getAllQuestionsOfExam(i));
                answeredQuestions.addAll(exerciseExamService.getAnsweredQuestionsOfExam(i));
            }
            logger.debug("List of all relevant questions is of size " + allQuestionList.size());
            logger.debug("List of all answered questions is of size " + answeredQuestions.size());
        } catch (ServiceException e) {
            logger.error("Could not fetch questions of exam", e);
            return;
        }

        if (!allQuestionList.isEmpty()) {
            percentageAnswered =
                (double) answeredQuestions.size() / (double) allQuestionList.size();
            examProgress.setProgress(percentageAnswered);
        }

        logger.debug("Already answered: " + answeredQuestions.size() + "/" +
            allQuestionList.size() + " " + percentageAnswered);

        if (percentageAnswered > 0.8d) {
            examProgress.setStyle("-fx-accent:#A2E88B;");
        } else if (percentageAnswered > 0.3d) {
            examProgress.setStyle("-fx-accent:#F0F595;");
        } else {
            examProgress.setStyle("-fx-accent:#F7A099;");
        }
    }

    public Node getRoot() {
        return this.root;
    }
}
