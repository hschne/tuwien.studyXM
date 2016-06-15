package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.application.MainApplication;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.QuestionNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.topic.TopicCell;
import at.ac.tuwien.sepm.ss16.qse18.service.ExportService;
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


import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class represents the controller for a single list item in the subject overview.
 *
 * @author Hans-Joerg Schroedl,Philipp Ganiu
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class SubjectItemController
    extends BaseController {

    @FXML private Node root;
    @FXML private Label name;
    @FXML private ListView<ObservableTopic> topicListView;
    @FXML private Button addTopicButton;
    @FXML private TextField topicTf;

    private ObservableSubject subject;
    private ObservableList<ObservableTopic> topicList;

    @Autowired private SubjectTopicQuestionService subjectTopicQuestionService;
    @Autowired private MainApplication mainApplication;

    @Autowired private QuestionNavigation questionNavigation;
    @Autowired private TopicService topicService;
    @Autowired private ExportService exportService;

    @FXML public void initialize(ObservableSubject subject) {
        try {
            this.subject = subject;
            List<ObservableTopic> observableTopics = subjectTopicQuestionService
                .getTopicToSubjectWithNumberOfQuestions(subject.getSubject()).stream()
                .map(ObservableTopic::new).collect(Collectors.toList());
            topicList = FXCollections.observableList(observableTopics);
            topicListView.setItems(topicList);
            topicListView.setCellFactory(listView -> {
                TopicCell topicCell = new TopicCell();
                topicCell.setQuestionNavigation(questionNavigation);
                return topicCell;
            });
        } catch (ServiceException e) {
            showError(e);
        }
    }

    public void loadFields() {
        name.setText(subject.getName());
    }

    @FXML public void handleExport() {
        exportService.setSubject(subject.getSubject());
        try {
            File selected = selectFile();
            if (selected != null) {
                exportService.export(selected.getAbsolutePath());
                showSuccess("Subject successfully exported");
            }
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    public void setAddTopicButtonAction(ObservableSubject subject,
        ObservableList<ObservableTopic> topicList) {
        addTopicButton.setOnAction(event -> {
            Topic newTopic = new Topic(-1, topicTf.getText());
            try {
                Topic t = topicService.createTopic(newTopic, subject.getSubject());
                topicList.add(new ObservableTopic(t));
                topicTf.clear();
            } catch (ServiceException e) {
                showError(e);
            }
        });
    }

    @FXML public void handleDelete() {
        //TODO implement this method
    }

    @FXML public void handleEditQuestions() {
        mainFrameController.handleQuestionOverview(subject);
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

    private File selectFile() {
        FileChooser fileChooser = new FileChooser();
        String defaultPath = "src/main/resources/export/zip";
        File defaultDirectory = new File(defaultPath);
        fileChooser.setInitialDirectory(defaultDirectory);
        fileChooser.setInitialFileName("xms_" + subject.getName() + "_" + subject.getAuthor());

        fileChooser.getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("XMS FILES (.xms)", "*.xms"));
        fileChooser.setTitle("Choose output directory");
        Stage mainStage = mainApplication.getPrimaryStage();
        return fileChooser.showSaveDialog(mainStage);
    }

}
