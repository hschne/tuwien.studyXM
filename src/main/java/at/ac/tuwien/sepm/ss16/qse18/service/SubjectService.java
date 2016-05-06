package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

/*
* Interface that defines the service layer of subject
* */
public interface SubjectService {

    Subject getSubject(int id) throws ServiceException;

    List<Subject> getSubjects()throws ServiceException;

    Subject createSubject(Subject subject)throws ServiceException;

    boolean deleteSubject(Subject subject)throws ServiceException;

    Subject updateSubject(Subject subject)throws ServiceException;

}
