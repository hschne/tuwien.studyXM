package at.ac.tuwien.sepm.ss16.qse18.application;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

public class MainTmp {
    public static void main(String[] args) {
        /*
        try {

            SubjectDao sdao = new SubjectDaoJdbc();
            List<Subject> s = sdao.getSubjects();
            System.out.println("DAVOR");
            for (Subject i : s) {
                System.out.println(i.getFid() + ":   " + i.getName() + "   " + i.getEcts() + " ECTS   " + i.getSemester());
            }
            Subject toCreate = new Subject();
            toCreate.setFid(4);
            toCreate.setName("pp");
            toCreate.setEcts(5);
            toCreate.setSemester("WS14");
            sdao.updateSubject(toCreate);
            s = sdao.getSubjects();
            System.out.println("DANACH");
            for (Subject i : s) {
                System.out.println(i.getFid() + ":   " + i.getName() + "   " + i.getEcts() + " ECTS   " + i.getSemester());
            }

        } catch (DaoException e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
        */
    }

}
