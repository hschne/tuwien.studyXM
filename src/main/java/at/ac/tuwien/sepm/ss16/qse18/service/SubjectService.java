package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

public interface SubjectService {

    Subject getSubject(int id);

    List<Subject> getSubjects();

    Subject createSubject(Subject subject);

    boolean deleteSubject(Subject subject);

    Subject updateSubject(Subject subject);

}
