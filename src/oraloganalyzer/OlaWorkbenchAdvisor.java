package oraloganalyzer;

import java.io.File;
import java.lang.reflect.Array;

import oraloganalyzer.actions.ParseFileAction;
import oraloganalyzer.dnd.ParseFileDropTarget;

import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;


public class OlaWorkbenchAdvisor extends WorkbenchAdvisor
{
    private WorkbenchActionBuilder actionBuilder;
    private Object startupArgs;
    
    public OlaWorkbenchAdvisor(Object startupArgs)
	{
		super();
		this.startupArgs = startupArgs;
	}
    
    public String getInitialWindowPerspectiveId()
    {
        return IComponentIdentities.OLA_PERSPECTIVE;
    }

    public void preWindowOpen(IWorkbenchWindowConfigurer configurer)
    {
        configurer.setTitle("OraLogAnalyzer");
        configurer.setInitialSize(new Point(1000, 800));
        configurer.setShowStatusLine(true);
        configurer.setShowCoolBar(true);
        configurer.setShowMenuBar(true);
        configurer.setShowPerspectiveBar(false);

        IEditorRegistry edtRegistry = configurer.getWindow().getWorkbench().getEditorRegistry();
        edtRegistry.setDefaultEditor("log", IComponentIdentities.ORALOG_EDITOR);
        
        ParseFileDropTarget parseFileDrop = new ParseFileDropTarget(configurer.getWindow());
        configurer.addEditorAreaTransfer(FileTransfer.getInstance());
        configurer.configureEditorAreaDropListener(parseFileDrop);
    }

    public void fillActionBars(IWorkbenchWindow window,
            IActionBarConfigurer configurer, int flags)
    {
        if (this.actionBuilder == null)
            this.actionBuilder = new WorkbenchActionBuilder(window);

        this.actionBuilder.makeAndPopulateActions(getWorkbenchConfigurer(),
                configurer);
    }

    public void postShutdown()
    {
        if (this.actionBuilder != null)
            this.actionBuilder.dispose();
    }
    
    @Override
    public void postStartup()
    {
    	openFileOnStartup();
    }
    
    private void openFileOnStartup()
    {
    	try
		{
			String startupFilename = Array.get(startupArgs, 0).toString();
			File file = new File(startupFilename);
			if (! file.exists()) return;
			ParseFileAction pfa = (ParseFileAction) ParseFileAction.create(OlaPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow());
			pfa.parseFile(file);
			pfa.dispose();
		}
		catch (Exception e)
		{
		}
    }

}
