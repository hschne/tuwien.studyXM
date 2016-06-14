package at.ac.tuwien.sepm.ss16.qse18.gui.statistic;

import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.QuestionNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.StatisticService;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.StatisticServiceImpl;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Julian Strohmayer
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticTopicCell extends ListCell<ObservableTopic> {
    private HBox box = new HBox(10);
    private Label TopicLabel = new Label();
    private Label numberOfQuestions = new Label();
    private ProgressBar progressBar = new ProgressBar(0);

    @Autowired StatisticService statisticService;

    StatisticTopicCell() {
        super();
        box.getChildren().addAll(progressBar,numberOfQuestions, TopicLabel);
    }


    @Override
    public void updateItem(ObservableTopic topic, boolean empty) {
        super.updateItem(topic, empty);
        if (topic != null) {
            TopicLabel.setText(topic.getTopic());

            try {
                double[] result = statisticService.getCorrectFalseRatio(topic.getT());
               progressBar.setProgress(result[0]);
               numberOfQuestions.setText(result[1]+"/"+result[2]);
            } catch (ServiceException e) {
                e.printStackTrace();
            }

            setGraphic(box);
        } else {
            setGraphic(null);
        }
    }
}

