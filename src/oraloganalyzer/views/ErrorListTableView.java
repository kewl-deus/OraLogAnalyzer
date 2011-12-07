package oraloganalyzer.views;

import oraloganalyzer.model.OraLineError;
import oraloganalyzer.model.ParsingResult;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class ErrorListTableView extends ErrorListView implements IErrorListColumns
{
	private TableViewer tableViewer;
	
	public ErrorListTableView()
	{
		super();
	}

	public void createPartControl(Composite parent)
	{
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.setLabelProvider(super.labelProvider);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.getControl().setLayoutData(gridData);

		registerListeners();

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn zeileColumn = new TableColumn(table, SWT.LEFT, ZEILE);
		zeileColumn.setText("Zeile");
		zeileColumn.setToolTipText("Position des Fehlers innerhalb des Logfiles");
		zeileColumn.setWidth(70);
		zeileColumn.setMoveable(true);
		zeileColumn.setResizable(true);
		zeileColumn.addSelectionListener(new SortSelectionAdapter(tableViewer, ZEILE));
				
		TableColumn codeColumn = new TableColumn(table, SWT.LEFT, FEHLERCODE);
		codeColumn.setText("Fehlercode");
		codeColumn.setToolTipText("Oracle-Fehlercode");
		codeColumn.setWidth(80);
		codeColumn.setMoveable(true);
		codeColumn.setResizable(true);
		codeColumn.addSelectionListener(new SortSelectionAdapter(tableViewer, FEHLERCODE));

		TableColumn meldungColumn = new TableColumn(table, SWT.LEFT, FEHLERMELDUNG);
		meldungColumn.setText("Fehlermeldung");
		meldungColumn.setToolTipText("Inhaltliche Beschreibung des Fehlers");
		meldungColumn.setWidth(500);
		meldungColumn.setMoveable(true);
		meldungColumn.setResizable(true);
		meldungColumn.addSelectionListener(new SortSelectionAdapter(tableViewer, FEHLERMELDUNG));

		TableColumn nrColumn = new TableColumn(table, SWT.LEFT, LFD_NR);
		nrColumn.setText("LfdNr");
		nrColumn.setToolTipText("Laufende Nummer");
		nrColumn.setWidth(50);
		nrColumn.setMoveable(true);
		nrColumn.setResizable(true);
		nrColumn.addSelectionListener(new SortSelectionAdapter(tableViewer, LFD_NR));

		this.getViewSite().getPage().getWorkbenchWindow().addPerspectiveListener(this);
	}

	public void setFocus()
	{
		this.tableViewer.getControl().setFocus();
	}

	public void setInput(ParsingResult input)
	{
		this.data = input;
		tableViewer.getTable().clearAll();
		tableViewer.refresh();
		tableViewer.setInput(input.resultsToArray());
		//tableViewer.add(input.resultsToArray());

//		Color lfdNrColor = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
//		for (TableItem item : tableViewer.getTable().getItems())
//		{
//			item.setBackground(ErrorListLabelProvider.LFD_NR, lfdNrColor);
//		}
	}

	public void clearData()
	{
		this.data = null;
		tableViewer.getTable().clearAll();
		tableViewer.refresh();
	}

	private final void registerListeners()
	{
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				if (!(event.getSelection() instanceof IStructuredSelection))
					return;
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (selection.size() != 1)
					return;
				Object element = selection.getFirstElement();
				if (!(element instanceof OraLineError))
					return;

				OraLineError ole = (OraLineError) element;
				editor.selectAndReveal(ole.getOffset(), ole.getError()
						.getErrType().getErrCodeLength());
			}
		});
	}


}
