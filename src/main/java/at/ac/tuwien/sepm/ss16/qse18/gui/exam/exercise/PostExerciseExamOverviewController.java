package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author  Zhang Haixiang on 15.06.2016.
 */
@Component public class PostExerciseExamOverviewController extends BaseController {
    @Autowired ApplicationContext applicationContext;
    private Map<Integer, Boolean> questionBooleans = new HashMap<>();
    @Autowired ExerciseExamService exerciseExamService;

    @FXML protected ListView<ObservableQuestion> questionListView;
    @FXML protected Button finishButton;
    private ObservableList<ObservableQuestion> questionList;


    @FXML public void initialize(ExerciseExam exerciseExam){
        try{
        List<ObservableQuestion> observableQuestions =
            exerciseExam.getExamQuestions().stream().map(ObservableQuestion::new)
                .collect(Collectors.toList());


            this.questionBooleans = exerciseExamService
                .getQuestionBooleansOfExam(exerciseExam.getExamid(), generateQuestionIDList(observableQuestions));


            setRightValues(observableQuestions);
            questionList = FXCollections.observableArrayList(observableQuestions);
            questionListView.setItems(questionList);
            questionListView.setCellFactory(listView -> applicationContext.getBean(ExerciseExamQuestionsCell.class));


        }catch (ServiceException e){
            logger.error("ServiceException in initialize with parameters {}", exerciseExam, e);
            showError(e.getMessage());
        }
    }

    public void finish(){
        logger.debug("entering finish()");
        mainFrameController.handleHome();
    }

    private List<Integer> generateQuestionIDList(List<ObservableQuestion> observableQuestions){
        List<Integer> questionIDs = new ArrayList<>();

        for(ObservableQuestion o: observableQuestions){
            questionIDs.add(o.getQuestionInstance().getQuestionId());
        }

        return questionIDs;
    }

    private void setRightValues(List<ObservableQuestion> observableQuestions){
        for(ObservableQuestion o: observableQuestions){
            if(questionBooleans.containsKey(o.getQuestionInstance().getQuestionId())){
                o.setAnsweredCorrectly(questionBooleans.get(o.getQuestionInstance().getQuestionId()));
            }
        }
    }

}
