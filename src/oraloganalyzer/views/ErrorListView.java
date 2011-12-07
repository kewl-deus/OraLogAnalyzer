package oraloganalyzer.views;

import oraloganalyzer.IComponentIdentities;
import oraloganalyzer.editor.OraLogEditor;
import oraloganalyzer.model.ParsingResult;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

public abstract class ErrorListView extends ViewPart implements IPerspectiveListener
{
	protected OraLogEditor editor;

	protected ParsingResult data;
	
	protected ErrorListLabelProvider labelProvider;

	
	public ErrorListView()
	{
		this.labelProvider = new ErrorListLabelProvider();
	}
	
	public void setEditor(OraLogEditor editor)
	{
		if (this.editor != editor)
		{
			this.editor = editor;
		}
	}


	public abstract void setInput(ParsingResult input);

	public abstract void clearData();

	public ParsingResult getData()
	{
		return this.data;
	}


	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective)
	{
	}

	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId)
	{
		if (perspective.getId().equals(IComponentIdentities.OLA_PERSPECTIVE)
				&& changeId.equals(IWorkbenchPage.CHANGE_EDITOR_CLOSE))
		{
			this.clearData();
		}
	}

}
