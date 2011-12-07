package oraloganalyzer.views;

import oraloganalyzer.model.OraLineError;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class ErrorListSorter extends ViewerSorter implements IErrorListColumns
{
	public static final int ASC = 1;
	public static final int DESC = -1;
	
	private int column;
	private int direction;

	public ErrorListSorter(int column, int direction)
	{
		super();
		this.column = column;
		this.direction = direction;
	}
	
	public ErrorListSorter(int column)
	{
		this(column, ASC);
	}
	
	public void switchDirection()
	{
		this.direction = (this.direction == ASC) ? DESC : ASC;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2)
	{
		OraLineError ole1 = (OraLineError) e1;
		OraLineError ole2 = (OraLineError) e2;

		int c;
		
		switch (this.column)
		{
		case ZEILE:
			c = compareInteger(ole1.getLineNo(),ole2.getLineNo());
			break;
		case LFD_NR:
			c = compareInteger(ole1.getLfdNr(),ole2.getLfdNr());
			break;
		case FEHLERCODE:
			c = ole1.getError().compareTo(ole2.getError());
			break;
		case FEHLERMELDUNG:
			c = ole1.getMessage().compareTo(ole2.getMessage());
			break;
		default:
			c = 0;
		}
		
		return this.direction * c;
	}

	private int compareInteger(int thisVal, int anotherVal)
	{
		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}

	public int getColumn()
	{
		return column;
	}
	
	public int getDirection()
	{
		return direction;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (!(obj instanceof ErrorListSorter))
			return false;
		
		ErrorListSorter other = (ErrorListSorter) obj;
		return this.getColumn() == other.getColumn() && this.getDirection() == other.getDirection();
	}
	
}
