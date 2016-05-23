package at.ac.tuwien.sepm.ss16.qse18.gui.topic;

import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Philipp Ganiu
 */
public class TopicCell extends ListCell<ObservableTopic> {
    private HBox box = new HBox(10);
    private Label topicLable = new Label();
    private Button button = new Button();
    private Label numberOfQuestions = new Label();

    private MainFrameController mainFrameController;

    public TopicCell(){
        super();
        box.getChildren().addAll(topicLable,numberOfQuestions,button);
    }

    public void setMainFrameController(MainFrameController mainFrameController){
        this.mainFrameController = mainFrameController;
    }


    @Override
    public void updateItem(ObservableTopic topic, boolean empty){
        super.updateItem(topic, empty);

        if(topic != null){
            topicLable.setText(topic.getTopic());
            numberOfQuestions.setText(topic.getNumberOfQuestions());
            button.setText("+");
            button.setOnAction(e -> {mainFrameController.handleCreateQuestion(topic.getT());});
            setGraphic(box);
        }
        else{
            setGraphic(null);
        }
    }
}
