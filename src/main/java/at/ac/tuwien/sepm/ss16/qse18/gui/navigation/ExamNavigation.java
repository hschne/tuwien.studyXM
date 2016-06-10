package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.ss16.qse18.gui.exam.NewExamController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ExamNavigation extends SubviewNavigation {

    @Autowired
    public ExamNavigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }

    public void handleNewExam() {
        logger.debug("Loading new exam view");
        try {
            setSubView("/fxml/exam/newExam.fxml", NewExamController.class);
        } catch (Exception e) {
            handleException(e);
        }
    }
}
