package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ExportService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class exports data
 *
 * @author Hans-Joerg Schroedl
 */
@Service public class ExportServiceImpl implements ExportService {

    private static final Logger logger = LogManager.getLogger();

    private Subject subject;

    private SubjectTopicDao subjectTopicDao;
    private QuestionTopicDao questionTopicDao;
    private ResourceQuestionDao resourceQuestionDao;

    @Autowired public void setTopicDao(QuestionTopicDao questionTopicDao) {
        this.questionTopicDao = questionTopicDao;
    }

    @Autowired public void setSubjectTopicDao(SubjectTopicDao subjectTopicDao) {
        this.subjectTopicDao = subjectTopicDao;
    }


    @Override public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override public void export() throws ServiceException {
        try {
            List<Topic> topics = subjectTopicDao.getTopicToSubject(subject);
            List<Question> questions = getQuestions(topics);
            serializeSubject();
            serializeTopics(topics);
            serializeQuestions(questions);
            //List<Resource> resources = getResources(questions);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }


    @Autowired public void setResourceQuestionDao(ResourceQuestionDao resourceQuestionDao) {
        this.resourceQuestionDao = resourceQuestionDao;
    }

    private List<Question> getQuestions(List<Topic> topics) throws ServiceException {
        List<Question> allQuestions = new ArrayList<>();
        try {
            for (Topic topic : topics) {
                allQuestions.addAll(questionTopicDao.getQuestionToTopic(topic));
            }
            return allQuestions;
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }


    private List<Resource> getResources(List<Question> questions) throws ServiceException {
        List<Resource> allResources = new ArrayList<>();
        try {
            for (Question question : questions) {
                Resource resource = resourceQuestionDao.getResourceOfQuestion(question);
                allResources.add(resource);
            }
            return allResources;
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    private void serializeSubject(){
        try {
            FileOutputStream output = new FileOutputStream("./subject.data");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject(subject);
            objectOutputStream.close();
            output.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void serializeQuestions(List<Question> questions) {
        try {
            FileOutputStream output = new FileOutputStream("./questions.data");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject((Serializable) questions);
            objectOutputStream.close();
            output.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void serializeTopics(List<Topic> topics){
        try {
            FileOutputStream output = new FileOutputStream("./topics.data");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject((Serializable) topics);
            objectOutputStream.close();
            output.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

}
