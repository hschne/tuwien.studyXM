package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ImportUtil;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class SubjectMerge {

    private ImportService importService;
    private TopicMerge topicMerge;
    private Logger logger = LogManager.getLogger();

    private ImportUtil importUtil;

    private String databaseErrorMessage = "Error configuring database connection. View logs for details.";

    @Autowired public void setTopicMerge(TopicMerge topicMerge) {
        this.topicMerge = topicMerge;
    }

    @Autowired public void setImportService(ImportService importService) {
        this.importService = importService;
    }

    @Autowired public void setImportUtil(ImportUtil importUtil) {
        this.importUtil = importUtil;
    }


    public void merge(SubjectConflict subjectConflict) throws ServiceException {
        if (!subjectConflict.isResolved()) {
            String message =
                "The conflict has not been resolved. Please make sure that all conflicting questions have a resolution.";
            logger.error(message);
            throw new ServiceException(message);
        }
        importSubject(subjectConflict);
    }


    private void importSubject(SubjectConflict subjectConflict) throws ServiceException {
        setAutocommitFalse();
        try {
            List<ExportTopic> importedWithoutConflict = subjectConflict.getNonConflictingImported();
            for (ExportTopic exportTopic : importedWithoutConflict) {
                importService
                    .importTopic(exportTopic, subjectConflict.getExistingSubject());
            }
            List<TopicConflict> conflictingTopics = subjectConflict.getConflictingTopics();
            for (TopicConflict topicConflict : conflictingTopics) {
                topicMerge.merge(topicConflict);
            }
        } catch (ServiceException e) {
            tryRollback();
            throw e;
        }

        setAutocommitTrue();
    }

    private void tryRollback() throws ServiceException {
        try {
            importUtil.rollback();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(databaseErrorMessage
                , e);
        }
    }

    private void setAutocommitFalse() throws ServiceException {
        try {
            importUtil.setAutocommit(false);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(
                databaseErrorMessage, e);
        }
    }

    private void setAutocommitTrue() throws ServiceException {
        try {
            importUtil.setAutocommit(true);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(
                databaseErrorMessage, e);
        }
    }



}
