package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
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

/**
 * @author Hans-Joerg Schroedl
 */
public class SubjectItemController {

    @FXML public Node root;
    @FXML Label name;
    @FXML public ListView<ObservableTopic> topicListView;

    private ObservableSubject subject;
    private TopicService topicService;
    private AlertBuilder alertBuilder;
    private ObservableList<ObservableTopic> topicList;


    public SubjectItemController(ObservableSubject subject) {
        this.subject = subject;
        loadGui();
        loadFields();
    }

    @FXML
    public void initialize(){
        /*try {
            List<ObservableTopic> observableTopics =
                topicService.getTopics().stream().map(ObservableTopic::new).collect(Collectors.toList());
            topicList = FXCollections.observableList(observableTopics);
            topicListView.setItems(topicList);
            //topicListView.setCellFactory(listView  -> new );
        }
        catch (ServiceException e){
            showAlert(e);
        }*/
        List<ObservableTopic> as = new ArrayList<>();
        as.add(new ObservableTopic(new Topic(1,"Test")));
        topicListView.setItems(FXCollections.observableList(as));
        topicListView.setCellFactory(listview -> new TopicCell());
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

    private void loadFields() {
        name.setText(subject.getName());
    }


    @FXML public void handleExport() {
        System.out.println("Wow, much export!");
    }

    @FXML public void handleNew() {

    }

    @FXML public void handleDelete(){

    }

    public Node getRoot() {
        return root;
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occured").contentText(e.getMessage()).build();
        alert.showAndWait();
    }

}
