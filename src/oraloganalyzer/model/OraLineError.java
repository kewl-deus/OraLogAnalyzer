package oraloganalyzer.model;

import oraloganalyzer.util.Toolbox;

public class OraLineError implements Comparable<OraLineError>
{
	protected int offset;
	
	protected int lineNo;
	
	protected int lfdNr;
	
	protected String message;
	
	protected OraError error;
	
	
	public OraLineError(OraErrorType errType, int errNo, int lineNo, int offset, int lfdNr, String message)
	{
		this(new OraError(errType, errNo), lineNo, offset, lfdNr, message);
	}
	
	
	public OraLineError(OraError error, int lineNo, int offset, int lfdNr, String message)
	{
		this.error = error;
		this.lineNo = lineNo;
		this.offset = offset;
		this.lfdNr = lfdNr;
		this.message = message;
	}

	public String toString()
	{
		return "Zeile " + lineNo + ": " + getMessage();
	}

	public int getLineNo()
	{
		return this.lineNo;
	}
	

	public String getMessage()
	{
		if (this.message == null || this.message.length() == 0)
		{
			return error.getMessage();
		}
		else
		{
			return this.message;
		}
	}
	
	
	public int compareTo(OraLineError o)
	{
		int lineNoComp = Toolbox.compare(this.lineNo, o.lineNo);
		if (lineNoComp == 0)
		{
			return this.getError().compareTo(o.getError());
		}
		else
		{
			return lineNoComp;
		}
	}
	
	public boolean equals(OraLineError other)
	{
		return this.compareTo(other) == 0;
	}
	
	public OraError getError()
	{
		return this.error;
	}


	public int getOffset()
	{
		return this.offset;
	}
	
	public int getLfdNr()
	{
		return this.lfdNr;
	}
	
	
}
