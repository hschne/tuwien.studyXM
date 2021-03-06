package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller of the exam window, in which all exams of the database are displayed
 *
 * @author Zhang Haixiang
 */

@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class CreateExamController
    implements GuiController {
    @FXML public Button buttonShowQuestions;
    @FXML public Button buttonNewExam;
    @FXML public TableView<Exam> tableExam;
    @FXML public TableColumn<Exam, Integer> columnExamID;
    @FXML public TableColumn<Exam, Timestamp> columnCreated;
    @FXML public TableColumn<Exam, Boolean> columnPassed;
    @FXML public TableColumn<Exam, String> columnAuthor;
    @Autowired ExamServiceImpl examService;
    @Autowired QuestionService questionService;
    @Autowired MainFrameController mainFrameController;
    private Logger logger = LogManager.getLogger(CreateExamController.class);
    private AlertBuilder alertBuilder;
    private Exam exam;
    private List<Question> questionList = new ArrayList<>();

    @Autowired public CreateExamController(AlertBuilder alertBuilder) {
        this.alertBuilder = alertBuilder;
    }

    @FXML public void initialize() {
        this.exam = null;
        try {
            initializeTable();
        } catch (ServiceException e) {
            logger.error("Could not fill exam-table: ", e);
            Alert alert =
                alertBuilder.alertType(Alert.AlertType.ERROR).title("Error").setResizable(true)
                    .headerText("Could not fill exam-table.")
                    .contentText("Please make sure you are connected to the database").build();
            alert.showAndWait();
        }
    }

    @FXML public void insertExamValues() {
        logger.debug("Entering insertExamValues()");
        mainFrameController.handleCreateExam();
    }

    @FXML public void showQuestions() {
        this.questionList = new ArrayList<>();
        logger.debug("Entering showQuestions()");
        if (this.exam != null) {
            tryShowQuestions();

        } else {
            logger.error("No Exam was selected");
            showAlert("Please select an Exam first");
        }
    }

    public List<Question> getQuestionList() {
        return this.questionList;
    }

    private void initializeTable() throws ServiceException {
        ObservableList<Exam> examObservableList =
            FXCollections.observableArrayList(this.examService.getExams());
        columnExamID.setCellValueFactory(new PropertyValueFactory<>("examid"));
        columnCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        columnPassed.setCellValueFactory(new PropertyValueFactory<>("passed"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        tableExam.setItems(examObservableList);

        tableExam.getSelectionModel().selectedItemProperty()
            .addListener((observableValue, oldValue, newValue) -> {
                if (tableExam.getSelectionModel().getSelectedItem() != null) {
                    TableView.TableViewSelectionModel selectionModel =
                        tableExam.getSelectionModel();
                    ObservableList selectedCells = selectionModel.getSelectedCells();
                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                    Object val =
                        tablePosition.getTableView().getItems().get(tablePosition.getRow());
                    exam = (Exam) val;
                }
            });
    }

    private void tryShowQuestions() {
        List<Integer> questionIDList;
        try {
            questionIDList = this.examService.getAllQuestionsOfExam(this.exam.getExamid());
            for (int e : questionIDList) {
                questionList.add(questionService.getQuestion(e));
            }
            mainFrameController.handleShowQuestions();
        } catch (ServiceException e) {
            logger.error(e);
            alertBuilder.alertType(Alert.AlertType.ERROR).
                title("Error").headerText("Could load questions for exam")
                .contentText(e.getMessage()).build().showAndWait();
        }

    }

    private void showAlert(String contentMsg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Such Input, Much Wow");
        alert.setContentText(contentMsg);
        alert.showAndWait();
    }
}
