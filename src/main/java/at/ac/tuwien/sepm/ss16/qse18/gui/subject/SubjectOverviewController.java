package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A controller for subjectView, to create, delete, and edit subjectListView.
 *
 * @author Hans-Joerg Schroedl
 */
@Component  public class SubjectOverviewController
    implements GuiController {

    @Autowired ApplicationContext applicationContext;

    @FXML public ListView<ObservableSubject> subjectListView;

    private Logger logger = LoggerFactory.getLogger(SubjectOverviewController.class);
    private ObservableList<ObservableSubject> subjectList;
    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;
    private SubjectService subjectService;
    private AlertBuilder alertBuilder;


    @Autowired public SubjectOverviewController(SpringFXMLLoader springFXMLLoader,
        SubjectService subjectService, AlertBuilder alertBuilder) {
        this.springFXMLLoader = springFXMLLoader;
        this.subjectService = subjectService;
        this.alertBuilder = alertBuilder;
    }

    @FXML public void initialize() {
        try {
            logger.debug("Initializing subject table");
            //Lambdas to create a new observable subject for each subject
            List<ObservableSubject> observableSubjects =
                subjectService.getSubjects().stream().map(ObservableSubject::new)
                    .collect(Collectors.toList());
            subjectList = FXCollections.observableArrayList(observableSubjects);
            subjectListView.setItems(subjectList);
            subjectListView.setCellFactory(listView -> applicationContext.getBean(SubjectCell.class));
        } catch (ServiceException e) {
            showAlert(e);
        }
    }

    @FXML public void handleNew() throws IOException {
        logger.debug("Create new subject");
        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, SubjectEditController> editSubjectWrapper =
            springFXMLLoader
                .loadAndWrap("/fxml/subject/subjectEditView.fxml", SubjectEditController.class);
        SubjectEditController childController = editSubjectWrapper.getController();
        childController.setSubject(null);
        childController.setStage(stage);
        configureStage(stage, "New Subject", editSubjectWrapper);
    }

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
            showAlert(e);
        }
    }

    private boolean actionConfirmed() {
        Alert alert = alertBuilder.alertType(Alert.AlertType.CONFIRMATION).title("Confirmation")
            .setResizable(true)
            .headerText("Are you sure?")
            .contentText("This will remove the subject and all associated queestions, materials and exams.")
            .build();
        alert.showAndWait();
        ButtonType result = alert.getResult();
        return result == ButtonType.OK;
    }


    @FXML public void handleEdit() throws IOException {
        logger.debug("Editing selected subject");
        ObservableSubject subject = subjectListView.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, SubjectEditController> editSubjectWrapper =
            springFXMLLoader
                .loadAndWrap("/fxml/subject/subjectEditView.fxml", SubjectEditController.class);
        SubjectEditController childController = editSubjectWrapper.getController();
        childController.setSubject(subject);
        childController.setStage(stage);
        configureStage(stage, "Edit Subject", editSubjectWrapper);
    }

    public void addSubject(ObservableSubject subject) {
        try {
            Subject s = subjectService.createSubject(subject.getSubject());
            subjectList.add(new ObservableSubject(s));
        } catch (ServiceException e) {
            showAlert(e);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    private void configureStage(Stage stage, String title,
        SpringFXMLLoader.FXMLWrapper<Object, SubjectEditController> editSubjectWrapper)
        throws IOException {
        stage.setTitle(title);
        stage.setScene(new Scene((Parent) editSubjectWrapper.getLoadedObject(), 400, 400));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.primaryStage);
        stage.showAndWait();
    }

    public void updateSubject(ObservableSubject observableSubject, Subject subject) {
        try {
            subjectService.updateSubject(subject);
            updateEntry(observableSubject, subject);
        } catch (ServiceException e) {
            showAlert(e);
        }
    }

    private void updateEntry(ObservableSubject observableSubject, Subject subject) {
        observableSubject.setName(subject.getName());
        observableSubject.setEcts(subject.getEcts());
        observableSubject.setSemester(subject.getSemester());
        observableSubject.setAuthor(subject.getAuthor());
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occured").contentText(e.getMessage()).build();
        alert.showAndWait();
    }

}
