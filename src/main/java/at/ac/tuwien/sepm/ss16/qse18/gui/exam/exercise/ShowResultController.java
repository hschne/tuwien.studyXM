package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExerciseExamServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  Zhang Haixiang on 02.06.2016.
 */
@Component public class ShowResultController extends BaseController{
    @Autowired private ExerciseExamServiceImpl exerciseExamService;
    @Autowired private QuestionService questionService;
    @Autowired PostExerciseExamOverviewController postExerciseExamOverviewController;
    @FXML private PieChart pieChart;
    @FXML private BarChart barChart;

    @FXML private Button buttonFinish;
    @FXML private Button showDetailsButton;

    @FXML private Label labelGrade;

    @FXML private CategoryAxis xAxis = new CategoryAxis();
    @FXML private NumberAxis yAxis = new NumberAxis();

    private ExerciseExam exerciseExam;



    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML public void initialize(ExerciseExam exerciseExam){
        logger.debug("entering initialize with parameters {}", exerciseExam);
        this.exerciseExam = exerciseExam;
        pieChart.getData().clear();
        pieChartData.clear();

        String[] result = new String[3];

        Map<Topic, String[]> topicResult = new HashMap<>();

        try {
            result = exerciseExamService.gradeExam(exerciseExam);
            labelGrade.setText("Grading: " + result[2]);
            pieChartData.add(new PieChart.Data("correct " + Integer.parseInt(result[0]), Integer.parseInt(result[0])));
            pieChartData.add(new PieChart.Data("false " + Integer.parseInt(result[1]), Integer.parseInt(result[1])));
            pieChart.setTitle("correct/false answers");
            pieChart.setTitleSide(Side.BOTTOM);
            pieChart.setData(pieChartData);
            pieChart.setLegendVisible(false);

            pieChartData.get(0).getNode().setStyle("-fx-pie-color: #A2E88B");
            pieChartData.get(1).getNode().setStyle("-fx-pie-color: #F7A099");


            yAxis.setLabel("answered questions");
            barChart.setTitle("performance in subject areas");
            barChart.setTitleSide(Side.BOTTOM);


            topicResult = exerciseExamService.topicGrade(exerciseExam);
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("false");

            for(Map.Entry<Topic, String[]> m: topicResult.entrySet()){
                XYChart.Data data = new XYChart.Data(m.getKey().getTopic(), Integer.parseInt(m.getValue()[1]));
                series1.getData().add(data);
            }


            XYChart.Series series2 = new XYChart.Series();
            series2.setName("correct");

            for(Map.Entry<Topic, String[]> f: topicResult.entrySet()){
                series2.getData().add(new XYChart.Data(f.getKey().getTopic(),  Integer.parseInt(f.getValue()[0])));
            }

            barChart.getData().addAll(series1, series2);


        }catch (ServiceException e){
            logger.error("Service Exception initialize {}", exerciseExam, e);
            showError(e);
        }
    }

    public void showDetail(){
        logger.debug("entering showDetail()");
        mainFrameController.handleShowDetail();
        if(this.exerciseExam.getExamQuestions().size() == 0) {
            setExamQuestions();
        }

        postExerciseExamOverviewController.initialize(this.exerciseExam);

    }

    public void finish(){
        logger.debug("entering finish()");
        mainFrameController.handleHome();
    }

    private void setExamQuestions(){
        try {
            for (int i : exerciseExamService.getAllQuestionsOfExam(this.exerciseExam.getExamid())) {
                this.exerciseExam.getExamQuestions().add(questionService.getQuestion(i));
            }
        }catch (ServiceException e){
            logger.error("Service Exception setExamQuestions()", e);
            showError(e.getMessage());
        }
    }

}
