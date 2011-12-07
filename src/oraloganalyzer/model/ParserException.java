package oraloganalyzer.model;

public class ParserException extends Exception
{

    private int line;
    
    public ParserException(int line)
    {
        super("Fehler beim Parsen von Zeile " + line);
        this.line = line;
    }

    public int getLine()
    {
        return line;
    }
}
