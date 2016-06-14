package at.ac.tuwien.sepm.ss16.qse18.gui.statistic;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.QuestionNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.topic.TopicCell;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExportServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class represents the controller for a single list item in the statistic overview.
 *
 * @author Julian Strohmayer
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticItemController extends
        BaseController {

    @FXML
    private Node root;
    @FXML
    private Label labelName;
    @FXML
    private ListView<ObservableTopic> topicListView;

    @FXML
    private Label labelAvgExamResult;
    @FXML
    private Label labelTimeSpent;
    @FXML
    private Label labelEctsTime;
    @FXML
    private Label labelHint;
    @FXML
    private Label labelMoreTime;
    @FXML
    private ImageView achievement1;
    @FXML
    private ImageView achievement2;
    @FXML
    private ImageView achievement3;

    private ObservableSubject subject;
    private ObservableList<ObservableTopic> topicList;

    @Autowired
    private SubjectTopicQuestionService subjectTopicQuestionService;


    @Autowired
    private TopicService topicService;

    @FXML
    public void initialize(ObservableSubject subject) {
        try {
            this.subject = subject;
            List<ObservableTopic> observableTopics =
                    subjectTopicQuestionService.getTopicToSubjectWithNumberOfQuestions(subject.getSubject())
                            .stream().map(ObservableTopic::new).collect(Collectors.toList());
            topicList = FXCollections.observableList(observableTopics);
            topicListView.setItems(topicList);
            topicListView.setCellFactory(listView -> {
                StatisticTopicCell statisticTopicCell = new StatisticTopicCell();
                return statisticTopicCell;
            });
        } catch (ServiceException e) {
            showError(e);
        }
    }

    public void loadFields() {
        labelName.setText(subject.getName() + " (" + subject.getSemester() + ")");
        labelAvgExamResult.setText("avg. exam result | 80%");

        int estimated = (int) subject.getEcts() * 25;
        labelEctsTime.setText(estimated + " hours estimated (" +
                String.valueOf(subject.getEcts()).substring(0, 3) + " ECTS)");
        labelTimeSpent.setText(subject.getTimeSpent() + " hours spent");

        String moreLess = estimated - subject.getTimeSpent() <= 0 ? "more" : "less";
        labelMoreTime.setText(Math.abs(estimated - subject.getTimeSpent()) +
                " hours " + moreLess + " than expected");

        labelHint.setText("Hint - study a lot! ;)");

        //if(moreLess=="less"){
            achievement1.setImage( new Image("/icons/acbrain.png"));
        //}
        //if(moreLess=="less"){
            achievement2.setImage( new Image("/icons/acknow.png"));
        //}
       // if(moreLess=="less"){
            achievement3.setImage( new Image("/icons/actime.png"));
       // }


    }

    public void setSubject(ObservableSubject subject) {
        this.subject = subject;
    }

    public Node getRoot() {
        return root;
    }

    public ObservableList<ObservableTopic> getTopicList() {
        return this.topicList;
    }

}

