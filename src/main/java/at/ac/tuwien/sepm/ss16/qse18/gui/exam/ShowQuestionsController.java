package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise.CreateExamController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller of the showQuestion window in which the questions of the chosen exam are displayed
 *
 * @author Zhang Haixiang
 */
@Component public class ShowQuestionsController extends BaseController {
    ObservableList<Question> questionObservableList;
    @Autowired CreateExamController createExamController;
    @FXML TableView<Question> tableQuestion;
    @FXML TableColumn<Question, Integer> columnQuestionID;
    @FXML TableColumn<Question, String> columnQuestion;
    @FXML TableColumn<Question, Integer> columnType;
    @FXML TableColumn<Question, Long> columnQuestionTime;


    @FXML public void initialize() {
        questionObservableList =
            FXCollections.observableArrayList(this.createExamController.getQuestionList());
        columnQuestionID.setCellValueFactory(new PropertyValueFactory<>("questionId"));
        columnQuestion.setCellValueFactory(new PropertyValueFactory<>("question"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnQuestionTime.setCellValueFactory(new PropertyValueFactory<>("questionTime"));
        tableQuestion.setItems(questionObservableList);
    }
}
