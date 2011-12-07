package oraloganalyzer.views;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import oraloganalyzer.model.OraError;
import oraloganalyzer.model.OraLineError;
import oraloganalyzer.model.ParsingResult;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class ErrorListGridView extends ErrorListView
{
	private Grid grid;

	private Map<String, GridItem> gridDataMap;
	
	public ErrorListGridView()
	{
		super();
	}
	
	public void createPartControl(Composite parent)
	{
		gridDataMap = new HashMap<String, GridItem>();
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		
	    this.grid = new Grid(parent,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    grid.setLayoutData(gridData);
	    grid.setHeaderVisible(true);
	    grid.setRowHeaderVisible(false);
	    grid.setLinesVisible(true);
	    grid.setRowsResizeable(true);
	    	
		GridColumn codeColumn = new GridColumn(grid, SWT.NONE);
		codeColumn.setText("Fehlercode");
		codeColumn.setWidth(80);
		codeColumn.setTree(true);

		GridColumn zeileColumn = new GridColumn(grid, SWT.NONE);
		zeileColumn.setText("Zeile");
		zeileColumn.setWidth(70);
		
		GridColumn meldungColumn = new GridColumn(grid, SWT.NONE);
		meldungColumn.setText("Fehlermeldung");
		meldungColumn.setWidth(500);
		meldungColumn.setWordWrap(true);

		registerListeners();


		this.getViewSite().getPage().getWorkbenchWindow().addPerspectiveListener(this);
	}

	public void setFocus()
	{
		this.grid.setFocus();
	}

	public void setInput(ParsingResult input)
	{
		this.data = input;
		grid.removeAll();
		gridDataMap.clear();
		fillGrid();
		grid.redraw();
	}
	
	private void fillGrid()
	{
		SortedSet<OraError> oeSet = new TreeSet<OraError>();
		for(OraLineError ole : this.data.getResults())
		{
			oeSet.add(ole.getError());
		}
		
		Color nodeRowColor = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
		//Color nodeRowColor = new Color(Display.getCurrent(), new RGB(0, 128, 0));
		for(OraError err : oeSet)
		{
			GridItem node = new GridItem(grid,SWT.NONE);
			node.setText(labelProvider.formatFehlercode(err));
			
			node.setBackground(0, nodeRowColor);
			node.setBackground(1, nodeRowColor);
			node.setBackground(2, nodeRowColor);
			
			//node.setExpanded(true);
			node.setData("anzahl", 0);
			gridDataMap.put(labelProvider.formatFehlercode(err), node);
		}
		
		for(OraLineError ole : this.data.getResults())
		{
			GridItem node = gridDataMap.get(labelProvider.formatFehlercode(ole.getError()));
			int anz = (Integer) node.getData("anzahl");
			node.setData("anzahl", anz+1);
		    
			GridItem subnode = new GridItem(node,SWT.NONE);
			subnode.setText(1,labelProvider.getColumnText(ole, IErrorListColumns.ZEILE));
			subnode.setText(2,labelProvider.getColumnText(ole, IErrorListColumns.FEHLERMELDUNG));
			subnode.setData(ole);
		}
		
		for(GridItem errNode : gridDataMap.values())
		{
			errNode.setText(2, "Anzahl: " + errNode.getData("anzahl"));
		}
	}

	public void clearData()
	{
		this.data = null;
		grid.removeAll();
		gridDataMap.clear();
	}

	private final void registerListeners()
	{
		grid.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent event)
			{
			}
			
			public void widgetSelected(SelectionEvent event)
			{
				try
				{
					GridItem item = (GridItem) event.item;
					OraLineError ole = (OraLineError) item.getData();
					editor.selectAndReveal(ole.getOffset(), ole.getError()
							.getErrType().getErrCodeLength());
				} catch (RuntimeException e)
				{
				}
			}
		});
	}


}
