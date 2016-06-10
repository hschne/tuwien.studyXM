package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Felix on 07.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class ShowExerciseExamsController
    extends BaseController {

    private ObservableExam exam;
    @FXML ListView<ObservableExerciseExam> examListView;
    @Autowired ExamService examService;
    @Autowired ApplicationContext applicationContext;
    @Autowired ExerciseExamService exerciseExamService;
    ObservableList<ObservableExerciseExam> observableExerciseExams;

    public void setExam(ObservableExam exam) {
        this.exam = exam;
        initialize();
    }

    @FXML public void initialize() {
        if(this.exam == null) {
            return;
        }

        try {
            logger.debug("Initializing exercise exam table");
            initializeListView();
        } catch(ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    private void initializeListView() throws ServiceException {
        List<ObservableExerciseExam> examList =
            exerciseExamService.getExams().stream().map(ObservableExerciseExam::new).collect(
                Collectors.toList());

        List<ObservableExerciseExam> exerciseExamListOfExam = new ArrayList<>();

        int samplesize = examList.size();
        int counter = 0;
        for(ObservableExerciseExam e : examList) {
            if(e.getExamInstance().getExam() == this.exam.getExamid()) {
                exerciseExamListOfExam.add(e);
                counter++;
            }
        }
        logger.debug("Showing " + counter + "/" + samplesize + "entries of exercise exam");

        observableExerciseExams = FXCollections.observableArrayList(exerciseExamListOfExam);
        examListView.setItems(observableExerciseExams);
        examListView.setCellFactory(listView -> applicationContext.getBean(ExerciseExamCell.class));
    }
}
