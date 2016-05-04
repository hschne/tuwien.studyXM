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

    private final Stage primaryStage;
    private final SubjectService subjectService;

    private Subject subject;

    @FXML TextField name;

    @FXML TextField semester;

    @FXML TextField ects;

    @Autowired
    public SubjectEditController(MainFrameController controller,SubjectService subjectService) {
        this.primaryStage = controller.getPimaryStage();
        this.subjectService = subjectService;
    }

    public void setSubject(Subject subject){
        if(subject != null){
            this.subject = subject;
        }
        else{
            this.subject = new Subject();
        }
    }
}
