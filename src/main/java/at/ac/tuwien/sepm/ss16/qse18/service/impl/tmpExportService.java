package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportResource;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 14.06.2016.
 */
@Service public class tmpExportService implements ExportService {
    private static final Logger logger = LogManager.getLogger();

    private Subject subject;
    @Autowired private SubjectService subjectService;
    @Autowired private TopicService topicService;
    @Autowired private QuestionService questionService;
    @Autowired private ResourceQuestionDao resourceQuestionDao;

    @Override public boolean export(String outputpath) throws ServiceException {
        if(this.subject == null) {
            throw new ServiceException("No subject selected for export.");
        }

        logger.debug("Exporting subject: {}", this.subject);
        return false;
    }

    @Override public void setSubject(Subject subject) throws ServiceException {
        this.subject = subject;
    }

    private ExportSubject serializeSubject() throws ServiceException {
        List<Topic> topics = getTopics();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        List<Resource> resources = new ArrayList<>();
        List<Note> notes = new ArrayList<>();

        for(Topic t : topics) {
            questions.addAll(getQuestionsFromTopic(t));
        }

        for(Question q : questions) {
            answers.addAll(getAnswersFromQuestion(q));
            resources.add(getResourcesFromQuestion(q));
        }

        return null;
    }

    private ExportTopic serializeTopic(Topic topic, List<ExportQuestion> questions) {
        return new ExportTopic(topic, questions);
    }

    private ExportQuestion serializeQuestion(Question question,
        ExportResource resource, List<Answer> answers) {
        return new ExportQuestion(question, resource, answers);
    }

    private List<Topic> getTopics() throws ServiceException {
        return topicService.getTopicsFromSubject(this.subject);
    }

    private List<Question> getQuestionsFromTopic(Topic topic) throws ServiceException {
        return questionService.getQuestionsFromTopic(topic);
    }

    private List<Answer> getAnswersFromQuestion(Question question) throws ServiceException {
        return questionService.getCorrespondingAnswers(question);
    }

    private Resource getResourcesFromQuestion(Question question) throws ServiceException {
        try {
            return resourceQuestionDao.getResourceOfQuestion(question);
        } catch(DaoException e) {
            logger.error("Could not get resource of question {}", question, e);
            throw new ServiceException("Could not get resource of question.", e);
        }
    }
}
