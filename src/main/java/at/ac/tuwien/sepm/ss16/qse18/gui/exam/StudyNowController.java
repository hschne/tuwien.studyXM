package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Felix on 01.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class StudyNowController
    extends BaseController {

    @FXML public void newExam() {
        mainFrameController.handleCreateExam();
    }

    @FXML public void resumeExam() {

    }

    @FXML public void exportAsPDF() {

    }
}
