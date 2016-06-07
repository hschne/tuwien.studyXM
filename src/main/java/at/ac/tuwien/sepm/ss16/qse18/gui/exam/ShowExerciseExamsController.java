package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExerciseExam;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Felix on 07.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class ShowExerciseExamsController
    extends BaseController {

    private ObservableExam exam;
    @FXML ListView<ObservableExerciseExam> examListView;

    public void setExam(ObservableExam exam) {
        this.exam = exam;
    }
}
