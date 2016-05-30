package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A controller for subjectView, to create, delete, and edit resourceListView.
 *
 * @author Hans-Joerg Schroedl
 */
@Component public class SubjectOverviewController implements GuiController {

    @FXML public ListView<ObservableSubject> subjectListView;
    @FXML public Button editButton;
    @FXML public Button deleteButton;
    @Autowired ApplicationContext applicationContext;
    @Autowired MainFrameController mainFrameController;
    private static final Logger logger = LogManager.getLogger();
    private ObservableList<ObservableSubject> subjectList;

    private SubjectService subjectService;
    private AlertBuilder alertBuilder;

    @Autowired
    public SubjectOverviewController(SubjectService subjectService, AlertBuilder alertBuilder) {
        this.subjectService = subjectService;
        this.alertBuilder = alertBuilder;
    }

    @FXML public void initialize() {
        try {
            logger.debug("Initializing subject table");
            //Lambdas to create a new observable subject for each subject
            initializeButtons();
            initializeListView();
        } catch (ServiceException e) {
            logger.error(e);
            showAlert();
        }
    }

    /**
     * ActionHandler for creating a new subject
     *
     * @throws IOException
     */
    @FXML public void handleNew() throws IOException {
        logger.debug("Create new subject");
        mainFrameController.handleCreateSubject(null);
    }

    /**
     * Action handler for deleting a subject
     */
    @FXML public void handleDelete() {
        try {
            logger.debug("Delete subject from table");
            if (!actionConfirmed()) {
                return;
            }
            ObservableSubject subjectToDelete =
                subjectListView.getSelectionModel().getSelectedItem();
            if (subjectToDelete != null) {
                subjectService.deleteSubject(subjectToDelete.getSubject());
                subjectList.remove(subjectToDelete);
            }
        } catch (ServiceException e) {
            logger.error(e);
            showAlert();
        }
    }

    /**
     * Action handler for editing the selected subject
     *
     * @throws IOException
     */
    @FXML public void handleEdit() throws IOException {
        logger.debug("Editing selected subject");
        ObservableSubject subject = subjectListView.getSelectionModel().getSelectedItem();
        mainFrameController.handleCreateSubject(subject);
    }

    /**
     * Creates a new subject and adds it to the subject list
     *
     * @param subject The subject to be added
     */
    public void addSubject(ObservableSubject subject) {
        try {
            Subject s = subjectService.createSubject(subject.getSubject());
            subjectList.add(new ObservableSubject(s));
        } catch (ServiceException e) {
            logger.error(e);
            showAlert();
        }
    }

    /**
     * Updates a subject in the list and in the database
     *
     * @param observableSubject The current subject in the list
     * @param subject           The new subject, containing new values
     */
    public void updateSubject(ObservableSubject observableSubject, Subject subject) {
        try {
            subjectService.updateSubject(subject);
            updateEntry(observableSubject, subject);
        } catch (ServiceException e) {
            logger.error(e);
            showAlert();
        }
    }

    private void initializeListView() throws ServiceException {
        List<ObservableSubject> observableSubjects =
            subjectService.getSubjects().stream().map(ObservableSubject::new)
                .collect(Collectors.toList());
        subjectList = FXCollections.observableArrayList(observableSubjects);
        subjectListView.setItems(subjectList);
        subjectListView.setCellFactory(listView -> applicationContext.getBean(SubjectCell.class));
    }

    private void initializeButtons() {
        editButton.disableProperty()
            .bind(subjectListView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty()
            .bind(subjectListView.getSelectionModel().selectedItemProperty().isNull());

    }

    private boolean actionConfirmed() {
        Alert alert = alertBuilder.alertType(Alert.AlertType.CONFIRMATION).title("Confirmation")
            .setResizable(true).headerText("Are you sure?").contentText(
                "This will remove the subject and all associated questions, materials and exams.")
            .build();
        alert.showAndWait();
        ButtonType result = alert.getResult();
        return result == ButtonType.OK;
    }

    private void updateEntry(ObservableSubject observableSubject, Subject subject) {
        observableSubject.setName(subject.getName());
        observableSubject.setEcts(subject.getEcts());
        observableSubject.setSemester(subject.getSemester());
        observableSubject.setAuthor(subject.getAuthor());
    }

    private void showAlert() {
        Alert alert =
            alertBuilder.alertType(Alert.AlertType.ERROR).title("Error").setResizable(true)
                .headerText("Could not load Subjects")
                .contentText("Please make sure you are connected to the database").build();
        alert.showAndWait();
    }

}
