package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService{

    @Override
    public Subject getSubject(int id) {
        Subject sub = new Subject();
        sub.setName("Testing");
        return sub;
    }

    @Override
    public List<Subject> getSubjects() {
        ArrayList<Subject> subjects = new ArrayList<>();
        Subject subjectToAdd = new Subject();
        subjectToAdd.setName("TEsting");
        subjectToAdd.setSemester("WS15");
        subjectToAdd.setEcts(3.0f);
        subjects.add(subjectToAdd);
        return subjects;
    }

    @Override
    public Subject createSubject(Subject subject) {
        return new Subject();
    }

    @Override
    public boolean deleteSubject(Subject subject) {
        return true;
    }

    @Override
    public Subject updateSubject(Subject subject) {
        return new Subject();
    }
}
