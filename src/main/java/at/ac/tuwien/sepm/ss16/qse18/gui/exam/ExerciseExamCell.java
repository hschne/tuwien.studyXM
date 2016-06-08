package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExerciseExam;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.ListCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Felix on 07.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ExerciseExamCell
    extends ListCell<ObservableExerciseExam> {

    @Autowired SpringFXMLLoader springFXMLLoader;

    private static Logger logger = LogManager.getLogger(ExerciseExamCell.class);

    @Override public void updateItem(ObservableExerciseExam exam, boolean empty) {
        super.updateItem(exam, empty);
        if(exam != null) {
            ExerciseExamItemController exerciseExamItemController = getController();
            setControllerProperties(exam, exerciseExamItemController);
        }
    }

    private void setControllerProperties(ObservableExerciseExam exam,
        ExerciseExamItemController exerciseExamItemController) {
        logger.debug("Got controller property: exercise exam " + exam);
        exerciseExamItemController.setExam(exam);
        exerciseExamItemController.loadFields();
        setGraphic(exerciseExamItemController.getRoot());
    }

    private ExerciseExamItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, ExerciseExamItemController> exerciseExamItemWrapper;
        try {
            exerciseExamItemWrapper = springFXMLLoader
                .loadAndWrap("/fxml/exam/exerciseExamItem.fxml", ExerciseExamItemController.class);
            return exerciseExamItemWrapper.getController();
        } catch(IOException e) {
            logger.error(e);
            throw new RuntimeException("Critical error during exercise exam loading process", e);
        }
    }
}
