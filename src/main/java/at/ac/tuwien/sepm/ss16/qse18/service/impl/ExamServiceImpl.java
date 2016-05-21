package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang Haixiang
 */
@Service
public class ExamServiceImpl implements ExamService {
    private ExamDao examDao;
    private SubjectQuestionDao subjectQuestionDao;
    private ExamQuestionDao examQuestionDao;
    private QuestionDao questionDao;
    private static final Logger logger = LogManager.getLogger();


    @Autowired public ExamServiceImpl(ExamDao examDao, SubjectQuestionDao subjectQuestionDao,
        ExamQuestionDao examQuestionDao, QuestionDao questionDao) {
        this.examDao = examDao;
        this.subjectQuestionDao = subjectQuestionDao;
        this.examQuestionDao = examQuestionDao;
        this.questionDao = questionDao;
    }


    @Override public Exam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameters {}", examID);
        if(examID <= 0){
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid Exam ID, please check your input");
        }

        try {
           return this.examDao.getExam(examID);
        }catch (DaoException e){
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public List<Exam> getExams() throws ServiceException {
        logger.debug("entering method getExams()");
        try{
            return this.examDao.getExams();

        }catch (DaoException e){
            logger.error("Service Exception getExams");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Exam createExam(Exam exam, Topic topic, int examTime) throws ServiceException {
        logger.debug("entering method createExam with parameters {}", exam);
        if(!DTOValidator.validate(exam)){
            logger.error("Service Exception createExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try{
            exam.setExamQuestions(getRightQuestions(exam, topic.getTopicId(), examTime));
            return this.examDao.create(exam, exam.getExamQuestions());

        }catch (DaoException e){
            logger.error("Service Exception createExam {}", exam);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Exam deleteExam(Exam exam) throws ServiceException {
        logger.debug("entering method deleteExam with parameters {}", exam);
        if(!DTOValidator.validate(exam)){
            logger.error("Service Exception deleteExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try{
            return this.examDao.delete(exam);

        }catch (DaoException e){
            logger.error("Service Exception deleteExam {}", exam, e);
            throw new ServiceException(e.getMessage());
        }
    }

    public Boolean deletedSubject(int subjectID) throws ServiceException{
        /*
        logger.debug("entering method deletedSubject with parameters {}", subjectID);
        List<Exam> examList = new ArrayList<>();
        List<Integer> questionIDList = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();

        if(subjectID <= 0){
            logger.error("Service Exception getExam {}", subjectID);
            throw new ServiceException("Invalid subject ID, please check your input");
        }

        try{
            examList = this.examDao.getAllExamsOfSubject(subjectID);
            for(Exam e: examList){
                questionIDList = this.examQuestionDao.getAllQuestionsOfExam(e.getExamid());
                map.put(e.getExamid(), questionIDList);
                this.examDao.delete(e);
            }

            for(Map.Entry e: map.entrySet()){
                for(int i: (List<Integer>)e.getValue()){
                    this.questionDao.deleteQuestion(this.questionDao.getQuestion(i));
                }
            }

        }catch (DaoException e){
            logger.error("Service Exception getAllExamsOfSubject {}", subjectID, e);
            throw new ServiceException(e.getMessage());
        }
        */

        return true;

    }

    public List<Question> getRightQuestions(Exam exam, int topicID, int examTime) throws ServiceException{
        logger.debug("entering method getRigthQuestions with parameters {}", exam, topicID, examTime);
        if(exam == null || topicID <= 0 || examTime <= 0){
            logger.error("Service Exception getRightQuestions {}", exam, topicID, examTime);
            throw new ServiceException("Invalid values, please check your input");
        }

        int counter = 0;
        long questionTimeCounter = 0;
        int random;
        List<Question> examQuestions = new ArrayList<>();
        List<Integer> notAnsweredQuestionID = new ArrayList<>();
        List<Question> wrongAnsweredQuestions = new ArrayList<>();
        List<Question> rightAnsweredQuestions = new ArrayList<>();
        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        List<Question> notAnsweredQuestions = new ArrayList<>();

        try {
            notAnsweredQuestionID = this.subjectQuestionDao.getAllQuestionsOfSubject(exam, topicID);
            questionBooleans = this.examQuestionDao.getAllQuestionBooleans(notAnsweredQuestionID);

            for(int e: notAnsweredQuestionID){
                notAnsweredQuestions.add(this.questionDao.getQuestion(e));
            }


            List<Question> temp = new ArrayList<>();
            for(Question q: notAnsweredQuestions){
                temp.add(q);
            }


            for(Map.Entry<Integer, Boolean> e: questionBooleans.entrySet()) {
                for(int i = 0; i < notAnsweredQuestions.size(); i++){
                    if(e.getKey() == notAnsweredQuestions.get(i).getQuestionId()){
                        if (e.getValue() == true) {
                            rightAnsweredQuestions.add(temp.get(counter));
                            notAnsweredQuestions.remove(temp.get(counter));
                        } else {
                            wrongAnsweredQuestions.add(temp.get(counter));
                            notAnsweredQuestions.remove(temp.get(counter));
                        }
                        counter++;
                    }
                }
            }

            while((wrongAnsweredQuestions.size() != 0 || notAnsweredQuestions.size() != 0
                || rightAnsweredQuestions.size()!= 0)){

                if(notAnsweredQuestions.size() != 0){
                    random = (int)(Math.random() * notAnsweredQuestions.size());
                    if((questionTimeCounter + notAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) {
                        examQuestions.add(notAnsweredQuestions.get(random));
                        questionTimeCounter += notAnsweredQuestions.get(random).getQuestionTime();
                    }
                    notAnsweredQuestions.remove(random);
                }

                if(wrongAnsweredQuestions.size() != 0 && notAnsweredQuestions.size() == 0){
                    random = (int)(Math.random() * wrongAnsweredQuestions.size());
                    if((questionTimeCounter + wrongAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) {
                        examQuestions.add(wrongAnsweredQuestions.get(random));
                        questionTimeCounter += wrongAnsweredQuestions.get(random).getQuestionTime();
                    }

                    wrongAnsweredQuestions.remove(random);
                }


                if(rightAnsweredQuestions.size() != 0 && notAnsweredQuestions.size() == 0
                    && wrongAnsweredQuestions.size() == 0){
                    random = (int)(Math.random() * rightAnsweredQuestions.size());
                    if((questionTimeCounter + rightAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) {
                        examQuestions.add(rightAnsweredQuestions.get(random));
                        questionTimeCounter += rightAnsweredQuestions.get(random).getQuestionTime();
                    }
                    rightAnsweredQuestions.remove(random);
                }
            }


        }catch (DaoException e){
            logger.error("Service Exception getRightQuestions with parameters{}", exam, topicID, examTime);
            throw new ServiceException(e.getMessage());
        }

        if(examQuestions.size() == 0){
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException("Could not get Questions with subjectId "
                + exam.getSubjectID() + " and topicID " + topicID + " or examTime " + examTime
                + " is too small");
        }

        return examQuestions;
    }

    @Override public List<Integer> getAllQuestionsOfExam(int examID) throws ServiceException{
        logger.debug("entering getAllQuestionsOfExam with parameters {}", examID);

        if(examID <= 0){
            logger.error("Service Exception getAllQuestionsOfExam with parameters {}", examID);
            throw new ServiceException("Invalid examID, please check your input");
        }

        try{
            return this.examQuestionDao.getAllQuestionsOfExam(examID);

        }catch (DaoException e){
            logger.error("Service Exception getAllQuestionsOfExam with parameters {}", examID, e);
            throw new ServiceException(e.getMessage());
        }
    }

}
