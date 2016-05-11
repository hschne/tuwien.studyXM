package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A controller for the subject detail view, in order to edit details or
 * add details for newly created subjects.
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class SubjectEditController {

    @FXML public TextField name;
    @FXML public TextField semester;
    @FXML public TextField ects;
    @FXML public TextField author;
    private Stage dialogStage;
    private ObservableSubject subject;
    private boolean isNew;
    private SubjectOverviewController overviewController;

    @Autowired public SubjectEditController(SubjectOverviewController controller) {
        this.overviewController = controller;
    }

    public void setStage(Stage stage) {
        this.dialogStage = stage;
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

    @FXML public void handleOk() {
        Subject newSubject = newSubjectFromFields();
        if (isNew) {
            overviewController.addSubject(new ObservableSubject(newSubject));
        } else {
            newSubject.setSubjectId(subject.getSubject().getSubjectId());
            overviewController.updateSubject(subject,newSubject);
        }
        dialogStage.close();
    }

    private Subject newSubjectFromFields() {
        Subject newSubject = new Subject();
        newSubject.setName(name.getText());
        newSubject.setEcts(parseDouble());
        newSubject.setSemester(semester.getText());
        newSubject.setAuthor(author.getText());
        return newSubject;
    }

    private float parseDouble() {
        try {
            return Float.parseFloat(ects.getText());

        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    @FXML public void handleCancel() {
        dialogStage.close();
    }

    private void initalizeFields() {
        name.setText(this.subject.getName());
        semester.setText(this.subject.getSemester());
        ects.setText(Double.toString(this.subject.getEcts()));
        author.setText(this.subject.getAuthor());
    }
}
