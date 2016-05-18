package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl,Philipp Ganiu
 */
@Component public class SubjectItemController {

    @FXML public Node root;
    @FXML Label name;
    @FXML public ListView<ObservableTopic> topicListView;

    private Logger logger = LoggerFactory.getLogger(SubjectItemController.class);
    private ObservableSubject subject;
    private ObservableList<ObservableTopic> topicList;

    @Autowired private SpringFXMLLoader springFXMLLoader;
    @Autowired private AlertBuilder alertBuilder;
    @Autowired private SubjectTopicQuestionService subjectTopicQuestionService;
    @Autowired private TopicService topicService;
    @Autowired private MainFrameController mainFrameController;

    @FXML
    public void initialize(ObservableSubject subject){
        try {
            this.subject = subject;
            List<ObservableTopic> observableTopics =
                    subjectTopicQuestionService.getTopicToSubjectWithNumberOfQuestions(subject.getSubject())
                        .stream().map(ObservableTopic::new).collect(Collectors.toList());
            topicList = FXCollections.observableList(observableTopics);
            topicListView.setItems(topicList);
            topicListView.setCellFactory(listView  -> new TopicCell());
        }
        catch (ServiceException e){
            showAlert(e);
        }
    }


    private void loadGui() {
        FXMLLoader fxmlLoader =
            new FXMLLoader(getClass().getResource("/fxml/subject/subjectItem.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFields() {
        name.setText(subject.getName());
    }


    @FXML public void handleExport() {
        System.out.println("Wow, much export!");
    }

    @FXML public void handleNew() {
        logger.debug("Create new topic");
        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, TopicEditController> editTopicWrapper = null;
        try {
            editTopicWrapper = springFXMLLoader
                .loadAndWrap("/fxml/topic/topicEditView.fxml", TopicEditController.class);
        } catch (IOException e) {
            logger.error("Couldn't load new Topic stage",e);
        }
        TopicEditController childController = editTopicWrapper.getController();
        childController.setStage(stage);
        stage.setTitle("New Topic");
        stage.setScene(new Scene((Parent) editTopicWrapper.getLoadedObject(), 400, 300));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        System.out.println(subject.getSubject());
    }

    @FXML public void handleDelete() {

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



    public void addTopic(ObservableTopic topic){
        try{
            topicService.createTopic(topic.getT(),this.subject.getSubject());
            topicList.add(topic);
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

}
