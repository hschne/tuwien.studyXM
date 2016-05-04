package at.ac.tuwien.sepm.ss16.qse18.gui;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A controller for subjectView, to create, delete, and edit subjects.
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class SubjectViewController {

    @FXML public TableView<Subject> subjects;
    @FXML public TableColumn<Subject, String> nameColumn;
    private Logger logger = LoggerFactory.getLogger(SubjectViewController.class);
    private SubjectService subjectService;


    @Autowired public SubjectViewController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @FXML public void initialize() {
        subjects.setItems(FXCollections.observableArrayList(subjectService.getSubjects()));
        nameColumn
            .setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
    }

}
