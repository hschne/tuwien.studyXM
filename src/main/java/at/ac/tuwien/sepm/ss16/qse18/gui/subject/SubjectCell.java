package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
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
 * @author Hans-Joerg Schroedl,Philipp Ganiu
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class SubjectCell
    extends ListCell<ObservableSubject> {

    @Autowired SpringFXMLLoader springFXMLLoader;

    private static final Logger logger = LogManager.getLogger();

    @Override public void updateItem(ObservableSubject subject, boolean empty) {
        super.updateItem(subject, empty);
        if (subject != null) {
            SubjectItemController itemController = getController();
            setControllerProperties(subject, itemController);
        }
    }

    private void setControllerProperties(ObservableSubject subject,
        SubjectItemController itemController) {
        itemController.initialize(subject);
        itemController.setAddTopicButtonAction(subject,itemController.getTopicList());
        itemController.loadFields();
        setGraphic(itemController.getRoot());
    }

    private SubjectItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, SubjectItemController> editSubjectWrapper;
        try {
            editSubjectWrapper = springFXMLLoader
                .loadAndWrap("/fxml/subject/subjectItem.fxml", SubjectItemController.class);
            return editSubjectWrapper.getController();
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("Error loading subject item", e);
        }
    }
}
