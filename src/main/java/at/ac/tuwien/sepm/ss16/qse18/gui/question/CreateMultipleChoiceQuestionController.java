package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Julian on 15.05.2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)public class CreateMultipleChoiceQuestionController
        implements GuiController{

    private Stage primaryStage;

    @Override
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
