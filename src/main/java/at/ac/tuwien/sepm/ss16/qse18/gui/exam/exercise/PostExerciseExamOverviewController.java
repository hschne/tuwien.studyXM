package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  Zhang Haixiang on 15.06.2016.
 */
@Component public class PostExerciseExamOverviewController extends BaseController {
    @Autowired ApplicationContext applicationContext;

    @FXML protected ListView<ObservableQuestion> questionListView;
    @FXML protected Button finishButton;
    private ObservableList<ObservableQuestion> questionList;


    @FXML public void initialize(ExerciseExam exerciseExam){
        List<ObservableQuestion> observableQuestions =
            exerciseExam.getExamQuestions().stream().map(ObservableQuestion::new)
                .collect(Collectors.toList());
        questionList = FXCollections.observableArrayList(observableQuestions);
        questionListView.setItems(questionList);
        questionListView.setCellFactory(listView -> applicationContext.getBean(ExerciseExamQuestionsCell.class));
    }

    public void finish(){
        logger.debug("entering finish()");
        mainFrameController.handleHome();
    }


}
