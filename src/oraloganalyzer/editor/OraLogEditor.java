package oraloganalyzer.editor;

import oraloganalyzer.IComponentIdentities;
import oraloganalyzer.views.ErrorListView;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class OraLogEditor extends AbstractTextEditor
{
	private ErrorListView errListView;
	
	public OraLogEditor()
	{
		super();
		internal_init();
	}

	/**
	 * Initializes the document provider and source viewer configuration. Called
	 * by the constructor. Subclasses may replace this method.
	 */
	protected void internal_init()
	{
		configureInsertMode(SMART_INSERT, false);
		setDocumentProvider(new TextDocumentProvider());
		setSourceViewerConfiguration( new OraLogViewerConfiguration());
		
	}
	
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException
	{
		//Scanner mit ParserInfos versorgen
		OraLogViewerConfiguration viewerConfig = (OraLogViewerConfiguration) getSourceViewerConfiguration();
		try
		{
			OraLogEditorInput oraInput = (OraLogEditorInput) input;
			OraLogScanner scanner = viewerConfig.getScanner();
			scanner.setRules(scanner.createGivenOffsetRules(oraInput.getParsingResult()));
		} catch (RuntimeException e)
		{
		}
		
		super.doSetInput(input);
	}
	
	

	public Object getAdapter(Class required)
	{
		if (ErrorListView.class.equals(required))
		{
			if (this.errListView == null)
			{
				this.errListView = (ErrorListView) this.getSite()
						.getWorkbenchWindow().getActivePage().findView(
								IComponentIdentities.ERRORLIST_VIEW);
			}
			errListView.setEditor(this);
			return errListView;
		}
		return super.getAdapter(required);
	}

}
