package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    private SubjectDao sd;

    public SubjectServiceImpl(SubjectDaoJdbc sd){
        this.sd = sd;
    }

    @Override
    public Subject getSubject(int id) throws ServiceException{
        try{
            return sd.getSubject(id);
        }catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Subject> getSubjects() throws ServiceException{
        try {
            return sd.getSubjects();
        }catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Subject createSubject(Subject subject) throws ServiceException{

        try {
            sd.createSubject(subject);
            return subject;
        }catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public boolean deleteSubject(Subject subject) throws ServiceException{
        try{
            sd.deleteSubject(subject);
            return true;
        }catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Subject updateSubject(Subject subject) throws ServiceException{
        try {
            sd.updateSubject(subject);
            return subject;
        }catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }

    }
}
