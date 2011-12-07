package oraloganalyzer.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import oraloganalyzer.IComponentIdentities;
import oraloganalyzer.OlaPlugin;
import oraloganalyzer.editor.OraLogEditorInput;
import oraloganalyzer.model.OraErrorParser;
import oraloganalyzer.model.ParsingResult;
import oraloganalyzer.views.ErrorListView;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.progress.IProgressService;


public class ParseFileAction extends Action implements
        IWorkbenchWindowActionDelegate, ActionFactory.IWorkbenchAction
{
    private IWorkbenchWindow window;

    public ParseFileAction()
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
        FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
        dialog.setText("Datei öffnen");
        dialog.setFilterExtensions(new String[]{"*.log", "*.*"});
        String path = dialog.open();
        if (path != null && path.length() > 0)
            return new File(path);
        return null;
    }


    /*
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
    	parseFile(queryFile());
    }
    
    public void parseFile(File file)
    {
        if (file == null || (! file.exists()) || (! file.canRead()))
        {
            String msg = MessageFormat.format("File is null: {0}",
                    new String[] { file.getName() });
            MessageDialog.openWarning(this.window.getShell(), "Error", msg);
            return;
        }
        final IWorkbenchPage page = this.window.getActivePage();
        IProgressService ps = this.window.getWorkbench().getProgressService();
       
        //Parse
        final OraErrorParser parser = new OraErrorParser(file);
        final int fileLen = (int) file.length();
		try {
			ps.run(true, false,
                    new IRunnableWithProgress() {
                        public void run(IProgressMonitor monitor)
                                throws InvocationTargetException,
                                InterruptedException {
                        	monitor.beginTask("Parse Datei...", fileLen <= 0 ? IProgressMonitor.UNKNOWN : fileLen);
                        	parser.init();
                            while (! parser.hasFinished())
                            {
                            	int parsedChars = parser.processNext(50);
                            	monitor.worked(parsedChars);
                            }
                            monitor.done();
                        }
                    });
        } catch (InvocationTargetException e) {
            
        } catch (InterruptedException e) {         
        }
        
        //long start = System.currentTimeMillis();
        
        //Open Editor
        final IEditorInput input = createEditorInput(file, parser.getParsingResult());
        final String editorId = getEditorId(file);
        page.closeAllEditors(false);
        final IEditorPart[] singleEditorPart = new IEditorPart[]{null};
        try {
			ps.run(false, false,
                    new IRunnableWithProgress() {
                        public void run(IProgressMonitor monitor)
                                throws InvocationTargetException,
                                InterruptedException {
                        	monitor.beginTask("Erzeuge Syntaxhighlighting...", IProgressMonitor.UNKNOWN);
                        	try
							{
								singleEditorPart[0] = page.openEditor(input, editorId);
							}
                        	catch (PartInitException pie)
							{
								MessageDialog.openError(window.getShell(), "Error", pie.getMessage());
							}
							finally
							{
								monitor.done();
							}
                        }
                    });
        } catch (InvocationTargetException e) {
            
        } catch (InterruptedException e) {         
        }
        
        //long end = System.currentTimeMillis();
        //MessageDialog.openInformation(window.getShell(), "Time elapsed", ((end - start)/1000 ) + " Sek.");
        
        IEditorPart editorPart = singleEditorPart[0];
        
        
        //Fetching ParserResult
        ParsingResult result = parser.getParsingResult();
		for (Exception ex : result.getExecptions())
		{
			if (ex instanceof IOException)
			{
				MessageDialog.openError(this.window.getShell(), "I/O-Error", ex.getMessage());
				return;
			}
		}
		ErrorListView view = (ErrorListView) editorPart.getAdapter(ErrorListView.class);
		view.setInput(result);

    }

    private String getEditorId(File file)
    {
        IWorkbench workbench = this.window.getWorkbench();
        IEditorRegistry editorRegistry = workbench.getEditorRegistry();
        IEditorDescriptor descriptor = editorRegistry.getDefaultEditor(file
                .getName());
        if (descriptor != null)
            return descriptor.getId();
        return IComponentIdentities.ORALOG_EDITOR;
    }

    private IEditorInput createEditorInput(File file, ParsingResult result)
    {
        IPath location = new Path(file.getAbsolutePath());
        OraLogEditorInput input = new OraLogEditorInput(location, result);
        return input;
    }
    
    public static IWorkbenchAction create(IWorkbenchWindow window) {
        if (window == null) {
            throw new IllegalArgumentException();
        }
        ParseFileAction action = new ParseFileAction();
        action.init(window);
        action.setId(IComponentIdentities.PARSE_FILE_ACTION);
        action.setText("Open");
        action.setImageDescriptor(OlaPlugin.getImageDescriptor("icons/openFolder.gif"));
        return action;
    }
    
}
