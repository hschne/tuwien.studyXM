package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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


    private MainFrameController parentController;
    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;

    @FXML public TableView<Subject> subjects;
    @FXML public TableColumn<Subject, String> nameColumn;
    private Logger logger = LoggerFactory.getLogger(SubjectOverviewController.class);
    private SubjectService subjectService;


    @Autowired public SubjectOverviewController(MainFrameController controller,SubjectService subjectService) {
        parentController = controller;
        this.springFXMLLoader = controller.getSpringFXMLLoader();
        this.primaryStage = controller.getPimaryStage();
        this.subjectService = subjectService;
    }

    @FXML public void initialize() {
        subjects.setItems(FXCollections.observableArrayList(subjectService.getSubjects()));
        nameColumn
            .setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
    }

    @FXML
    public void newSubject() throws IOException{
        Stage stage = new Stage();
        SpringFXMLLoader.FXMLWrapper<Object, SubjectEditController> editSubjectWrapper =
            springFXMLLoader.loadAndWrap("/fxml/subject/subjectEdit.fxml", SubjectEditController.class);
        editSubjectWrapper.getController().setSubject(null);
        stage.setTitle("Edit Subject");
        stage.setScene(new Scene((Parent) this.springFXMLLoader.load("/fxml/subject/subjectEdit.fxml"), 600, 400));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.primaryStage);
        stage.showAndWait();
    }

    @FXML public void deleteSubject(){

    }

    @FXML public void editSubject(){

    }



}
