package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class SubjectConflict {

    private Subject existingSubject;
    private ExportSubject importedSubject;
    private TopicConflictDetection topicConflictDetection;
    private List<TopicConflict> topicConflicts;

    @Autowired
    public void setTopicConflictDetection(TopicConflictDetection topicConflictDetection) {
        this.topicConflictDetection = topicConflictDetection;
    }

    public void initialize(Subject existingSubject, ExportSubject importedSubject)
        throws ServiceException {
        this.existingSubject = existingSubject;
        this.importedSubject = importedSubject;
        initializeSubConflicts();
    }

    public List<TopicConflict> getConflictingTopics() throws ServiceException {

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
        // If no more topics are unresolved, the subject conflict is resolved
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
