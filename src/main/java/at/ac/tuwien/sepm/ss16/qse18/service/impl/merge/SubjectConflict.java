package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class SubjectConflict {

    private static final Logger logger = LogManager.getLogger();

    private Subject existingSubject;
    private ExportSubject importedSubject;
    private TopicConflictDetection topicConflictDetection;
    private List<TopicConflict> topicConflicts;

    public String getSubjectName() {
        return existingSubject.getName();
    }

    @Autowired
    public void setTopicConflictDetection(TopicConflictDetection topicConflictDetection) {
        this.topicConflictDetection = topicConflictDetection;
    }

    /**
     * Populates subject conflict
     * @param existingSubject The existing subject causing a conflict
     * @param importedSubject The imported subject causing a conflict
     * @throws ServiceException
     */
    public void setSubjects(Subject existingSubject, ExportSubject importedSubject)
        throws ServiceException {
        this.existingSubject = existingSubject;
        this.importedSubject = importedSubject;
        initializeSubConflicts();
    }

    public List<TopicConflict> getConflictingTopics() throws ServiceException {
        logger.debug("Returning topic conflicts {}", topicConflicts);
        return topicConflicts;
    }

    public boolean isDuplicate() {
        return topicConflicts.stream()
            .allMatch(p -> p.getResolution() == ConflictResolution.DUPLICATE);
    }

    private void initializeSubConflicts() throws ServiceException {
        topicConflictDetection.setSubjects(existingSubject, importedSubject);
        topicConflicts = topicConflictDetection.getConflictingTopics();
    }

    Subject getExistingSubject() {
        return existingSubject;
    }

    boolean isResolved() {
        logger.debug("Checking resolution for {}", existingSubject);
        return !topicConflicts.stream()
            .anyMatch(p -> p.getResolution() == ConflictResolution.UNRESOLVED);
    }

    List<ExportTopic> getNonConflictingImported() {
        List<ExportTopic> nonConflictingImported = new ArrayList<>();
        topicConflicts.forEach(p -> nonConflictingImported.remove(p.getImportedTopic()));
        return nonConflictingImported;
    }


    void setTopicConflicts(List<TopicConflict> topicConflicts) {
        this.topicConflicts = topicConflicts;
    }
}
