package oraloganalyzer.actions;

import oraloganalyzer.OlaPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


public class InfoAction extends Action implements
        IWorkbenchWindowActionDelegate
{

    private IWorkbenchWindow window;

    public InfoAction()
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

    /*
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        MessageDialog.openInformation(window.getShell(), "OraLogAnalyzer",
                "Author: Dennis Gottschalk (zeb/information.technology)"
        		+ "\n\nVersion: " + OlaPlugin.VERSION
        		+ "\n\nProgramm zur Analyse von Oracle Log-Dateien");
    }

}
