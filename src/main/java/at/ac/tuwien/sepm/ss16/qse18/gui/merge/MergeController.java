package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopicConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.merge.SubjectConflict;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class MergeController extends BaseController {

    @Autowired ApplicationContext applicationContext;
    @FXML private ListView<ObservableTopicConflict> listView;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    private ObservableList<ObservableTopicConflict> topicConflictList;
    @Autowired private SubjectConflict subjectConflict;
    private Stage stage;

    @FXML public void initialize() {
        List<ObservableTopicConflict> observableTopicConflicts =
            subjectConflict.getTopicConflicts().stream().map(ObservableTopicConflict::new)
                .collect(Collectors.toList());
        topicConflictList = FXCollections.observableArrayList(observableTopicConflicts);
        listView.setItems(topicConflictList);
        listView.setCellFactory(listView -> applicationContext.getBean(TopicConflictCell.class));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML void handleConfirm() {

    }

    @FXML void handleCancel() {
        boolean shouldCancel = showConfirmation(
            "This will cancel the import. Resolved conflicts will be lost. Are you sure?");
        if (shouldCancel) {
            stage.close();
        }
    }
}
