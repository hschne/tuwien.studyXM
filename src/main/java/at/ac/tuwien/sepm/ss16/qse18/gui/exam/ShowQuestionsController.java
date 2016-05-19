package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zhang Haixiang
 */
@Component
public class ShowQuestionsController implements GuiController {
    private Logger logger = LoggerFactory.getLogger(ShowQuestionsController.class);
    private Stage primaryStage;
    ObservableList<Question> questionObservableList;

    @FXML TableView<Question> tableQuestion;
    @FXML TableColumn<Question, Integer> columnQuestionID;
    @FXML TableColumn<Question, String> columnQuestion;
    @FXML TableColumn<Question, Integer> columnType;
    @FXML TableColumn<Question, Long> columnQuestionTime;


    @Override public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
