package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import javafx.fxml.FXML;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller vor the "Study now"-View
 *
 * Created by Felix on 01.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class StudyNowController
    extends BaseController {


    private ObservableExam exam;

    /**
     * Displays a create new exercise exam view
     */
    @FXML public void newExam() {
        mainFrameController.handleCreateExerciseExam(exam);
    }


    @FXML public void resumeExam() {
        mainFrameController.handleShowExerciseExams(exam);
    }

    @FXML public void exportAsPDF() {
        mainFrameController.handleCreateExerciseExamPrint(exam);
    }



    public void setExam(ObservableExam exam) {
        this.exam = exam;
    }
}
