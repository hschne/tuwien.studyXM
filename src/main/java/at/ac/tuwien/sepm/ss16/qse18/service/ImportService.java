package at.ac.tuwien.sepm.ss16.qse18.service;

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
    void importSubject(File zippedFile) throws ServiceException;
}
