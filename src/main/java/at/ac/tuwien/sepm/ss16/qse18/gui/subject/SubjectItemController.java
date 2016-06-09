package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.topic.TopicCell;
import at.ac.tuwien.sepm.ss16.qse18.gui.topic.TopicEditController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl,Philipp Ganiu
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class SubjectItemController extends
    BaseController {

    @FXML public Node root;
    @FXML Label name;
    @FXML public ListView<ObservableTopic> topicListView;
    @FXML public Button addTopicButton;

    private ObservableSubject subject;
    private ObservableList<ObservableTopic> topicList;

    @Autowired private SpringFXMLLoader springFXMLLoader;
    @Autowired private SubjectTopicQuestionService subjectTopicQuestionService;
    @Autowired private TopicService topicService;

    @FXML
    public void initialize(ObservableSubject subject){
        try {
            this.subject = subject;
            List<ObservableTopic> observableTopics =
                    subjectTopicQuestionService.getTopicToSubjectWithNumberOfQuestions(subject.getSubject())
                        .stream().map(ObservableTopic::new).collect(Collectors.toList());
            topicList = FXCollections.observableList(observableTopics);
            topicListView.setItems(topicList);
            topicListView.setCellFactory(listView  -> {
                TopicCell topicCell = new TopicCell();
                topicCell.setMainFrameController(mainFrameController);
                return topicCell;
            });
        }
        catch (ServiceException e){
            showError(e);
        }
    }

    public void loadFields() {
        name.setText(subject.getName());
    }


    @FXML public void handleExport() {
        System.out.println("Wow, much export!");
    }

    public void setAddTopicButtonAction(ObservableSubject subject,ObservableList<ObservableTopic> topicList){
        addTopicButton.setOnAction(event -> {
            mainFrameController.handleCreateTopic(subject,topicList);
            });
    }

    @FXML public void handleDelete() {
        //TODO implement this method
    }

    @FXML public void handleEditQuestions(){
        mainFrameController.handleQuestionOverview(subject);
    }

    public void setSubject(ObservableSubject subject) {
        this.subject = subject;
    }


    public Node getRoot() {
        return root;
    }


    public ObservableList<ObservableTopic> getTopicList(){
        return this.topicList;
    }

}
