package oraloganalyzer.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;


public class WhitespaceDetector implements IWhitespaceDetector
{

    /*
     * (non-Javadoc) Method declared on IWhitespaceDetector
     */
    public boolean isWhitespace(char character)
    {
        return Character.isWhitespace(character);
    }
}
