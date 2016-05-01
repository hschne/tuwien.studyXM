package at.ac.tuwien.sepm.gui;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * RegexTextField combines a TextField with a Regular Expression.
 * 
 * @author The SE-Team
 * @version 1.0
 * @see javax.swing.JTextField
 */
public final class RegexTextField extends JTextField {

	/**
	 * serialVersionUID, generated by eclipse.
	 */
	private static final long serialVersionUID = -1298714928521185271L;

	/**
	 * The Regular Expression to be matched against by isMatching() method.
	 */
	private String regex = ".*";

	/**
	 * For user feedback in the GUI.
	 */
	private JComponent validationIndicator = null;

	/**
	 * The color set by validationIndicator
	 */
	private Color errorColor = Color.RED, originalColor = null;

	/**
	 * Constructs a new <code>RegexTextField</code>. A default
	 * <code>model</code> is created, the initial string is <code>null</code>,
	 * and the number of columns is set to 0.
	 * 
	 */
	public RegexTextField() {
		super();
	}

	/**
	 * Constructs a new <code>RegexTextField</code> initialized with the
	 * specified text. A default <code>model</code> is created and the number
	 * of columns is set to 0.
	 * 
	 * @param text
	 *            the text to be displayed, or <code>null</code>.
	 * 
	 */
	public RegexTextField(String text) {
		super(text);
	}

	/**
	 * Constructs a new empty <code>TextField</code> with the specified number
	 * of columns. A default model is created and the initial string is set to
	 * <code>null</code>.
	 * 
	 * @param columns
	 *            the number of columns to use to calculate the preferred width;
	 *            if columns is set to <code>zero</code>, the preferred width
	 *            will be whatever naturally results from the component
	 *            implementation.
	 */
	public RegexTextField(int columns) {
		super(columns);
	}

	/**
	 * Constructs a new <code>TextField</code> initialized with the specified
	 * text and columns. A default model is created.
	 * 
	 * @param text
	 *            the text to be displayed, or <code>null</code>.
	 * @param columns
	 *            the number of columns to use to calculate the preferred width;
	 *            if columns is set to <code>zero</code>, the preferred width
	 *            will be whatever naturally results from the component
	 *            implementation.
	 * 
	 */
	public RegexTextField(String text, int columns) {
		super(text, columns);
	}

	/**
	 * Constructs a new <code>JTextField</code> that uses the given text
	 * storage model and the given number of columns. This is the constructor
	 * through which the other constructors feed. If the document is
	 * <code>null</code>, a default model is created.
	 * 
	 * @param doc
	 *            the text storage to use; if this is <code>null</code>, a
	 *            default will be provided by calling the
	 *            <code>createDefaultModel</code> method.
	 * @param text
	 *            the text to be displayed, or <code>null</code>.
	 * @param columns
	 *            the number of columns to use to calculate the preferred width;
	 *            if columns is set to <code>zero</code>, the preferred width
	 *            will be whatever naturally results from the component
	 *            implementation.
	 */
	public RegexTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	/**
	 * Returns the error-color, the error-color indicates a mismatch of the
	 * regex and the text.
	 * 
	 * @return Returns the errorColor.
	 * @see #getRegex()
	 * @see #getText()
	 */
	public Color getErrorColor() {
		return errorColor;
	}

	/**
	 * Sets the errorColor which indicates a mismatch of the regex and the text.
	 * 
	 * @param errorColor
	 *            The errorColor to set.
	 * @see #setRegex(String)
	 * @see #setText(String)
	 */
	public void setErrorColor(Color errorColor) {
		this.errorColor = errorColor;
	}

	/**
	 * Returns the regular expression the text is validated against.
	 * 
	 * @return Returns the regex
	 * @see #getText()
	 * @see #isMatching()
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * Sets the regular expression the text is validated against.
	 * 
	 * @param regex
	 *            The regex to set.
	 * @see #isMatching()
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * Returns the validationIndicator. It is a <code>JComponent</code> which
	 * is used to indicate a mismatch of the regex and the text. In case of a
	 * mismatch, the <code>JComponent</code>s foreground is set to the
	 * errorColor.
	 * 
	 * @return Returns the validationIndicator, or <code>null</code>.
	 * @see #isMatching()
	 * @see #getErrorColor()
	 */
	public JComponent getValidationIndicator() {
		return validationIndicator;
	}

	/**
	 * Sets the validationInidcator. This is a <code>JComponent</code> which
	 * is used to indicate a mismatch of the regex and the text. In case of a
	 * mismatch, the <code>JComponent</code>s foreground is set to the
	 * errorColor.
	 * 
	 * @param validationIndicator
	 *            The validationIndicator to set, or <code>null</code>.
	 */
	public void setValidationIndicator(JComponent validationIndicator) {
		this.validationIndicator = validationIndicator;
	}

	/**
	 * Determines whether the text is matching the regular expression. The text
	 * is matched against the regex, using the method
	 * <code>Pattern.matches()</code> from <code>java.util.regex</code>. In
	 * case of a mismatch, the
	 * <code>validationIndicator</coed>'s foreground is set to <code>errorColor</code>. 
	 * @return <code>true</code> if the text matches the regex,
	 *         <code>false</code> otherwise.
	 * @see #getRegex()
	 * @see Pattern#matches(java.lang.String, java.lang.CharSequence)
	 * @see #getValidationIndicator()
	 * @see #getErrorColor()
	 */
	public boolean isMatching() {
		boolean erg = Pattern.matches(regex, super.getText());
		if (validationIndicator != null) {
			validationIndicator.setForeground(erg ? originalColor : errorColor);
		}
		return erg;
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		originalColor = fg;
	}

	@Override
	public String getText() {
		// isMatching();
		return super.getText();
	}

	@Override
	public void setText(String t) {
		super.setText(t);
		// isMatching();
	}
}
