package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExerciseExamServiceImpl;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller of the exerciseExam window, in which all exams of the database are displayed
 *
 * @author Zhang Haixiang
 */

@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class CreateExamController
    extends BaseController {

    @FXML public Button buttonShowQuestions;
    @FXML public Button buttonNewExam;
    @FXML private Button startExamButton;
    @FXML public TableView<ExerciseExam> tableExam;
    @FXML public TableColumn<ExerciseExam, Integer> columnExamID;
    @FXML public TableColumn<ExerciseExam, Timestamp> columnCreated;
    @FXML public TableColumn<ExerciseExam, Boolean> columnPassed;
    @FXML public TableColumn<ExerciseExam, String> columnAuthor;
    @Autowired ExerciseExamServiceImpl examService;
    //@Autowired ExamServiceImpl examService;

    @Autowired QuestionService questionService;
    private ExerciseExam exerciseExam;
    private List<Question> questionList = new ArrayList<>();

    @FXML public void initialize() {

        startExamButton.disableProperty().bind(Bindings.isEmpty(tableExam.getSelectionModel().
                                                                            getSelectedItems()));
        this.exerciseExam = null;
        try {
            initializeTable();
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    @FXML public void createExerciseExam(ObservableExam exam) {
        logger.debug("Entering createExerciseExam()");
        mainFrameController.handleCreateExerciseExam(exam);
    }

    @FXML public void startExamButtonClicked(){
        logger.debug("Button Start exam clicked");
        mainFrameController.handleStartExam(tableExam.getSelectionModel().getSelectedItem());
    }

    @FXML public void showQuestions() {
        this.questionList = new ArrayList<>();
        logger.debug("Entering showQuestions()");
        if (this.exerciseExam != null) {
            tryShowQuestions();

        } else {
            logger.error("No ExerciseExam was selected");
            showAlert("Please select an ExerciseExam first");
        }
    }

    public List<Question> getQuestionList() {
        return this.questionList;
    }

    private void initializeTable() throws ServiceException {
        ObservableList<ExerciseExam> exerciseExamObservableList =
            FXCollections.observableArrayList(this.examService.getExams());
        columnExamID.setCellValueFactory(new PropertyValueFactory<>("examid"));
        columnCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        columnPassed.setCellValueFactory(new PropertyValueFactory<>("passed"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        tableExam.setItems(exerciseExamObservableList);

        tableExam.getSelectionModel().selectedItemProperty()
            .addListener((observableValue, oldValue, newValue) -> {
                if (tableExam.getSelectionModel().getSelectedItem() != null) {
                    TableView.TableViewSelectionModel selectionModel =
                        tableExam.getSelectionModel();
                    ObservableList selectedCells = selectionModel.getSelectedCells();
                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                    Object val =
                        tablePosition.getTableView().getItems().get(tablePosition.getRow());
                    exerciseExam = (ExerciseExam) val;
                }
            });
    }

    private void tryShowQuestions() {
        List<Integer> questionIDList;
        try {
            questionIDList = this.examService.getAllQuestionsOfExam(this.exerciseExam.getExamid());
            for (int e : questionIDList) {
                questionList.add(questionService.getQuestion(e));
            }
            mainFrameController.handleShowQuestions();
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }

    }

    private void showAlert(String contentMsg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("An error has occured");
        alert.setContentText(contentMsg);
        alert.showAndWait();
    }
}
