package oraloganalyzer;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class OlaApplication implements IPlatformRunnable
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
     */
    public Object run(Object args) throws Exception
    {
        Display display = PlatformUI.createDisplay();
        try
        {
            int returnCode = PlatformUI.createAndRunWorkbench(display,
                    new OlaWorkbenchAdvisor(args));
            if (returnCode == PlatformUI.RETURN_RESTART) { return IPlatformRunnable.EXIT_RESTART; }
            return IPlatformRunnable.EXIT_OK;
        }
        finally
        {
            display.dispose();
        }
    }
}
