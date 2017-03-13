package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.FxmlLoadException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.ConflictResolution;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.QuestionConflict;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.ListCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) class QuestionConflictCell
    extends ListCell<QuestionConflict> {

    private static final Logger logger = LogManager.getLogger();

    private static Map<QuestionConflict, QuestionConflictItemController> existingControllers =
        new HashMap<>();

    @Autowired SpringFXMLLoader springFXMLLoader;

    @Override public void updateItem(QuestionConflict question, boolean empty) {
        super.updateItem(question, empty);
        if (question != null) {
            boolean isDuplicate = question.getResolution() == ConflictResolution.DUPLICATE;
            if (!isDuplicate) {
                createOrLoadController(question);
            }
        }
    }

    private void createOrLoadController(QuestionConflict question) {
        if (!existingControllers.containsKey(question)) {
            QuestionConflictItemController itemController = getController();
            setControllerProperties(question, itemController);
            existingControllers.put(question, itemController);
        } else {
            QuestionConflictItemController itemController = existingControllers.get(question);
            setGraphic(itemController.getRoot());
        }
    }

    private void setControllerProperties(QuestionConflict questionConflict,
        QuestionConflictItemController itemController) {
        itemController.setQuestionConflict(questionConflict);
        setGraphic(itemController.getRoot());
    }

    private QuestionConflictItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, QuestionConflictItemController> editSubjectWrapper;
        try {
            editSubjectWrapper = springFXMLLoader
                .loadAndWrap("/fxml/merge/questionConflictItem.fxml",
                    QuestionConflictItemController.class);
            return editSubjectWrapper.getController();
        } catch (IOException e) {
            logger.error(e);
            throw new FxmlLoadException("Error loading question conflict item", e);
        }
    }
}
