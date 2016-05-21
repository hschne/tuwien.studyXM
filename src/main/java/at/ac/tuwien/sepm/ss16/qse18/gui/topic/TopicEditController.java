package at.ac.tuwien.sepm.ss16.qse18.gui.topic;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectItemController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Philipp Ganiu
 */
@Component  @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class TopicEditController {
    @FXML public TextField topic;

    private ObservableList<ObservableTopic> topicList;
    private Subject subject;
    private Stage stage;
    private SubjectItemController overviewController;

    @Autowired public TopicEditController(SubjectItemController controller) {
        this.overviewController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleOk(){
        Topic newTopic = new Topic(-1,topic.getText());
        overviewController.addTopic(new ObservableTopic(newTopic),subject,topicList);
        stage.close();
    }

    public void setSubject(Subject subject){
        this.subject = subject;
    }


    public void setTopicList(ObservableList<ObservableTopic> topicList){
        this.topicList = topicList;
    }

    @FXML void handleCancel(){
        stage.close();
    }

}
