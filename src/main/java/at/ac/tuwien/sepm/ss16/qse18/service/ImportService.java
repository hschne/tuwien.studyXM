package at.ac.tuwien.sepm.ss16.qse18.service;

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
}
