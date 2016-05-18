package at.ac.tuwien.sepm.ss16.qse18.gui.topic;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectItemController;
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
        overviewController.addTopic(new ObservableTopic(newTopic));
        stage.close();
    }

    @FXML void handleCancel(){
        stage.close();
    }

}
