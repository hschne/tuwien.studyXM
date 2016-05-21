package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zhang Haixiang
 *
 */

@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class CreateExamController
    implements GuiController {
    private Stage primaryStage;
    private Logger logger = LoggerFactory.getLogger(CreateExamController.class);
    private SpringFXMLLoader springFXMLLoader;
    private AlertBuilder alertBuilder;
    private ConnectionH2 database = new ConnectionH2();
    private Exam exam;
    private List<Question> questionList = new ArrayList<>();

    @Autowired ExamServiceImpl examService;
    @Autowired QuestionService questionService;

    private ObservableList<Exam> examObservableList;

    @FXML public Button buttonShowQuestions;
    @FXML public Button buttonNewExam;

    @FXML public TableView<Exam> tableExam;
    @FXML public TableColumn<Exam, Integer> columnExamID;
    @FXML public TableColumn<Exam, Timestamp> columnCreated;
    @FXML public TableColumn<Exam, Boolean> columnPassed;
    @FXML public TableColumn<Exam, String> columnAuthor;


    @Autowired MainFrameController mainFrameController;

    @Autowired
    public CreateExamController(SpringFXMLLoader springFXMLLoader, AlertBuilder alertBuilder) {
        this.springFXMLLoader = springFXMLLoader;
        this.alertBuilder = alertBuilder;
    }

    @FXML public void initialize() {
        this.exam = null;
        try {
            examObservableList = FXCollections.observableArrayList(this.examService.getExams());
            columnExamID.setCellValueFactory(new PropertyValueFactory<>("examid"));
            columnCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
            columnPassed.setCellValueFactory(new PropertyValueFactory<>("passed"));
            columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            tableExam.setItems(examObservableList);

            tableExam.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                    if(tableExam.getSelectionModel().getSelectedItem() != null)
                    {
                        TableView.TableViewSelectionModel selectionModel = tableExam.getSelectionModel();
                        ObservableList selectedCells = selectionModel.getSelectedCells();
                        TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                        Object val = tablePosition.getTableView().getItems().get(tablePosition.getRow());
                        exam = (Exam) val;
                    }
                }
            });

        } catch (ServiceException e) {
            logger.error("Could not fill exam-table: " + e.getMessage());
            alertBuilder.alertType(Alert.AlertType.ERROR).headerText("Error")
                .contentText("Could not fill exam-table.").build();
        }
    }

    @FXML public void insertExamValues() throws Exception {
        logger.debug("Entering insertExamValues()");
        mainFrameController.handleCreateExam();
    }

    @FXML public void showQuestions() throws Exception {
        this.questionList = new ArrayList<>();
        logger.debug("Entering showQuestions()");
        List<Integer> questionIDList = new ArrayList<>();
        logger.debug("Entering showQuestions");
        if(this.exam != null){
            questionIDList = this.examService.getAllQuestionsOfExam(this.exam.getExamid());

            for(int e: questionIDList) {
                questionList.add(questionService.getQuestion(e));
            }

            mainFrameController.handleShowQuestions();

        }else{
            logger.error("No Exam was selected");
            showAlert("Please select an Exam first");
        }
    }

    @Override public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public List<Question> getQuestionList(){
        return this.questionList;
    }

    private void showAlert(String contentMsg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Such Input, Much Wow");
        alert.setContentText(contentMsg);
        alert.showAndWait();
    }
}
