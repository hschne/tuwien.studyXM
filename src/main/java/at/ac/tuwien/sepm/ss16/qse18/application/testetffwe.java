package at.ac.tuwien.sepm.ss16.qse18.application;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhang Haixiang on 17.05.2016.
 */
public class testetffwe {
    public static void main(String[] args) {
        try {
            ConnectionH2 database = new ConnectionH2();
            database.getConnection();
            ExamQuestionDao examQuestionDao = new ExamQuestionDaoJdbc(database);
            ExamDao examDao = new ExamDaoJdbc(database, examQuestionDao);
            SubjectQuestionDao subjectQuestionDao = new SubjectQuestionDaoJdbc(database);
            QuestionDao questionDao = new QuestionDaoJdbc(database);

            ExamService examService = new ExamServiceImpl(examDao, subjectQuestionDao, examQuestionDao, questionDao);

            List<Question> al = new ArrayList<>();
            Question q = new Question();
            q.setQuestion("egal");
            q.setQuestionId(10);
            q.setType(QuestionType.valueOf(1));
            q.setQuestionTime(2);
            al.add(q);

            Topic t = new Topic();
            t.setTopicId(1);
            t.setTopic("Topic1");

            Exam exam = new Exam();
            exam.setExamid(2);
            exam.setCreated(new Timestamp(8989));
            exam.setPassed(false);
            exam.setSubjectID(1);
            exam.setAuthor("author1");
            exam.setExamQuestions(al);

            examService.createExam(exam, t, 2000);
            System.out.println(exam.toString());

        }catch (Exception e){

        }

        System.out.println();
    }
}
