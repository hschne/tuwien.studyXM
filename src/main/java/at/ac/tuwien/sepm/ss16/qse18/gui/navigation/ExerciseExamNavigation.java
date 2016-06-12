package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class ExerciseExamNavigation
    extends SubviewNavigation {

    @Autowired
    public ExerciseExamNavigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }
}
