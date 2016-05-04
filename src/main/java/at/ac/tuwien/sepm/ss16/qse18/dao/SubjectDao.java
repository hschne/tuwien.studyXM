package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

public interface SubjectDao {

    Subject getSubject(int id) throws DaoException;

    List<Subject> getSubjects()throws DaoException;

    void createSubject(Subject subject)throws DaoException;

    void deleteSubject(Subject subject)throws DaoException;

    void updateSubject(Subject subject)throws DaoException;

}
