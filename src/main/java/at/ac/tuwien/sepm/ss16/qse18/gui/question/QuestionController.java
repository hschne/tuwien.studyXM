package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Abstract base class to be used by any controllers for question creation
 *
 * @author Hans-Joerg Schroedl
 */

@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public abstract class QuestionController
    implements GuiController {

    protected final QuestionService questionService;
    protected final AlertBuilder alertBuilder;

    protected ObservableTopic topic;

    @Autowired protected MainFrameController mainFrameController;

    @Autowired
    public QuestionController(QuestionService questionService, AlertBuilder alertBuilder) {
        this.questionService = questionService;
        this.alertBuilder = alertBuilder;
    }

    /**
     * Sets the topic in which the question should be created
     * @param topic The topic
     */
    public void setTopic(ObservableTopic topic) {
        this.topic = topic;
    }

    protected void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occurred").contentText(e.getMessage()).build();
        alert.showAndWait();
    }


    protected void showSuccess(String msg) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).title("Success")
            .headerText("The operation was successful!").contentText(msg).build();
        alert.showAndWait();
    }
}
