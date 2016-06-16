package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.gui.FxmlLoadException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
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
 * @author  Zhang Haixiang on 15.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class ExerciseExamQuestionsCell
    extends ListCell<ObservableQuestion> {
    @Autowired SpringFXMLLoader springFXMLLoader;
    private static final Logger logger = LogManager.getLogger();

    @Override public void updateItem(ObservableQuestion oq, boolean empty) {
        super.updateItem(oq, empty);
        if (oq != null) {
            PostExerciseExamItemController itemController = getController();
            setControllerProperties(oq, itemController);
        }
    }

    private void setControllerProperties(ObservableQuestion oq,
        PostExerciseExamItemController itemController) {
        itemController.initialize(oq);
        if(oq.getAnsweredCorrectly() == true) {
            itemController.getRoot().setStyle("-fx-background-color: seagreen");
        }else {
            itemController.getRoot().setStyle("-fx-background-color: firebrick");
        }
        itemController.loadFields();
        setGraphic(itemController.getRoot());
    }

    private PostExerciseExamItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, PostExerciseExamItemController> editQuestionWrapper;
        try {
            editQuestionWrapper = springFXMLLoader
                .loadAndWrap("/fxml/exam/postExerciseExamItem.fxml", PostExerciseExamItemController.class);
            return editQuestionWrapper.getController();
        } catch (IOException e) {
            logger.error(e);
            throw new FxmlLoadException("Error loading question item", e);
        }
    }


}
