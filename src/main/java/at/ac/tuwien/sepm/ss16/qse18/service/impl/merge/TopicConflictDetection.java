package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class TopicConflictDetection {

    private static final Logger logger = LogManager.getLogger();
    private Subject existingSubject;
    private ExportSubject importedSubject;

    private SubjectTopicDao subjectTopicDao;

    private ApplicationContext applicationContext;

    @Autowired public TopicConflictDetection(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setSubjects(Subject existingSubject, ExportSubject importedSubject) {
        this.existingSubject = existingSubject;
        this.importedSubject = importedSubject;
    }

    @Autowired public void setSubjectTopicDao(SubjectTopicDao subjectTopicDao) {
        this.subjectTopicDao = subjectTopicDao;
    }

    public List<TopicConflict> getConflictingTopics() throws ServiceException {
        List<Topic> existingTopics = getExistingTopics();
        List<TopicConflict> duplicates = new ArrayList<>();
        for (Topic existingTopic : existingTopics)
            compareWithImportedTopics(duplicates, existingTopic);
        return duplicates;
    }

    private void compareWithImportedTopics(List<TopicConflict> duplicates, Topic existingTopic)
        throws ServiceException {
        List<ExportTopic> importedTopics = importedSubject.getTopics();
        for (ExportTopic importedTopic : importedTopics) {
            if (checkIfDuplicate(existingTopic, importedTopic)) {
                createTopicConflict(duplicates, existingTopic, importedTopic);
                break;
            }
        }
    }

    private void createTopicConflict(List<TopicConflict> duplicates, Topic existingTopic,
        ExportTopic importedTopic) throws ServiceException {
        TopicConflict topicConflict = applicationContext.getBean(TopicConflict.class);
        topicConflict.setTopics(existingTopic, importedTopic);
        List<QuestionConflict> questionConflicts = topicConflict.initializeQuestionConflicts();
        if (!questionConflicts.isEmpty()) {
            duplicates.add(topicConflict);
        }
    }

    private boolean checkIfDuplicate(Topic existingTopic, ExportTopic importedTopic) {
        String existingTopicName = existingTopic.getTopic();
        String importedTopicName = importedTopic.getTopic().getTopic();
        if (existingTopicName.equals(importedTopicName)) {
            return true;
        }
        return false;
    }

    private List<Topic> getExistingTopics() throws ServiceException {
        try {
            return subjectTopicDao.getTopicToSubject(existingSubject);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException("Could not read topics for existingSubject.");
        }
    }

}
