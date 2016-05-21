package at.ac.tuwien.sepm.ss16.qse18.application;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Zhang Haixiang on 21.05.2016.
 */
public class jasdkfjtest {

    public static void main(String[] args) {

        try{
            ConnectionH2 database = new ConnectionH2();
            database.getConnection();
            ExamQuestionDao examQuestionDao = new ExamQuestionDaoJdbc(database);
            ExamServiceImpl examService = new ExamServiceImpl(new ExamDaoJdbc(database,examQuestionDao), new SubjectQuestionDaoJdbc(database), examQuestionDao, new QuestionDaoJdbc(database));

            examService.deletedSubject(1);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
