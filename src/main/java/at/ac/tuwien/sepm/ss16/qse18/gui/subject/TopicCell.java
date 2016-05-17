package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

/**
 * @author Philipp Ganiu
 */
public class TopicCell extends ListCell<ObservableTopic> {
    private Button actionBtn;
    private GridPane pane;

    public TopicCell() {
        super();
        actionBtn = new Button("+");
        actionBtn.setOnAction(e -> {
                //TODO handle button
        });
        pane = new GridPane();
        setText(null);
    }

    @Override
    public void updateItem(ObservableTopic topic, boolean empty){
        super.updateItem(topic, empty);

        if(topic != null){
            pane.add(new Label(topic.getTopic()),1,1);
            pane.add(actionBtn, 2, 1);
            pane.setHgap(10);
            setGraphic(pane);
        }
        else{
            setGraphic(null);
        }
    }
}
