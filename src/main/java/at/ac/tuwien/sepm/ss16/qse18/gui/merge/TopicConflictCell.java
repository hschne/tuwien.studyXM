package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.FxmlLoadException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopicConflict;
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
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class TopicConflictCell
    extends ListCell<ObservableTopicConflict> {

    private static final Logger logger = LogManager.getLogger();
    @Autowired SpringFXMLLoader springFXMLLoader;

    @Override public void updateItem(ObservableTopicConflict topic, boolean empty) {
        super.updateItem(topic, empty);
        if (topic != null) {
            TopicConflictItemController itemController = getController();
            setControllerProperties(topic, itemController);
        }
    }

    private void setControllerProperties(ObservableTopicConflict topicConflict,
        TopicConflictItemController itemController) {
        itemController.initialize(topicConflict);
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
