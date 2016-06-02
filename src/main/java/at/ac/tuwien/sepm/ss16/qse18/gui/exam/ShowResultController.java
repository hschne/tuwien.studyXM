package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author  Zhang Haixiang on 02.06.2016.
 */
@Component public class ShowResultController extends BaseController{
    @Autowired private ExamServiceImpl examService;
    @FXML public PieChart pieChart;
    @FXML public BarChart barChart;

    @FXML public Button buttonFinish;

    @FXML public Label labelGrade;


    public ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML public void initialize(){
        String[] result = new String[3];
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


        }catch (Exception e){

        }
    }


}
