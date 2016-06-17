package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestionConflict;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopicConflict;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hans-Joerg Schroedl
 */

@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class TopicConflictItemController
    extends BaseController {

    @FXML private Node root;

    @FXML private Label topicName;

    @FXML private ListView<ObservableQuestionConflict> questionConflictListView;

    @Autowired private ApplicationContext applicationContext;

    private ObservableTopicConflict topicConflict;

    private ObservableList<ObservableQuestionConflict> questionConflictList;

    public void setQuestionConflict(ObservableTopicConflict topicConflict) {
        this.topicConflict = topicConflict;
        List<ObservableQuestionConflict> observableTopicConflicts =
            topicConflict.getQuestionConflicts().stream().map(ObservableQuestionConflict::new)
                        .collect(Collectors.toList());
        questionConflictList = FXCollections.observableArrayList(observableTopicConflicts);
        questionConflictListView.setItems(questionConflictList);
        questionConflictListView.setCellFactory(listView -> applicationContext.getBean(QuestionConflictCell.class));
        topicName.setText(topicConflict.getTopicName());
    }

    public Node getRoot() {
        return root;
    }
}
