package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExamGenParams;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * Class ExerciseExamServiceImpl
 * concrete implementatioin of ExerciseExamService
 *
 * @author Zhang Haixiang
 */
@Service public class ExerciseExamServiceImpl implements ExerciseExamService {
    private static final Logger logger = LogManager.getLogger();
    private ExerciseExamDao exerciseExamDao;
    private SubjectQuestionDao subjectQuestionDao;
    private ExerciseExamQuestionDao exerciseExamQuestionDao;
    private QuestionDao questionDao;
    private ExerciseExamGenParams egp;


    @Autowired public ExerciseExamServiceImpl(ExerciseExamDao exerciseExamDao, SubjectQuestionDao subjectQuestionDao,
        ExerciseExamQuestionDao exerciseExamQuestionDao, QuestionDao questionDao) {
        this.exerciseExamDao = exerciseExamDao;
        this.subjectQuestionDao = subjectQuestionDao;
        this.exerciseExamQuestionDao = exerciseExamQuestionDao;
        this.questionDao = questionDao;
    }


    @Override public ExerciseExam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameters {}", examID);
        if (examID <= 0) {
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid ExerciseExam ID, please check your input");
        }

        try {
            return this.exerciseExamDao.getExam(examID);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public List<ExerciseExam> getExams() throws ServiceException {
        logger.debug("entering method getExams()");
        try {
            return this.exerciseExamDao.getExams();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public ExerciseExam createExam(ExerciseExam exerciseExam, Topic topic, int examTime) throws ServiceException {
        logger.debug("entering method createExam with parameters {}", exerciseExam);

        tryValidateExam(exerciseExam);

        if (examTime <= 0) {
            logger.error("ExamTime must at least be 1");
            throw new ServiceException("ExamTime must at least be 1");
        }

        try {
            exerciseExam.setExamTime(examTime);
            exerciseExam
                .setExamQuestions(getRightQuestions(exerciseExam, topic.getTopicId(), examTime));
            return this.exerciseExamDao.create(exerciseExam, exerciseExam.getExamQuestions());
        } catch (DaoException e) {
            logger.error("Service Exception createExam {}", exerciseExam, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public ExerciseExam deleteExam(ExerciseExam exerciseExam) throws ServiceException {
        logger.debug("entering method deleteExam with parameters {}", exerciseExam);

        tryValidateExam(exerciseExam);

        try {
            return this.exerciseExamDao.delete(exerciseExam);
        } catch (DaoException e) {
            logger.error("Service Exception deleteExam {}", exerciseExam, e);
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * getRightQuestions
     * chooses the right questions for the exerciseExam and saves it into a list
     * @param exerciseExam exerciseExam for which the questions are chosen
     * @param topicID topic from which the questions are chosen
     * @param examTime duration of the exerciseExam
     * @return returns a List that contains the questions of the given exerciseExam
     * @throws ServiceException
     *
     * */
    public List<Question> getRightQuestions(ExerciseExam exerciseExam, int topicID, int examTime)
        throws ServiceException {
        logger
            .debug("entering method getRigthQuestions with parameters {}", exerciseExam, topicID, examTime);
        if (exerciseExam == null || topicID <= 0 || examTime <= 0) {
            logger.error("Service Exception getRightQuestions {}", exerciseExam, topicID, examTime);
            throw new ServiceException("Invalid values, please check your input");
        }

        this.egp = new ExerciseExamGenParams();

        try {
            this.egp.setNotAnsweredQuestionID(this.subjectQuestionDao.getAllQuestionsOfSubject(
                exerciseExam, topicID));
            egp.setQuestionBooleans(this.exerciseExamQuestionDao.getAllQuestionBooleans(egp.getNotAnsweredQuestionID()));

            for (int e : egp.getNotAnsweredQuestionID()) {
                egp.getNotAnsweredQuestions().add(this.questionDao.getQuestion(e));
            }

            splitQuestions();

            selectQuestions(egp.getNotAnsweredQuestions(), examTime);
            selectQuestions(egp.getWrongAnsweredQuestions(), examTime);
            selectQuestions(egp.getRightAnsweredQuestions(), examTime);

        } catch (DaoException e) {
            logger.error("Service Exception getRightQuestions with parameters{}", exerciseExam, topicID,
                examTime, e);
            throw new ServiceException(e.getMessage());
        }

        if (egp.getExamQuestions().isEmpty()) {
            logger.error("Service Exception getRightQuestions with parameters{}", exerciseExam);
            throw new ServiceException(
                "Could not get Questions with subjectId " + exerciseExam.getSubjectID() + " and topicID "
                    + topicID + " or examTime " + examTime + " is too small");
        }

        if (egp.getQuestionTime() < examTime * 0.8) {
            logger.error("Service Exception getRightQuestions with parameters{}", exerciseExam);
            throw new ServiceException(
                "There aren't enough questions to cover the exerciseExam time " + examTime
                    + " ,please reduce the exerciseExam time");
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
            return this.exerciseExamQuestionDao.getAllQuestionsOfExam(examID);
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
            for (int i = 0; i < temp.size(); i++) {
                if (e.getKey() == temp.get(i).getQuestionId()) { // checks if the question has been answered already
                    if (e.getValue() == true) { // if the question has already been answered correctly
                        egp.getRightAnsweredQuestions().add(temp.get(counter));
                        egp.getNotAnsweredQuestions().remove(temp.get(counter));
                    } else { // if the question has already been answered incorrectly
                        egp.getWrongAnsweredQuestions().add(temp.get(counter));
                        egp.getNotAnsweredQuestions().remove(temp.get(counter));
                    }
                }
                counter++;
            }
            counter = 0;
        }
    }

    public void selectQuestions(List<Question> questionList, int examTime){
        while(!questionList.isEmpty()){
            questionList.remove(chooseQuestions(questionList, examTime));
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

    private void tryValidateExam(ExerciseExam exerciseExam) throws ServiceException {
        try {
            validate(exerciseExam);
        } catch (DtoValidatorException e) {
            logger.error("ExerciseExam [" + exerciseExam + "] is invalid", e);
            throw new ServiceException("ExerciseExam [" + exerciseExam + "] is invalid: " + e);
        }
    }

    /*
    public List<Integer> gradeExam(ExerciseExam exam) throws ServiceException{
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
    */
    
    @Override public List<Integer> getAnsweredQuestionsOfExam(int examID) throws ServiceException {
        logger.debug("entering getAnsweredQuestionsOfExam");

        if(examID <= 0) {
            logger.error("Service Exception getAnsweredQuestionsOfExam with parameters {}", examID);
            throw new ServiceException("Invalid examID, please check your input");
        }

        List<Integer> answeredQuestions = new ArrayList<>();

        try {
            answeredQuestions.addAll(this.exerciseExamQuestionDao.getAnsweredQuestionsPerExam(examID));
        } catch (DaoException e) {
            logger.error("Service Exception getAnsweredQuestionsOfExam with parameters", e);
            throw new ServiceException("Could not get answered questions of exam", e);
        }
        return answeredQuestions;
    }

}
