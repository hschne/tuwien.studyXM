package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExamGenParams;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class ExamServiceImpl
 * concrete implementatioin of ExamService
 *
 * @author Zhang Haixiang
 */
@Service public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LogManager.getLogger();
    private ExamDao examDao;
    private SubjectQuestionDao subjectQuestionDao;
    private ExamQuestionDao examQuestionDao;
    private QuestionDao questionDao;
    private ExamGenParams egp;


    @Autowired public ExamServiceImpl(ExamDao examDao, SubjectQuestionDao subjectQuestionDao,
        ExamQuestionDao examQuestionDao, QuestionDao questionDao) {
        this.examDao = examDao;
        this.subjectQuestionDao = subjectQuestionDao;
        this.examQuestionDao = examQuestionDao;
        this.questionDao = questionDao;
    }


    @Override public Exam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameters {}", examID);
        if (examID <= 0) {
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid Exam ID, please check your input");
        }

        try {
            return this.examDao.getExam(examID);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public List<Exam> getExams() throws ServiceException {
        logger.debug("entering method getExams()");
        try {
            return this.examDao.getExams();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public Exam createExam(Exam exam, Topic topic, int examTime) throws ServiceException {
        logger.debug("entering method createExam with parameters {}", exam);
        if (!DtoValidator.validate(exam)) {
            logger.error("Service Exception createExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try {
            exam.setExamQuestions(getRightQuestions(exam, topic.getTopicId(), examTime));
            return this.examDao.create(exam, exam.getExamQuestions());
        } catch (DaoException e) {
            logger.error("Service Exception createExam {}", exam, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Exam deleteExam(Exam exam) throws ServiceException {
        logger.debug("entering method deleteExam with parameters {}", exam);
        if (!DtoValidator.validate(exam)) {
            logger.error("Service Exception deleteExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try {
            return this.examDao.delete(exam);
        } catch (DaoException e) {
            logger.error("Service Exception deleteExam {}", exam, e);
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * getRightQuestions
     * chooses the right questions for the exam and saves it into a list
     * @param exam exam for which the questions are chosen
     * @param topicID topic from which the questions are chosen
     * @param examTime duration of the exam
     * @return returns a List that contains the questions of the given exam
     * @throws ServiceException
     *
     * */
    public List<Question> getRightQuestions(Exam exam, int topicID, int examTime)
        throws ServiceException {
        logger
            .debug("entering method getRigthQuestions with parameters {}", exam, topicID, examTime);
        if (exam == null || topicID <= 0 || examTime <= 0) {
            logger.error("Service Exception getRightQuestions {}", exam, topicID, examTime);
            throw new ServiceException("Invalid values, please check your input");
        }

        this.egp = new ExamGenParams();

        try {
            this.egp.setNotAnsweredQuestionID(this.subjectQuestionDao.getAllQuestionsOfSubject(exam, topicID));
            egp.setQuestionBooleans(this.examQuestionDao.getAllQuestionBooleans(egp.getNotAnsweredQuestionID()));

            for (int e : egp.getNotAnsweredQuestionID()) {
                egp.getNotAnsweredQuestions().add(this.questionDao.getQuestion(e));
            }

            splitQuestions();


            while (!egp.getWrongAnsweredQuestions().isEmpty() || !egp.getNotAnsweredQuestions().isEmpty()
                || !egp.getRightAnsweredQuestions().isEmpty()) {
                System.out.println("hier");

                if (!egp.getNotAnsweredQuestions().isEmpty()) {
                    egp.getNotAnsweredQuestions().remove(chooseQuestions(egp.getNotAnsweredQuestions(), examTime));
                }

                if (!egp.getWrongAnsweredQuestions().isEmpty() && egp.getNotAnsweredQuestions().isEmpty()) {
                    egp.getWrongAnsweredQuestions().remove(chooseQuestions(egp.getWrongAnsweredQuestions(), examTime));
                }


                if (!egp.getRightAnsweredQuestions().isEmpty() && egp.getNotAnsweredQuestions().isEmpty()
                    && egp.getWrongAnsweredQuestions().isEmpty()) {
                    egp.getRightAnsweredQuestions().remove(chooseQuestions(egp.getRightAnsweredQuestions(), examTime));
                }
            }

        } catch (DaoException e) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam, topicID,
                examTime, e);
            throw new ServiceException(e.getMessage());
        }

        if (egp.getExamQuestions().isEmpty()) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException(
                "Could not get Questions with subjectId " + exam.getSubjectID() + " and topicID "
                    + topicID + " or examTime " + examTime + " is too small");
        }

        if (egp.getQuestionTime() < examTime * 0.8) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException(
                "There aren't enough questions to cover the exam time " + examTime
                    + " ,please reduce the exam time");
        }

        return egp.getExamQuestions();
    }

    @Override public List<Integer> getAllQuestionsOfExam(int examID) throws ServiceException {
        logger.debug("entering getAllQuestionsOfExam with parameters {}", examID);

        if (examID <= 0) {
            logger.error("Service Exception getAllQuestionsOfExam with parameters {}", examID);
            throw new ServiceException("Invalid examID, please check your input");
        }

        try {
            return this.examQuestionDao.getAllQuestionsOfExam(examID);
        } catch (DaoException e) {
            logger.error("Service Exception getAllQuestionsOfExam with parameters {}", examID, e);
            throw new ServiceException(e.getMessage());
        }
    }

    public void splitQuestions(){
        List<Question> temp = new ArrayList<>();
        int counter = 0;

        for(Question q: egp.getNotAnsweredQuestions()){
            temp.add(q);
        }

        for (Map.Entry<Integer, Boolean> e : egp.getQuestionBooleans().entrySet()) {
            for (int i = 0; i < egp.getNotAnsweredQuestions().size(); i++) {
                if (e.getKey() == egp.getNotAnsweredQuestions().get(i).getQuestionId()) { // checks if the question has been answered already
                    if (e.getValue() == true) { // if the question has already been answered correctly
                        egp.getRightAnsweredQuestions().add(temp.get(counter));
                        egp.getNotAnsweredQuestions().remove(temp.get(counter));
                    } else { // if the question has already been answered incorrectly
                        egp.getWrongAnsweredQuestions().add(temp.get(counter));
                        egp.getNotAnsweredQuestions().remove(temp.get(counter));
                    }
                    counter++;
                }
            }
        }
    }

    public int chooseQuestions(List<Question> questionList, int examTime){
        int random = (int) (Math.random() * questionList.size());
        if ((this.egp.getQuestionTime() + questionList.get(random).getQuestionTime())
            <= examTime) { // if time of questions are still smaller or the same as examTime
            egp.getExamQuestions().add(questionList.get(random));
            egp.setQuestionTime(egp.getQuestionTime() + questionList.get(random).getQuestionTime());
        }

        return random;
    }

    public List<Integer> gradeExam(Exam exam) throws ServiceException{
        logger.debug("entering gradeExam with parameters {}",exam);
        List <Integer> questionIDList = new ArrayList<>();
        List <Question> questionList = new ArrayList<>();

        if(!DtoValidator.validate(exam)){
            logger.error("Service Exception gradeExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try{

            questionIDList = getAllQuestionsOfExam(exam.getExamid());
            for(int i = 0; i < questionIDList.size(); i++){
                questionList.add(this.questionDao.getQuestion(questionIDList.get(i)));
            }


        }catch (DaoException e){
            logger.error("Dao Exception gradeExam with parameters {}", exam, e);
            throw new ServiceException(e.getMessage());
        }


        return null;
    }

    public List<Integer> calculateResult(List<Question> questionList){
        int incorrect = 0;
        int correct = 0;
        List<Integer> result = new ArrayList<>();

        if(questionList != null && questionList.size() > 0){
            for(int i = 0; i < questionList.size(); i++){

            }
        }

        return null;
    }

}
