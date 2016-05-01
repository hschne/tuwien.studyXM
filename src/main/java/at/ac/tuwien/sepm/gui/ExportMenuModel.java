package at.ac.tuwien.sepm.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.sepm.export_import.Export;

/**
 * This class contains the Export-Classes, used by the "Export"-Menu. New Exporters can be
 * added by adding them to the Bean-Config file ({@value at.ac.tuwien.sepm.helper.Constants#SPRINGBEANS})
 * 
 * @author The SE-Team
 * @version 1.2
 * @since 1.2
 * @see Export
 * 
 */
public class ExportMenuModel {

	/**
	 * Retrieves the logger for this class.
	 */
	private static Log log = LogFactory.getLog(ExportMenuModel.class);

	/**
	 * List of available exportfilters.
	 */
	private List<Export> exportFilter;

	/**
	 * Initialized by the Springframework after setter injection
	 */
	private Export selectedItem;

	public ExportMenuModel() {
		super();
		exportFilter = new ArrayList<Export>();
	}

	/**
	 * Initialise Method called by Springframework after setter injection
	 */
	public void init() {

	}

	public List<Export> getExportFilter() {
		return exportFilter;
	}

	public void setExportFilter(List<Export> exportFilter) {
		StringBuffer regfilters = new StringBuffer("" + exportFilter.size()
				+ " Export Filters are registered (");
		for (Export ex : exportFilter) {
			regfilters.append(ex.toString() + ", ");
		}
		regfilters.append(")");
		log.info(regfilters.toString());
		this.exportFilter = exportFilter;
	}

	public Export getSelectedItem() {
		return selectedItem;
	}

	/**
	 * Searches for the Export-Class with the provided name.
	 * @param object the name of the Export-Class as String.
	 */
	public void setSelectedItem(Object object) {
		for (Export export : exportFilter) {
			if (export.toString().equals((String) object)) {
				log.info("Export with name " + (String) object + " found.");
				selectedItem = export;
			}
		}
	}

	public Object getElementAt(int index) {
		return exportFilter.get(index);
	}

	public int getSize() {
		return exportFilter.size();
	}

}
