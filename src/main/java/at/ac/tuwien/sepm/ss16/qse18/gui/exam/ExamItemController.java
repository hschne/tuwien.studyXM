package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
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

import java.util.List;

/**
 * Created by Felix on 01.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ExamItemController extends BaseController {
    @FXML public Node root;
    @Autowired ExerciseExamService exerciseExamService;
    @FXML private Label examIdentifier;
    @FXML private ProgressBar examProgress;
    @FXML private Button buttonStudy;
    private ObservableExam exam;

    @Autowired ExamItemController(ExerciseExamService exerciseExamService) {
        this.exerciseExamService = exerciseExamService;
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
        List<Integer> allQuestionList, answeredQuestions;
        double percentageAnswered = 0d;
        try {
            allQuestionList = exerciseExamService.getAllQuestionsOfExam(this.exam.getExamid());
            answeredQuestions =
                exerciseExamService.getAnsweredQuestionsOfExam(this.exam.getExamid());
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
            examProgress.setStyle("-fx-accent:#8bf22f;");
        } else if (percentageAnswered > 0.3d) {
            examProgress.setStyle("-fx-accent:yellow;");
        } else {
            examProgress.setStyle("-fx-accent:red;");
        }
    }

    public Node getRoot() {
        return this.root;
    }
}
