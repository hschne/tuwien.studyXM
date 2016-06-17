package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
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

    @Autowired public void setTopicMerge(TopicMerge topicMerge) {
        this.topicMerge = topicMerge;
    }

    @Autowired public void setImportService(ImportService importService) {
        this.importService = importService;
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
        List<ExportTopic> importedWithoutConflict = subjectConflict.getNonConflictingImported();
        for (ExportTopic exportTopic : importedWithoutConflict) {
            importService.importTopic(exportTopic.getTopic(), subjectConflict.getExistingSubject());
        }
        List<TopicConflict> conflictingTopics = subjectConflict.getConflictingTopics();
        for (TopicConflict topicConflict : conflictingTopics) {
            topicMerge.merge(topicConflict);
        }
    }




}
