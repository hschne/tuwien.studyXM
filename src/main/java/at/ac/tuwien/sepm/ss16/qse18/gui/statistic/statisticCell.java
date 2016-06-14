package at.ac.tuwien.sepm.ss16.qse18.gui.statistic;

import at.ac.tuwien.sepm.ss16.qse18.gui.FxmlLoadException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.ListCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class represents a custom list item in the statistic overview list.
 *
 * @author Julian Strohmayer
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) class StatisticCell
        extends ListCell<ObservableSubject> {

    @Autowired SpringFXMLLoader springFXMLLoader;

    private static final Logger logger = LogManager.getLogger();

    @Override public void updateItem(ObservableSubject subject, boolean empty) {
        super.updateItem(subject, empty);
        if (subject != null) {
            StatisticItemController itemController = getController();
            setControllerProperties(subject, itemController);
        }
    }

    private void setControllerProperties(ObservableSubject subject,
                                         StatisticItemController itemController) {
        itemController.initialize(subject);
        try {
            itemController.loadFields();
        } catch (ServiceException e) {
            logger.error(e);
            //TODO: Errorhandling
        }
        setGraphic(itemController.getRoot());
    }

    private StatisticItemController getController() {
        SpringFXMLLoader.FXMLWrapper<Object, StatisticItemController> statisticItemWrapper;
        try {
            statisticItemWrapper = springFXMLLoader
                    .loadAndWrap("/fxml/statistic/statisticItem.fxml", StatisticItemController.class);
            return statisticItemWrapper.getController();
        } catch (IOException e) {
            logger.error(e);
            throw new FxmlLoadException("Error loading subject item", e);
        }
    }
}
