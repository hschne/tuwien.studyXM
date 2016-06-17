package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
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

    public void initialize(Subject existingSubject, ExportSubject importedSubject) {
        this.existingSubject = existingSubject;
        this.importedSubject = importedSubject;
    }

    public boolean isResolved(){
        return topicConflicts.stream().allMatch(p -> p.isResolved());
    }

    public List<TopicConflict> getConflictingTopics() throws ServiceException {
        topicConflictDetection.setSubjects(existingSubject, importedSubject);
        topicConflicts = topicConflictDetection.getConflictingTopics();
        return topicConflicts;
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
