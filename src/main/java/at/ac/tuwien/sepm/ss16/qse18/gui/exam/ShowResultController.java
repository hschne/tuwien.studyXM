package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
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
    @Autowired private ExamServiceImpl examService;
    @FXML public PieChart pieChart;
    @FXML public BarChart barChart;

    @FXML public Button buttonFinish;

    @FXML public Label labelGrade;

    @FXML public CategoryAxis xAxis = new CategoryAxis();
    @FXML public NumberAxis yAxis = new NumberAxis();



    public ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML public void initialize(){
        String[] result = new String[3];
        String[] test = {"5", "6", "A"};
        String[] test2 = {"7", "9", "B"};
        String[] test3 = {"3", "2", "B"};

        Map<String, String[]> testMap = new HashMap<>();
        testMap.put("topic1", test);
        testMap.put("topic2", test2);
        testMap.put("topic3", test3);

        labelGrade.setText("A");
        try {
            //result = examService.gradeExam(exam);
            pieChartData.add(new PieChart.Data("correct", 30)); // muss dann noch geändert werden wenn eine exam übergeben wird
            pieChartData.add(new PieChart.Data("false", 70));
            pieChart.setTitle("correct/false answers");
            pieChart.setTitleSide(Side.BOTTOM);
            pieChart.setData(pieChartData);
            pieChart.setLegendVisible(false);
            pieChartData.get(0).getNode().setStyle("-fx-pie-color: " + "limegreen");
            pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + "firebrick");


            yAxis.setLabel("answered questions");
            barChart.setTitle("performance in subject areas");
            barChart.setTitleSide(Side.BOTTOM);

            XYChart.Series series1 = new XYChart.Series();
            series1.setName("false");
            for(Map.Entry<String, String[]> m: testMap.entrySet()){
                XYChart.Data data = new XYChart.Data(m.getKey(), Double.parseDouble(m.getValue()[1]));
                //data.getNode().setStyle("-fx-bar-fill: " + "limegreen"); funktioniert iwie nicht
                series1.getData().add(data);
            }


            XYChart.Series series2 = new XYChart.Series();
            series2.setName("correct");
            for(Map.Entry<String, String[]> f: testMap.entrySet()){
                series2.getData().add(new XYChart.Data(f.getKey(),  Double.parseDouble(f.getValue()[0])));
            }


            barChart.getData().addAll(series1, series2);


        }catch (Exception e){

        }
    }

}
