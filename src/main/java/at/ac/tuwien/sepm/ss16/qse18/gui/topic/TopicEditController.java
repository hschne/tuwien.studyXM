package at.ac.tuwien.sepm.ss16.qse18.gui.topic;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Philipp Ganiu
 */
@Component  @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class TopicEditController extends
    BaseController{
    @FXML public TextField topic;

    private ObservableList<ObservableTopic> topicList;
    private Subject subject;

    @Autowired MainFrameController mainFrameController;
    @Autowired private TopicService topicService;


    @FXML
    public void handleOk(){
        Topic newTopic = new Topic(-1,topic.getText());
        try{
            Topic t = topicService.createTopic(newTopic,subject);
            topicList.add(new ObservableTopic(t));
            mainFrameController.handleSubjects();
        }
        catch (ServiceException e){
            showError(e);
        }
    }

    public void setSubject(Subject subject){
        this.subject = subject;
    }


    public void setTopicList(ObservableList<ObservableTopic> topicList){
        this.topicList = topicList;
    }

    @FXML void handleCancel(){
        mainFrameController.handleSubjects();
    }

}
