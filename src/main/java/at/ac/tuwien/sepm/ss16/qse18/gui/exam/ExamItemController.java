package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Felix on 01.06.2016.
 */
@Component public class ExamItemController extends BaseController {
    @FXML public Node root;
    @FXML private Label examIdentifier;
    @FXML private ProgressBar examProgress;
    @FXML private Button buttonStudy;
    private ObservableExam exam;
    @Autowired ExamService examService;

    @Autowired ExamItemController(ExamService examService) {
        this.examService = examService;
    }

    @FXML public void handleStudy() {

    }

    public void setExam(ObservableExam exam) {
        this.exam = exam;
    }

    public void loadFields() {
        examIdentifier.setText(this.exam.getAuthor() + " " + this.exam.getCreated("dd-MM-YYYY"));
        List<Integer> allQuestionList, answeredQuestions;
        double percentageAnswered = 0d;
        try {
            allQuestionList = examService.getAllQuestionsOfExam(
                this.exam.getExamid());
            answeredQuestions = examService.getAnsweredQuestionsOfExam(
                this.exam.getExamid());
        } catch(ServiceException e) {
            logger.error("Could not fetch questions of exam", e);
            return;
        }

        if(!allQuestionList.isEmpty()) {
            percentageAnswered = (double)answeredQuestions.size() / (double)allQuestionList.size();
            examProgress.setProgress(percentageAnswered);
        }

        logger.debug("Already answered: " + answeredQuestions.size() + "/" +
            allQuestionList.size() + " " + percentageAnswered);

        if(percentageAnswered > 0.8d) {
            examProgress.setStyle("-fx-accent:#8bf22f;");
        } else if(percentageAnswered > 0.3d) {
            examProgress.setStyle("-fx-accent:yellow;");
        } else {
            examProgress.setStyle("-fx-accent:red;");
        }


    }

    public Node getRoot() {
        return this.root;
    }
}
