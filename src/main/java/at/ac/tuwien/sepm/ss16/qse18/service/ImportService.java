package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectConflict;

import java.io.File;

/**
 * Interface ImportService
 * imports a subject
 *
 * @author Bicer Cem
 */
public interface ImportService {

    /**
     * Imports a previously exported file which contains a subject with its topics and questions.
     * The imported subject is then saved into the database
     *
     * @param zippedFile The file to import
     * @throws ServiceException
     */
    SubjectConflict importSubject(File zippedFile) throws ServiceException;

    void importSubject(ExportSubject exportSubject) throws ServiceException;

    void importTopic(ExportTopic exportTopic, Subject existingSubject) throws ServiceException;

    void importQuestion(ExportQuestion exportQuestion, Topic existingTopic) throws ServiceException;
}
