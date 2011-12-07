package oraloganalyzer.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SortSelectionAdapter extends SelectionAdapter
{
	private TableViewer tableViewer;
	private int column;
		
	public SortSelectionAdapter(TableViewer tableViewer, int column)
	{
		this.tableViewer = tableViewer;
		this.column = column;
	}

	public void widgetSelected(SelectionEvent e)
	{		
		if (tableViewer.getSorter() != null
			&& tableViewer.getSorter() instanceof ErrorListSorter)
		{
			ErrorListSorter currentSorter = (ErrorListSorter) tableViewer.getSorter();
			if (currentSorter.getColumn() == column)
			{
				currentSorter.switchDirection();
				tableViewer.refresh();
				return;
			}
		}
		tableViewer.setSorter(new ErrorListSorter(column));
	}
}
