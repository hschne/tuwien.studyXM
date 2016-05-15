package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class SubjectItemController {

    @FXML public Node root;

    @FXML Label name;

    @Autowired MainFrameController mainFrameController;

    private ObservableSubject subject;

    public void loadFields() {
        name.setText(subject.getName());
    }


    @FXML public void handleExport() {
        System.out.println("Wow, much export!");
    }

    @FXML public void handleNew() {

    }

    @FXML public void handleDelete() {

    }

    @FXML public void handleEditQuestions(){
        mainFrameController.handleQuestionOverview(subject);
    }

    public void setSubject(ObservableSubject subject) {
        this.subject = subject;
    }

    public Node getRoot() {
        return root;
    }

}
