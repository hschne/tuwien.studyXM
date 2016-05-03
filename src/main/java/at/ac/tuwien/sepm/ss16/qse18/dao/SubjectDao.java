package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

public interface SubjectDao {

    Subject getSubject(int id);

    List<Subject> getSubjects();

    Subject createSubject(Subject subject);

    Subject deleteSubject(Subject subject);

    Subject updateSubject(Subject subject);

}