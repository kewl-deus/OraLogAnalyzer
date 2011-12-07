package oraloganalyzer.model;

import java.util.ResourceBundle;

import oraloganalyzer.util.Toolbox;

public class OraError implements Comparable<OraError>
{
	private static ResourceBundle bundle = ResourceBundle
			.getBundle("oraloganalyzer/resource/ora9errors");

	protected int errNo;

	protected OraErrorType errType;

	public static String getMessageForError(OraError error)
	{
		String msg = "";
		int positiveErrNo = Integer.signum(error.getErrNo()) * error.getErrNo();
		String strErrNo = Toolbox.lpad(String.valueOf(positiveErrNo), "0",
				error.getErrType().getErrNoLength());
		try
		{
			msg = bundle.getString(error.getErrType().getTypename() + strErrNo);
		}
		catch (RuntimeException e)
		{
		}
		return msg;
	}

	public OraError(OraErrorType errType, int errNo)
	{
		this.errType = errType;
		this.errNo = errNo;
	}

	public int getErrNo()
	{
		return this.errNo;
	}

	public OraErrorType getErrType()
	{
		return this.errType;
	}

	public int compareTo(OraError o)
	{
		int typeComp = this.errType.getTypename().compareTo(
				o.errType.getTypename());
		if (typeComp == 0)
		{
			return Toolbox.compare(this.errNo, o.errNo);
		}
		else
		{
			return typeComp;
		}
	}

	public String toString()
	{

		return this.errType.getTypename()
				+ Toolbox.lpad(String.valueOf(this.errNo), "0", this.errType
						.getErrNoLength()) + ": " + getMessage();
	}

	public String getMessage()
	{
		return getMessageForError(this);
	}

	public boolean equals(OraError other)
	{
		return this.compareTo(other) == 0;
	}
	
	@Override
	public int hashCode()
	{
		return this.errType.ordinal() * 100000 + this.errNo;
	}

}
