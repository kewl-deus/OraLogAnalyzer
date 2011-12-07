package oraloganalyzer.model;

public interface IProcessor<T>
{
	void init();
	
	boolean hasFinished();
	
	T processNext(int steps);
}
