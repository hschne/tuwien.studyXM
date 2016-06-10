package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectEditController;
import at.ac.tuwien.sepm.ss16.qse18.gui.topic.TopicEditController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SubjectNavigation extends SubviewNavigation {

    @Autowired
    public SubjectNavigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }


    public void handleCreateSubject(ObservableSubject subject) {
        logger.debug("Loading create subject view");
        try {
            SubjectEditController controller =
                setSubView("/fxml/subject/subjectEditView.fxml", SubjectEditController.class);
            controller.setSubject(subject);
        } catch (IOException e) {
            handleException(e);
        }
    }


    public void handleCreateTopic(ObservableSubject subject, ObservableList<ObservableTopic> topicList) {
        logger.debug("Loading create topic view");
        try {
            TopicEditController controller =
                setSubView("/fxml/topic/topicEditView.fxml", TopicEditController.class);
            controller.setSubject(subject.getSubject());
            controller.setTopicList(topicList);
        } catch (IOException e) {
            handleException(e);
        }
    }

}
