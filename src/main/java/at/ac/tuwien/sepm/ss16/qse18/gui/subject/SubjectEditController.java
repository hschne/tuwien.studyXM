package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class SubjectEditController {

    private Stage dialogStage;

    private final SubjectService subjectService;

    private Subject subject;

    @FXML TextField name;

    @FXML TextField semester;

    @FXML TextField ects;

    private SubjectOverviewController overviewController;

    @Autowired
    public SubjectEditController(SubjectOverviewController controller, SubjectService subjectService) {
        this.overviewController = controller;
        this.subjectService = subjectService;
    }

    public void setStage(Stage stage){
        this.dialogStage = stage;
    }

    public void setSubject(Subject subject){
        if(subject != null){
            this.subject = subject;
        }
        else{
            this.subject = new Subject();
        }
    }

    @FXML public void handleOk(){
        subjectService.updateSubject(subject);
    }

    @FXML public void handleCancel(){
        dialogStage.close();
    }

    public void initalizeFields(){
        name.setText(this.subject.getName());
    }
}
