package at.ac.tuwien.sepm.ss16.qse18.gui.statistic;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.StatisticService;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicQuestionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
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

    @Autowired
    private SubjectTopicQuestionService subjectTopicQuestionService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    ApplicationContext applicationContext;


    @FXML
    public void initialize(ObservableSubject subject) {
        try {
            this.subject = subject;
            List<ObservableTopic> observableTopics =
                    subjectTopicQuestionService.getTopicToSubjectWithNumberOfQuestions(subject.getSubject())
                            .stream().map(ObservableTopic::new).collect(Collectors.toList());
            ObservableList<ObservableTopic> topicList = FXCollections.observableList(observableTopics);
            topicListView.setItems(topicList);
            topicListView.setCellFactory(listView -> {
                return applicationContext.getBean(StatisticTopicCell.class);
            });
        } catch (ServiceException e) {
            showError(e);
        }
    }

    public void loadFields() throws ServiceException {

        statisticService.initializeHints();
        double[] result = statisticService.gradeAllExamsForSubject(subject.getSubject());
        int estimated = (int) (subject.getEcts() * 25);
        int spent = subject.getTimeSpent() / 60;
        labelName.setText(subject.getName() + " (" + subject.getSemester() + ")");
        labelAvgExamResult.setText("average exercise exam result | " +
                ((int)(result[0]) == -1 ? "?" : result[0]) +
                " (" + (int) result[1] +
                ((int) result[1] == 1 ? " exam)" : " exams)"));

        labelEctsTime.setText(estimated + " hours estimated (" +
                String.valueOf(subject.getEcts()).substring(0, 3) + " ECTS)");
        labelTimeSpent.setText(spent + " hours spent");
        labelMoreTime.setText(Math.abs(estimated - spent) +
                " hours " + (spent - estimated <= 0 ? "less" : "more") + " than expected");

        labelHint.setText(statisticService.getHint(subject.getSubject()));

        Queue<String> achievements = new LinkedList<>();
        if (result[0] <= 2.0 && result[1] >= 2) {
            achievements.add("/icons/acbrain.png");
        }

        if (spent - estimated <= 0 && result[1] >= 2) {
            achievements.add("/icons/actime.png");
        }
        if (statisticService.checkKnowItAllAchievement(subject.getSubject())) {
            achievements.add("/icons/acknow.png");
        }
        setAchievements(achievements);

    }

    private void setAchievements(Queue<String> achievements) {
        achievement1.setImage(achievements.peek() != null ? new Image(achievements.poll()) : null);
        achievement2.setImage(achievements.peek() != null ? new Image(achievements.poll()) : null);
        achievement3.setImage(achievements.peek() != null ? new Image(achievements.poll()) : null);
    }

    public void setSubject(ObservableSubject subject) {
        this.subject = subject;
    }

    public Node getRoot() {
        return root;
    }
}

