package oraloganalyzer.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OraErrorParser implements IProcessor<Integer>
{
	private BufferedReader reader;
	
	private ParsingResult result;
	
	private int offset;
	
	private int lineNo;
	
	private int errCounter;
	
	private boolean finished;

	private File fileToParse;
	
	
	public OraErrorParser(File fileToParse)
	{
		super();
		this.fileToParse = fileToParse;
	}

	public ParsingResult getParsingResult()
	{
		return this.result;
	}
	
	public void init()
	{
		this.finished = false;
		this.offset = 0;
		this.lineNo = 0;
		this.errCounter = 0;
		this.result = new ParsingResult();
		
		try
		{
			this.reader = new BufferedReader(new FileReader(fileToParse));
		}
		catch (IOException ioe)
		{
			this.result.addException(ioe);
			this.finished = true;
		}
	}

	private void parseLine(int lineNo, int lineOffset, String line)
	{
		for (OraErrorType oraType : OraErrorType.values())
		{
			try
			{
				if (line.startsWith(oraType.getTypename()))
				{
					int errNo = Integer.parseInt(line.substring(oraType
							.getTypenameLength(), oraType.getErrCodeLength()));
					OraLineError err = new OraLineError(oraType, errNo, lineNo, lineOffset, ++errCounter, line);
					this.result.addResult(err);
				}
			}
			catch (Exception e)
			{
				ParserException pex = new ParserException(lineNo);
				this.result.addException(pex);
			}
		}
	}
	
	public Integer processNext(int steps)
	{
		try
		{
			for (int i = 0; i < steps && this.reader.ready(); i++)
			{			
				int lastOffset = this.offset;
				String line = readLine();
				parseLine(this.lineNo, lastOffset, line.trim());
			}
			
			if (! this.reader.ready())
			{
				this.reader.close();
				this.finished = true;
			}
			
		}
		catch (IOException ioe)
		{
			this.result.addException(ioe);
		}
		return this.offset;
	}
	
	private String readLine() throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int c = 0;
		int count = 0;
		while((c = this.reader.read()) != -1)
		{
			count++;
			if (c == '\n')
			{
				//End of Line
				break;
			}
			else
			{
				if (c != '\r')
				{
					sb.append((char) c);
				}
			}
		}
		this.lineNo++;
		this.offset += count;
		
		return sb.toString();
	}
	
	public boolean hasFinished()
	{
		return this.finished;
	}

}
