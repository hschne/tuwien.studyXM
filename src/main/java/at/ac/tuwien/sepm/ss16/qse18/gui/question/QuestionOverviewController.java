package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class QuestionOverviewController implements GuiController {


    private Stage primaryStage;

    @Override public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void init() {


    }
}
