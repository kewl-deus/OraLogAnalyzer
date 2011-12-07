package oraloganalyzer.views;

import oraloganalyzer.model.OraError;
import oraloganalyzer.model.OraErrorType;
import oraloganalyzer.model.OraLineError;
import oraloganalyzer.util.Toolbox;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ErrorListLabelProvider extends LabelProvider implements
		ITableLabelProvider, IErrorListColumns
{

	public ErrorListLabelProvider()
	{
	}
	
	public Image getColumnImage(Object element, int columnIndex)
	{
		return null;
	}

	public String getColumnText(Object element, int columnIndex)
	{
		OraLineError ole = (OraLineError) element;
		switch (columnIndex)
		{
		case ZEILE:
			return String.valueOf(ole.getLineNo());
		case FEHLERCODE:
			return formatFehlercode(ole.getError());
		case FEHLERMELDUNG:
			return formatFehlermeldung(ole);
		case LFD_NR:
			return String.valueOf(ole.getLfdNr());
		}
		return "";

	}

	public String formatFehlercode(OraError err)
	{
		return err.getErrType().getTypename()
				+ Toolbox.lpad(String.valueOf(err.getErrNo()), "0", err
						.getErrType().getErrNoLength());
	}
	
	public String formatFehlermeldung(OraLineError ole)
	{
		String msg = ole.getMessage().trim();
		OraErrorType type = ole.getError().getErrType();
		if (msg.startsWith(type.getTypename()))
		{
			//+1 wegen ":"
			msg = msg.substring(type.getErrCodeLength() + 1);
		}
		if (msg == null || msg.trim().length() == 0)
		{
			msg = ole.getError().getMessage();
		}
		return msg.trim();
	}

}
