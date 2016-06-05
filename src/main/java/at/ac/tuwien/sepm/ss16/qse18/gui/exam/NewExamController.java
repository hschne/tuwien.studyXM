package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Felix on 05.06.2016.
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class NewExamController
    extends BaseController {
    @FXML ListView<ObservableSubject> listSubjects;
    @FXML TextField fieldName;
    @FXML TextField fieldDueDate;
    @FXML Button buttonCancel;
    @FXML Button buttonCreate;

    @FXML public void create() {

    }

    @FXML public void cancel() {

    }
}
