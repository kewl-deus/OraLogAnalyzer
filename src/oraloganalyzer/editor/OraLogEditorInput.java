package oraloganalyzer.editor;

import oraloganalyzer.model.ParsingResult;

import org.eclipse.core.runtime.IPath;

public class OraLogEditorInput extends PathEditorInput
{

	private ParsingResult parsingResult;
	
	public OraLogEditorInput(IPath path, ParsingResult parsingResult)
	{
		super(path);
		this.parsingResult = parsingResult;
	}

	public ParsingResult getParsingResult()
	{
		return parsingResult;
	}

}
