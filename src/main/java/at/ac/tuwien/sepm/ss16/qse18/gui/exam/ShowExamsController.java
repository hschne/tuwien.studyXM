package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Felix on 01.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class ShowExamsController
    extends BaseController {
    @FXML private ListView<ObservableExam> examListView;
    @FXML private Button buttonNewExam;
    @Autowired ApplicationContext applicationContext;
    private ObservableList<ObservableExam> exams;
    private ExamService examService;

    @Autowired public ShowExamsController(ExamService examService) {
        this.examService = examService;
    }

    @FXML public void initialize() {
        try {
            logger.debug("Initializing exam table");
            initializeListView();
        } catch(ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    @FXML public void newExam() {
        mainFrameController.handleNewExam();
    }

    private void initializeListView() throws ServiceException {
        List<ObservableExam> observableExams =
            examService.getExams().stream().map(ObservableExam::new).collect(Collectors.toList());
        exams = FXCollections.observableArrayList(observableExams);
        logger.debug(exams.size() + " exam list size");
        examListView.setItems(exams);
        examListView.setCellFactory(listView -> applicationContext.getBean(ExamCell.class));
    }
}
