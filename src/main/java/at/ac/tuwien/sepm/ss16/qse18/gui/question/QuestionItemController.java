package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component public class QuestionItemController extends BaseController {

    @FXML public Node root;
    @FXML public TextField questionText;
    @FXML public Node relatedAnswers;
    private ObservableQuestion question;

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
