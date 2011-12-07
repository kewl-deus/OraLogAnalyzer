package oraloganalyzer.actions;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import oraloganalyzer.IComponentIdentities;
import oraloganalyzer.OlaPlugin;
import oraloganalyzer.model.CsvExporter;
import oraloganalyzer.model.ParsingResult;
import oraloganalyzer.views.ErrorListView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ExportErrorListAction extends Action implements
		IWorkbenchWindowActionDelegate, ActionFactory.IWorkbenchAction
{
	private IWorkbenchWindow window;

	public ExportErrorListAction()
	{
		setEnabled(true);
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose()
	{
		window = null;
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window)
	{
		this.window = window;
	}

	/*
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		run();
	}

	/*
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
	}

	private File queryFile()
	{
		FileDialog dialog = new FileDialog(window.getShell(), SWT.SAVE);
		dialog.setText("Speicherort für Exportdatei");
		dialog.setFilterExtensions(new String[] { "*.csv" });
		String path = dialog.open();
		if (path != null && path.length() > 0)
		{
			if (! path.endsWith(".csv"))
			{
				path += ".csv";
			}
			return new File(path);
		}
		return null;
	}

	/*
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run()
	{
		File file = queryFile();
		if (file != null)
		{
			IWorkbenchPage page = this.window.getActivePage();
			ErrorListView view = (ErrorListView) page.findView(IComponentIdentities.ERRORLIST_VIEW);
			ParsingResult data = view.getData();
			CsvExporter exp = new CsvExporter(data, file);
			try
			{
				exp.export();
				MessageDialog.openInformation(this.window.getShell(),
						"Export erfolgreich", "Daten in " + file.getPath()
								+ " exportiert.");
			} catch (IOException e)
			{
				MessageDialog.openWarning(this.window.getShell(),
						"Export fehlgeschlagen", e.getMessage());
			}
		} else
		{
			String msg = MessageFormat.format("File is null: {0}",
					new String[] { file.getName() });
			MessageDialog.openWarning(this.window.getShell(), "Problem", msg);
		}
	}

	public static IWorkbenchAction create(IWorkbenchWindow window)
	{
		if (window == null)
		{
			throw new IllegalArgumentException();
		}
		ExportErrorListAction action = new ExportErrorListAction();
		action.init(window);
		action.setId(IComponentIdentities.EXPORT_ERRORLIST_ACTION);
		action.setText("Export");
		action.setImageDescriptor(OlaPlugin.getImageDescriptor("icons/export.gif"));
		return action;
	}

}
