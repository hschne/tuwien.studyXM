package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A controller for subjectView, to create, delete, and edit subjects.
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class SubjectOverviewController {


    @FXML public TableView<Subject> subjects;
    @FXML public TableColumn<Subject, String> nameColumn;
    private ObservableList<Subject> subjectList;
    private MainFrameController parentController;
    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;
    private Logger logger = LoggerFactory.getLogger(SubjectOverviewController.class);
    private SubjectService subjectService;


    @Autowired public SubjectOverviewController(MainFrameController controller,
        SubjectService subjectService) {
        parentController = controller;
        this.springFXMLLoader = controller.getSpringFXMLLoader();
        this.primaryStage = controller.getPimaryStage();
        this.subjectService = subjectService;
    }

    @FXML public void initialize() {
        subjectList = FXCollections.observableArrayList(subjectService.getSubjects());
        subjects.setItems(subjectList);
        nameColumn
            .setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
    }

    @FXML public void newSubject() throws IOException {
        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, SubjectEditController> editSubjectWrapper =
            springFXMLLoader
                .loadAndWrap("/fxml/subject/subjectEdit.fxml", SubjectEditController.class);
        SubjectEditController childController = editSubjectWrapper.getController();
        childController.setSubject(null);
        childController.setStage(stage);
        configureStage(stage, "New Subject", editSubjectWrapper);
    }

    @FXML public void deleteSubject() {
        Subject subjectToDelete = subjects.getSelectionModel().getSelectedItem();
        subjectService.deleteSubject(subjectToDelete);
        subjectList.remove(subjectToDelete);
    }

    @FXML public void editSubject() throws IOException {
        Subject subject = subjects.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, SubjectEditController> editSubjectWrapper =
            springFXMLLoader
                .loadAndWrap("/fxml/subject/subjectEdit.fxml", SubjectEditController.class);
        SubjectEditController childController = editSubjectWrapper.getController();
        childController.setSubject(subject);
        childController.setStage(stage);
        configureStage(stage, "Edit Subject", editSubjectWrapper);
    }

    private void configureStage(Stage stage, String title,
        SpringFXMLLoader.FXMLWrapper<Object, SubjectEditController> editSubjectWrapper)
        throws IOException {
        stage.setTitle(title);
        stage.setScene(new Scene((Parent) editSubjectWrapper.getLoadedObject(), 400, 300));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.primaryStage);
        stage.showAndWait();
    }



}
