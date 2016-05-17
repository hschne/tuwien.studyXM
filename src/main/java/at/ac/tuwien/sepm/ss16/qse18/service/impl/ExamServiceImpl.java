package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang Haixiang
 */
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
            logger.error("Service Exception deleteExam {}", exam);
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Question> getRightQuestions(Exam exam, int topicID, int examTime) throws ServiceException{
        int counter = 0;
        long questionTimeCounter = 0;
        int random;
        List<Question> examQuestions = new ArrayList<>();
        List<Integer> notAnsweredQuestionID = new ArrayList<>();
        List<Question> wrongAnsweredQuestions = new ArrayList<>();
        List<Question> rightAnsweredQuestions = new ArrayList<>();
        List<Boolean> questionBooleans = new ArrayList<>();
        List<Question> notAnsweredQuestions = new ArrayList<>();

        try {
            notAnsweredQuestionID = this.subjectQuestionDao.getAllQuestionsOfSubject(exam, topicID);
            questionBooleans = this.examQuestionDao.getAllQuestionBooleans(notAnsweredQuestionID);

            for(int e: notAnsweredQuestionID){
                notAnsweredQuestions.add(this.questionDao.getQuestion(e));
            }


            for(Boolean e: questionBooleans){
                if(e == true){
                    rightAnsweredQuestions.add(notAnsweredQuestions.get(counter));
                    notAnsweredQuestions.remove(counter);
                }else{
                    wrongAnsweredQuestions.add(notAnsweredQuestions.get(counter));
                    notAnsweredQuestions.remove(counter);
                    }
                }
                counter++;
            System.out.println(wrongAnsweredQuestions.size());
            System.out.println(rightAnsweredQuestions.size());
            System.out.println(questionBooleans.size());

            while((wrongAnsweredQuestions.size() != 0 || notAnsweredQuestions.size() != 0
                || rightAnsweredQuestions.size()!= 0)){

                if(wrongAnsweredQuestions.size() != 0){
                    random = (int)(Math.random() * wrongAnsweredQuestions.size());
                    if((questionTimeCounter + wrongAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) {
                        examQuestions.add(wrongAnsweredQuestions.get(random));
                        questionTimeCounter += wrongAnsweredQuestions.get(random).getQuestionTime();
                    }

                    wrongAnsweredQuestions.remove(random);
                }

                if(wrongAnsweredQuestions.size() == 0 && notAnsweredQuestions.size() != 0){
                    random = (int)(Math.random() * notAnsweredQuestions.size());
                    if((questionTimeCounter + notAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) {
                        examQuestions.add(notAnsweredQuestions.get(random));
                        questionTimeCounter += notAnsweredQuestions.get(random).getQuestionTime();
                    }
                    notAnsweredQuestions.remove(random);
                }

                else{
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
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException(e.getMessage());
        }

        return examQuestions;
    }

}
