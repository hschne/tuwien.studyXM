package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopicConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectMerge;
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
    private SubjectConflict subjectConflict;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSubjectConflict(SubjectConflict subjectConflict){
        this.subjectConflict = subjectConflict;
        try {
            initializeListView(subjectConflict);
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }

    }

    private void initializeListView(SubjectConflict subjectConflict) throws ServiceException {
        List<ObservableTopicConflict>
            observableTopicConflicts = subjectConflict.getConflictingTopics().stream().map(
            ObservableTopicConflict::new)
            .collect(Collectors.toList());
        topicConflictList = FXCollections.observableArrayList(observableTopicConflicts);
        listView.setItems(topicConflictList);
        listView.setCellFactory(listView -> applicationContext.getBean(TopicConflictCell.class));
    }

    @FXML void handleConfirm() {
        try {
            SubjectMerge merge = applicationContext.getBean(SubjectMerge.class);
            merge.merge(subjectConflict);
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    @FXML void handleCancel() {
        boolean shouldCancel = showConfirmation(
            "This will cancel the import. Resolved conflicts will be lost. Are you sure?");
        if (shouldCancel) {
            stage.close();
        }
    }
}
