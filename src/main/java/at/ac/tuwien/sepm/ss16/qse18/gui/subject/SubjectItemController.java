package at.ac.tuwien.sepm.ss16.qse18.gui.subject;


import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicService;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl,Philipp Ganiu
 */
@Component public class SubjectItemController {

    @FXML public Node root;

    @FXML Label name;
    @FXML public ListView<ObservableTopic> topicListView;

    private ObservableSubject subject;
    private ObservableList<ObservableTopic> topicList;

    @Autowired private AlertBuilder alertBuilder;
    @Autowired SubjectTopicService subjectTopicService;
    @Autowired MainFrameController mainFrameController;

    @FXML
    public void initialize(ObservableSubject subject){
        try {
            this.subject = subject;
            List<ObservableTopic> observableTopics =
                    subjectTopicService.getTopicToSubject(subject.getSubject()).stream()
                    .map(ObservableTopic::new).collect(Collectors.toList());
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

    }

    @FXML public void handleDelete() {

    }

    @FXML public void handleEditQuestions(){
        mainFrameController.handleQuestionOverview(subject);
    }
    /*
    public void setSubject(ObservableSubject subject) {
        this.subject = subject;
    }
    */

    public Node getRoot() {
        return root;
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occured").contentText(e.getMessage()).build();
        alert.showAndWait();
    }

}
