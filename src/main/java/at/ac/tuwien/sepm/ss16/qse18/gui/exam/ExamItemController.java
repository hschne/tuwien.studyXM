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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 01.06.2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExamItemController extends BaseController {
    @FXML
    public Node root;
    @Autowired
    ExerciseExamService exerciseExamService;
    @Autowired
    ExamService examService;
    @FXML
    private Label examIdentifier;
    @FXML
    private ProgressBar examProgress;
    @FXML
    private Button buttonStudy;
    @FXML
    private Label labelProgress;
    @FXML
    private ImageView imageViewProgress;
    private ObservableExam exam;

    @Autowired
    ExamItemController(ExerciseExamService exerciseExamService, ExamService examService) {
        this.exerciseExamService = exerciseExamService;
        this.examService = examService;
    }

    @FXML
    public void handleStudy() {
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
            for (Integer i : exerciseExamList) {
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

        checkProgress(percentageAnswered);
    }

    /**
     * This method calculates the time difference in days between the current date and the
     * exam due date. When the progress is critical a hint will be displayed in the GUI.
     * @param progress amount of answered questions
     */
    private void checkProgress(double progress){
        logger.debug("calculating time difference ");
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(format.format(new Date()));
            d2 = format.parse(exam.getDueDate());

            long difference = d2.getTime() - d1.getTime();
            long differenceHours = difference / (60 * 60 * 1000) % 24;
            long differenceDays = difference / (24 * 60 * 60 * 1000);

            if (progress < 0.5d && differenceDays < 7L && differenceDays >= 0) {
                imageViewProgress.setImage(new Image("/icons/badProgress.png"));
                labelProgress.setText("your progress is critical, " +
                        differenceDays + (differenceDays != 1 ? " days " : " day ") +
                        differenceHours + (differenceHours!=1?" hours ":"hour ")+
                        "till the exam.");
            }
            else if(differenceDays < 0){
                imageViewProgress.setImage(new Image("/icons/overProgress.png"));
                labelProgress.setText("the due date for this exam is already surpassed.");
            }

        } catch (ParseException e) {
            logger.error("Unable to calculate time difference", e);
        }
    }


    public Node getRoot() {
        return this.root;
    }
}
