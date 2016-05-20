package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zhang Haixiang
 */

@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class CreateExamController
    implements GuiController {
    private Stage primaryStage;
    private Logger logger = LoggerFactory.getLogger(CreateExamController.class);
    private SpringFXMLLoader springFXMLLoader;
    private AlertBuilder alertBuilder;
    private ConnectionH2 database = new ConnectionH2();
    @Autowired ExamServiceImpl examService;

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
        try {
            examObservableList = FXCollections.observableArrayList(this.examService.getExams());
            columnExamID.setCellValueFactory(new PropertyValueFactory<>("examid"));
            columnCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
            columnPassed.setCellValueFactory(new PropertyValueFactory<>("passed"));
            columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            tableExam.setItems(examObservableList);
        } catch (ServiceException e) {
            logger.error("Could not fill exam-table: " + e.getMessage());
            Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).headerText("Error")
                .contentText("Could not fill exam-table.").build();
        }
    }

    @FXML public void insertExamValues() throws Exception {
        logger.debug("Entering insertExamValues()");
        mainFrameController.handleCreateExam();
    }

    @FXML public void showQuestions() throws Exception {
        logger.debug("Entering showQuestions()");
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/exam/showQuestions.fxml"));
        primaryStage.setTitle("Show Questions");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Override public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
