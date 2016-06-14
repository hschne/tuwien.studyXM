package at.ac.tuwien.sepm.ss16.qse18.service;

import java.io.File;

public interface ImportService {

    /**
     * Imports a previously exported file which contains a subject with its topics and questions.
     * The imported subject is then saved into the database
     *
     * @param fileToImport The exported file that has to be imported
     * @throws ServiceException
     */
    void importSubject(File fileToImport) throws ServiceException;
}
