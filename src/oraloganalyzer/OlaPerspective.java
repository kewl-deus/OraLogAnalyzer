package oraloganalyzer;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class OlaPerspective implements IPerspectiveFactory
{

    public void createInitialLayout(IPageLayout layout)
    {
        layout.setEditorAreaVisible(true);
        layout.addView(IComponentIdentities.ERRORLIST_VIEW, IPageLayout.RIGHT, new Float(0.6)
                .floatValue(), IPageLayout.ID_EDITOR_AREA);
    }

}
