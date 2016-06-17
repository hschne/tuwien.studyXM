package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectOverviewController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectMerge;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.TopicConflict;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class MergeController extends BaseController {

    @Autowired ApplicationContext applicationContext;

    @Autowired SubjectOverviewController subjectOverviewController;

    @FXML private ListView<TopicConflict> listView;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    private ObservableList<TopicConflict> topicConflictList;
    private SubjectConflict subjectConflict;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSubjectConflict(SubjectConflict subjectConflict) {
        this.subjectConflict = subjectConflict;
        try {
            initializeListView(subjectConflict);
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }

    }

    private void initializeListView(SubjectConflict subjectConflict) throws ServiceException {
        List<TopicConflict> observableTopicConflicts = subjectConflict.getConflictingTopics();
        topicConflictList = FXCollections.observableArrayList(observableTopicConflicts);
        listView.setItems(topicConflictList);
        listView.setCellFactory(listView -> applicationContext.getBean(TopicConflictCell.class));
    }

    @FXML void handleConfirm() {
        try {
            SubjectMerge merge = applicationContext.getBean(SubjectMerge.class);
            merge.merge(subjectConflict);
            subjectOverviewController.initialize();
            showInformation("Subject has successfully been inported");
            stage.close();
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    @FXML public void handleCancel(Event e) {
        boolean shouldCancel = showConfirmation(
            "This will cancel the import. Resolved conflicts will be lost. Are you sure?");
        if (shouldCancel) {
            stage.close();
        }
    }
}
