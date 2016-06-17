package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.FxmlLoadException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.ConflictResolution;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.TopicConflict;
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
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class TopicConflictCell
    extends ListCell<TopicConflict> {

    private static final Logger logger = LogManager.getLogger();


    private static Map<TopicConflict, TopicConflictItemController> existingControllers =
        new HashMap<>();

    @Autowired SpringFXMLLoader springFXMLLoader;

    @Override public void updateItem(TopicConflict topic, boolean empty) {
        super.updateItem(topic, empty);
        if (topic != null) {
            boolean isDuplicate = topic.getResolution() == ConflictResolution.DUPLICATE;
            if(!isDuplicate){
                createOrLoadController(topic);
            }
        }
    }

    private void createOrLoadController(TopicConflict topic) {
        if (!existingControllers.containsKey(topic)) {
            TopicConflictItemController itemController = getController();
            setControllerProperties(topic, itemController);
            existingControllers.put(topic, itemController);
        } else {
            TopicConflictItemController itemController = existingControllers.get(topic);
            setGraphic(itemController.getRoot());
        }
    }

    private void setControllerProperties(TopicConflict topicConflict,
        TopicConflictItemController itemController) {
        itemController.setQuestionConflict(topicConflict);
        setGraphic(itemController.getRoot());
    }

    private TopicConflictItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, TopicConflictItemController> editSubjectWrapper;
        try {
            editSubjectWrapper = springFXMLLoader.loadAndWrap("/fxml/merge/topicConflictItem.fxml",
                TopicConflictItemController.class);
            return editSubjectWrapper.getController();
        } catch (IOException e) {
            logger.error(e);
            throw new FxmlLoadException("Error loading topic conflict item", e);
        }
    }

}