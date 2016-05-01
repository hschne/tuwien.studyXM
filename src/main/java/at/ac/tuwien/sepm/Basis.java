package at.ac.tuwien.sepm;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm.gui.MainFrame;

/**
 * Class containing just the main method, nothing more.
 * 
 * @author The SE-Team
 * @version 1.0
 */
public class Basis {

	/**
	 * Retrieves the logger for this root package
	 */
	private static Logger log = Logger.getLogger(Basis.class);

	/**
	 * Entry point for the program. Configures the the Log4j-Logger, 
	 * starts the MainFrame and then terminates.
	 * 
	 * @param args
	 *            the command-line arguments
	 * @see MainFrame
	 */
	public static void main(String[] args) {
		// configure log4j based on log.properties
		log.info("Starting Application");
       
		MainFrame amf = new MainFrame();
		amf.setLocation(50, 50);
		amf.pack();
		amf.setVisible(true);
		log.info("Terminating Start Class");
	}

}
