package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableQuestion;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.ListCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class QuestionCell
    extends ListCell<ObservableQuestion> {


    @Autowired SpringFXMLLoader springFXMLLoader;

    private Logger logger = LogManager.getLogger();

    @Override public void updateItem(ObservableQuestion question, boolean empty) {
        super.updateItem(question, empty);
        if (question != null) {
            QuestionItemController itemController = getController();
            setControllerProperties(question, itemController);
        }
    }

    private void setControllerProperties(ObservableQuestion question,
        QuestionItemController itemController) {
        itemController.setQuestion(question);
        itemController.loadFields();
        setGraphic(itemController.getRoot());
    }

    private QuestionItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, QuestionItemController> editSubjectWrapper;
        try {
            editSubjectWrapper = springFXMLLoader
                .loadAndWrap("/fxml/question/questionItem.fxml", QuestionItemController.class);
            return editSubjectWrapper.getController();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
