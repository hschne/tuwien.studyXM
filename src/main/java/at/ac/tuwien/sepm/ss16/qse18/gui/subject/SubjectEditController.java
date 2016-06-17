package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.SubjectNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * A controller for the subject detail view, in order to edit details or
 * add details for newly created resourceListView.
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class SubjectEditController
    extends BaseController {

    @Autowired SubjectNavigation subjectNavigator;
    @FXML private TextField name;
    @FXML private TextField semester;
    @FXML private TextField ects;
    @FXML private TextField author;
    private ObservableSubject subject;
    private boolean isNew;
    private SubjectOverviewController subjectOverviewController;
    @Autowired private SubjectService subjectService;

    @Autowired
    public void setSubjectOverviewController(SubjectOverviewController subjectOverviewController) {
        this.subjectOverviewController = subjectOverviewController;
    }

    public void setSubject(ObservableSubject subject) {
        if (subject != null) {
            this.subject = subject;
            initalizeFields();
            isNew = false;
        } else {
            this.subject = new ObservableSubject(new Subject());
            isNew = true;
        }
    }

    /**
     * Invoked when pressing ok, creates or updates exam then returns to overview
     */
    @FXML public void handleCreate() {
        Subject newSubject;
        try {
            newSubject = newSubjectFromFields();
        } catch (ServiceException e) {
            showError(e);
            return;
        }
        if (createOrUpdateSubject(newSubject))
            return;
        showSuccess("Subject successfully created");
        mainFrameController.handleSubjects();
    }

    /**
     * Invoked when pressing create and continue, creates another exam
     */
    @FXML public void handleCreateAndContinue() {
        subjectNavigator.handleCreateSubject(null);
    }

    /**
     * Invoked when pressing cancel, returns to subject overview
     */
    @FXML public void handleCancel() {
        mainFrameController.handleSubjects();
    }

    private boolean createOrUpdateSubject(Subject newSubject) {
        if (isNew) {
            if (tryCreateSubject(newSubject))
                return true;
        } else {
            if (tryUpdateSubject(newSubject))
                return true;
        }
        return false;
    }

    private boolean tryUpdateSubject(Subject newSubject) {
        try {
            newSubject.setSubjectId(subject.getSubject().getSubjectId());
            subjectService.updateSubject(newSubject);
            subjectOverviewController.updateSubject(subject, newSubject);
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
            return true;
        }
        return false;
    }

    private boolean tryCreateSubject(Subject newsubject) {
        try {
            Subject subjectWithId = subjectService.createSubject(newsubject);
            subjectOverviewController.addSubject(new ObservableSubject(subjectWithId));
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
            return true;
        }
        return false;
    }

    private Subject newSubjectFromFields() throws ServiceException {
        Subject newSubject = new Subject();
        newSubject.setName(name.getText());
        newSubject.setEcts(parseDouble());
        newSubject.setSemester(semester.getText());
        newSubject.setAuthor(author.getText());
        return newSubject;
    }

    private float parseDouble() throws ServiceException {
        try {
            return Float.parseFloat(ects.getText());

        } catch (NumberFormatException e) {
            throw new ServiceException("Error when parsing ects. Ects must be a number.");
        }
    }

    private void initalizeFields() {
        name.setText(this.subject.getName());
        semester.setText(this.subject.getSemester());
        ects.setText(Double.toString(this.subject.getEcts()));
        author.setText(this.subject.getAuthor());
    }
}
