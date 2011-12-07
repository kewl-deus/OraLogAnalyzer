package oraloganalyzer.dnd;

import java.io.File;

import oraloganalyzer.actions.ParseFileAction;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

public class ParseFileDropTarget extends DropTargetAdapter
{
	private IWorkbenchWindow window;


	public ParseFileDropTarget(IWorkbenchWindow window)
	{
		this.window = window;
	}

	public void dragEnter(DropTargetEvent event)
	{
		// always indicate a copy
		event.detail = DND.DROP_COPY;
	}

	public void dragOperationChanged(DropTargetEvent event)
	{
		// always indicate a copy
		event.detail = DND.DROP_COPY;
	}

	public void drop(final DropTargetEvent event)
	{
		Display d = window.getShell().getDisplay();
		final IWorkbenchPage page = window.getActivePage();
		if (page != null)
		{
			d.asyncExec(new Runnable()
			{
				public void run()
				{
					asyncDrop(event, page);
				}
			});
		}
	}

	private void asyncDrop(DropTargetEvent event, IWorkbenchPage page)
	{
		
		if (event.data == null)
        {
            event.detail = DND.DROP_NONE;
            return;
        }
        if (FileTransfer.getInstance().isSupportedType(event.currentDataType))
        {
            String[] fileNames = (String[]) event.data;
            if (fileNames.length == 0)
            {
            	event.detail = DND.DROP_NONE;
                return;
            }
            File file = new File(fileNames[0]);
			if (! file.exists()) return;
			ParseFileAction pfa = (ParseFileAction) ParseFileAction.create(page.getWorkbenchWindow());
			pfa.parseFile(file);
			pfa.dispose();
        }
	}
}
