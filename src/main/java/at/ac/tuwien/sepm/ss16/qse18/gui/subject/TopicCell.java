package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * @author Philipp Ganiu
 */
public class TopicCell extends ListCell<ObservableTopic> {
    private HBox box = new HBox(10);
    private Label label = new Label();
    private Button button = new Button();

    public TopicCell(){
        super();
        box.getChildren().addAll(label,button);
    }


    @Override
    public void updateItem(ObservableTopic topic, boolean empty){
        super.updateItem(topic, empty);

        if(topic != null){
            label.setText(topic.getTopic());
            button.setText("+");
            button.setOnAction(e -> {/*TODO implement what button does*/});
            setGraphic(box);
        }
        else{
            setGraphic(null);
        }
    }
}
