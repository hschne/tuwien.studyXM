package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableQuestion;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class QuestionItemController implements GuiController {

    @FXML public Node root;

    @FXML public TextField questionText;

    @FXML public Node relatedAnswers;

    private ObservableQuestion question;

    private Stage primaryStage;

    @Override public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setQuestion(ObservableQuestion question) {
        this.question = question;
    }

    public void loadFields() {
        questionText.setText(question.getQuestion());
    }

    public Node getRoot() {
        return root;
    }
}