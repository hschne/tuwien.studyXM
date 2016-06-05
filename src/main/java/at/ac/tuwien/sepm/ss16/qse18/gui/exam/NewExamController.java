package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Felix on 05.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class NewExamController
    extends BaseController {
    @FXML ListView<ObservableSubject> listSubjects;
    @FXML TextField fieldName;
    @FXML TextField fieldDueDate;
    private Exam exam;

    @Autowired SubjectService subjectService;
    @Autowired ExamService examService;

    @FXML public void create() {
        logger.debug("Create exam called");
        Exam exam = null;
        try {
            exam = examService.validate(fieldName.getText(), fieldDueDate.getText(),
                listSubjects.getSelectionModel().getSelectedItem().getSubject());
        } catch(ServiceException e) {
            logger.warn("Invalid input", e);
            showError(e);
            return;
        }
        try {
            exam = examService.createExam(exam,
                listSubjects.getSelectionModel().getSelectedItem().getSubject());
        } catch(ServiceException e) {
            logger.error("Could not create new exam", e);
            showError(e);
            return;
        }
        logger.debug("Created exam " + exam);
        mainFrameController.handleHome();
    }

    @FXML public void cancel() {
        mainFrameController.handleHome();
    }

    @FXML public void initialize() {
        this.exam = null;
        try {
            initializeTable();
        } catch(ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    private void initializeTable() throws ServiceException {
        List<ObservableSubject> observableSubjects =
            subjectService.getSubjects().stream().map(ObservableSubject::new)
                .collect(Collectors.toList());

        listSubjects.setItems(FXCollections.observableList(observableSubjects));

        listSubjects.setCellFactory(lv -> new ListCell<ObservableSubject>() {
            @Override public void updateItem(ObservableSubject subject, boolean empty) {
                super.updateItem(subject, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text =
                        subject.getSubject().getSubjectId() + ": " + subject.getName() + " (" + subject
                            .getSemester() + ")";
                    setText(text);
                }
            }
        });
    }
}
