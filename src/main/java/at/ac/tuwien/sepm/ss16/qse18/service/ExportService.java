package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

/**
 * Interface ExportService
 * exports a subject
 *
 * @author Bicer Cem
 */
public interface ExportService {

    /**
     * Exports a subject with its topics and questions
     *
     * @param outputpath the path where the subjects is stored
     * @throws ServiceException
     */
    void export(String outputpath) throws ServiceException;

    /**
     * Sets the subject that has to be exported.
     *
     * Note: This method has to be called before export().
     */
   void setSubject(Subject subject);

}
