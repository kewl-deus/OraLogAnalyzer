package oraloganalyzer.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;

public class GivenOffsetRule implements IRule
{
	private IToken succesToken;
	private int offset;
	private int length;
	
	
	
	
	public GivenOffsetRule(IToken succesToken, int offset, int length)
	{
		super();
		this.succesToken = succesToken;
		this.offset = offset;
		this.length = length;
	}


	public IToken evaluate(ICharacterScanner scanner)
	{
		if (!(scanner instanceof ITokenScanner))
			return Token.UNDEFINED;
		
		ITokenScanner tscanner = (ITokenScanner) scanner;
		
		if (tscanner.getTokenOffset() == this.offset)
		{
			//Zeichen weglesen
			int c;
			for (int i = 0; i < this.length; i++)
			{
				c = scanner.read();
				if (c == ICharacterScanner.EOF)
				{
					break;
				}
			}
			
			return this.succesToken;
		}
		//else
		return Token.UNDEFINED;
	}

}
