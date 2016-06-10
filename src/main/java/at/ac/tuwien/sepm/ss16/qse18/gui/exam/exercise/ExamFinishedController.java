package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

/**
 * @author Philipp Ganiu
 */
@Component
public class ExamFinishedController extends BaseController {
    @FXML private Label mainLabel;

    @FXML public void initialize(){
            mainLabel.setText("The exam is over");
    }
}
