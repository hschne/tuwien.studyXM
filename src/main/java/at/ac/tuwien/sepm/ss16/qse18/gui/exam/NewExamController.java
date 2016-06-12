package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for creating new exams. Allows setting due date, name, and subject.
 * <p>
 * Created by Felix on 05.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class NewExamController
    extends BaseController {
    @FXML ComboBox<ObservableSubject> subjects;
    @FXML TextField fieldName;
    @FXML DatePicker dueDate;

    @Autowired SubjectService subjectService;
    @Autowired ExamService examService;

    @FXML public void handleOk() {
        logger.debug("Create exam called");
        try {
            Exam exam = createExamFromFields();
            examService.validate(exam);
            examService.createExam(exam);
        } catch (DtoValidatorException | ServiceException e) {
            logger.error(e);
            showError(e);
            return;
        }
        logger.debug("Created exam");
        mainFrameController.handleHome();
    }

    @FXML public void handleCancel() {
        mainFrameController.handleHome();
    }

    @FXML public void initialize() {
        try {
            initializeTable();
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    private Exam createExamFromFields() throws DtoValidatorException {
        String name = fieldName.getText();
        Timestamp date = null;
        int subjectId;
        try {
            date = Timestamp.valueOf(dueDate.getValue().atStartOfDay());
        } catch(NullPointerException e) {
            logger.warn("No due date selected", e);
            throw new DtoValidatorException("Please select a due date.");
        }

        try {
            subjectId = subjects.getSelectionModel().getSelectedItem().getSubject().getSubjectId();
        } catch(NullPointerException e) {
            logger.warn("No subject selected", e);
            throw new DtoValidatorException("Please select a subject.");
        }

        return new Exam(name, date, subjectId);
    }

    private void initializeTable() throws ServiceException {
        List<ObservableSubject> observableSubjects =
            subjectService.getSubjects().stream().map(ObservableSubject::new)
                .collect(Collectors.toList());
        subjects.setItems(FXCollections.observableList(observableSubjects));
        subjects.setButtonCell(new SubjectComboBoxCell());
        subjects.setCellFactory(p -> new SubjectComboBoxCell());
    }

    private class SubjectComboBoxCell extends ListCell<ObservableSubject> {
        @Override protected void updateItem(ObservableSubject item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getName() + " (" + item.getSemester() + ")");
            }
        }


    }
}
