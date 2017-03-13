package at.ac.tuwien.sepm.ss16.qse18.gui.statistic;

import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.StatisticService;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class represents the topic items in the statistics table view
 *
 * @author Julian Strohmayer
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticTopicCell extends ListCell<ObservableTopic>{
    private static final Logger logger = LogManager.getLogger();
    private HBox box = new HBox(10);
    private Label topicLabel = new Label();
    private Label numberOfQuestions = new Label();
    private ProgressBar progressBar = new ProgressBar(0);
    private AlertBuilder alertBuilder;

    @Autowired
    StatisticService statisticService;

    StatisticTopicCell() {
        super();
        box.getChildren().addAll(progressBar, numberOfQuestions, topicLabel);
    }

    @Override
    public void updateItem(ObservableTopic topic, boolean empty){
        super.updateItem(topic, empty);
        if (topic != null) {
            topicLabel.setText(topic.getTopic());

            try {
                double[] result = statisticService.getAnsweredQuestionsForTopic(topic.getT());
                progressBar.setProgress(result[1] / result[2]);

                if (progressBar.getProgress() > 0.66) {
                    progressBar.setStyle("-fx-accent:#A2E88B;");
                } else if (progressBar.getProgress() < 0.33) {
                    progressBar.setStyle("-fx-accent:#F7A099;");
                } else {
                    progressBar.setStyle("-fx-accent:#F0F595;");
                }

                numberOfQuestions.setText((int) result[1] + "/" + (int) result[2]);
            } catch (ServiceException e) {
                logger.error(e);
                showAlert("ERROR","Unable to update item",e.getMessage());
            }
            setGraphic(box);
        } else {
            setGraphic(null);
        }
    }

    private void showAlert(String title, String headerMsg, String contentMsg) {
        Alert alert =
                alertBuilder.alertType(Alert.AlertType.INFORMATION).title(title).headerText(headerMsg)
                        .modality(Modality.APPLICATION_MODAL).contentText(contentMsg).setResizable(true)
                        .build();
        alert.showAndWait();
    }
}

