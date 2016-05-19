package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zhang Haixiang
 */
@Component
public class InsertExamValuesController implements GuiController {
    private Logger logger = LoggerFactory.getLogger(InsertExamValuesController.class);
    private Stage primaryStage;

    @FXML public Button buttonCreate;
    @FXML public Button buttonCancel;

    @FXML public TextField fieldAuthor;
    @FXML public TextField fieldSubjectID;

    @Autowired ExamService eService;

    @Override public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML public void create(){
        logger.debug("Create button pressed. Entering create method.");

    }

    @FXML public void cancel(){
        Stage stage = (Stage)buttonCancel.getScene().getWindow();
        stage.close();
    }
}
