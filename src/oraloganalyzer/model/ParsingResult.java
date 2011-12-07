package oraloganalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class ParsingResult
{
	private List<OraLineError> results;
	
	private List<Exception> execptionList;
	
	public ParsingResult()
	{
		this.results = new ArrayList<OraLineError>();
		this.execptionList = new ArrayList<Exception>();
	}
	
	public void clear()
	{
		this.results.clear();
		this.execptionList.clear();
	}

	public boolean addResult(OraLineError o)
	{
		return results.add(o);
	}

	
	public boolean addException(Exception ex)
	{
		return execptionList.add(ex);
	}

	
	public boolean hasExceptions()
	{
		return ! this.execptionList.isEmpty();
	}
	
	public boolean hasResults()
	{
		return ! this.results.isEmpty();
	}
	
	public Object[] resultsToArray()
	{
		return this.results.toArray();
	}

	public List<OraLineError> getResults()
	{
		return results;
	}

	public List<Exception> getExecptions()
	{
		return execptionList;
	}
	
	
	
}
