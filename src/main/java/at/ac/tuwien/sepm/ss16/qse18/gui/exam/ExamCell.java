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
 * Created by Felix on 01.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ExamCell
    extends ListCell<ObservableExerciseExam> {

    @Autowired SpringFXMLLoader springFXMLLoader;
    private static Logger logger = LogManager.getLogger(ExamCell.class);

    @Override public void updateItem(ObservableExerciseExam exam, boolean empty) {
        super.updateItem(exam, empty);
        if(exam != null) {
            ExamItemController examController = getController();
            setControllerProperties(exam, examController);
        }
    }

    private void setControllerProperties(ObservableExerciseExam exam, ExamItemController examController) {
        examController.setExam(exam);
        examController.loadFields();
        setGraphic(examController.getRoot());
    }

    private ExamItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, ExamItemController> examItemWrapper;
        try {
            examItemWrapper = springFXMLLoader
                .loadAndWrap("/fxml/exam/examItem.fxml", ExamItemController.class);
            return examItemWrapper.getController();
        } catch(IOException e) {
            logger.error(e);
            throw new RuntimeException("Critical Error during exam loading process", e);
        }
    }
}
