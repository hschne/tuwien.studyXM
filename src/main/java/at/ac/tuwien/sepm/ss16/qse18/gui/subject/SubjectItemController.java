package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.topic.TopicCell;
import at.ac.tuwien.sepm.ss16.qse18.gui.topic.TopicEditController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl,Philipp Ganiu
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class SubjectItemController {

    @FXML public Node root;
    @FXML Label name;
    @FXML public ListView<ObservableTopic> topicListView;
    @FXML public Button addTopicButton;

    private Logger logger = LoggerFactory.getLogger(SubjectItemController.class);
    private ObservableSubject subject;
    private ObservableList<ObservableTopic> topicList;

    @Autowired private SpringFXMLLoader springFXMLLoader;
    @Autowired private AlertBuilder alertBuilder;
    @Autowired private SubjectTopicQuestionService subjectTopicQuestionService;
    @Autowired private TopicService topicService;
    @Autowired private MainFrameController mainFrameController;
    @Autowired private SubjectOverviewController subjectOverviewController;


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
            showAlert(e);
        }
    }

    public void loadFields() {
        name.setText(subject.getName());
    }


    @FXML public void handleExport() {
        System.out.println("Wow, much export!");
    }

    public void setAddTopicButtonAction(ObservableSubject subject,ObservableList<ObservableTopic> topicList){
        addTopicButton.setOnAction(event -> { Stage stage = new Stage();
            SpringFXMLLoader.FXMLWrapper<Object, TopicEditController> editTopicWrapper = null;
            try {
                editTopicWrapper = springFXMLLoader
                    .loadAndWrap("/fxml/topic/topicEditView.fxml", TopicEditController.class);
            } catch (IOException e) {
                logger.error("Couldn't load new Topic stage", e);
            }
            TopicEditController childController = editTopicWrapper.getController();
            childController.setStage(stage);
            childController.setSubject(subject.getSubject());
            childController.setTopicList(topicList);
            stage.setTitle("New Topic");
            stage.setScene(new Scene((Parent) editTopicWrapper.getLoadedObject(), 400, 300));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();});
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



    public void addTopic(ObservableTopic topic,Subject subject,ObservableList<ObservableTopic> topicList){
        try{
            Topic t = topicService.createTopic(topic.getT(),subject);
            topicList.add(new ObservableTopic(t));
        }
        catch (ServiceException e){
            showAlert(e);
        }
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occured").contentText(e.getMessage()).build();
        alert.showAndWait();
    }

    public ObservableList<ObservableTopic> getTopicList(){
        return this.topicList;
    }

}
