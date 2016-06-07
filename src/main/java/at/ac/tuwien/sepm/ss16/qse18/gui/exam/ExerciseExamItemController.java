package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Felix on 07.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ExerciseExamItemController extends
    BaseController {
    @FXML public Node root;
    @Autowired ExerciseExamService exerciseExamService;
    private ObservableExerciseExam exam;

    @Autowired ExerciseExamItemController(ExerciseExamService exerciseExamService) {
        this.exerciseExamService = exerciseExamService;
    }

    public void loadFields() {

    }

    public void setExam(ObservableExerciseExam exam) {
        this.exam = exam;
    }

    public Node getRoot() {
        return this.root;
    }
}
