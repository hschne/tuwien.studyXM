package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

/**
 * @author Bicer Cem
 */
public interface ExportService {

    /**
     * Exports a subject with its topics and questions
     *
     * @param outputpath the path where the subjects is stored
     * @return true if the export was successfull
     * @throws ServiceException
     */
    boolean export(String outputpath) throws ServiceException;

    /**
     * Sets the subject that has to be exported.
     *
     * Note: This method has to be called before export().
     *
     * @throws ServiceException
     */
   void setSubject(Subject subject) throws ServiceException;

}
