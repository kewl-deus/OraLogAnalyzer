package oraloganalyzer.util;

public class Toolbox
{

	public static int compare(int a, int b)
	{
		if (a < b)
			return -1;
		else if (a > b)
			return 1;
		else
			return 0;
	}
	
	
	/**
	 * String <code>input</code> mit <code>filler</code> von Links auffüllen
	 * bis <code>size</code> erreicht ist
	 * 
	 * @param input
	 * @param filler
	 * @param size
	 * @return
	 */
	public static String lpad(String input, String filler, int size)
	{
		StringBuffer b = new StringBuffer(input);
		while (b.length() < size)
		{
			b.insert(0, filler);
		}
		return b.toString();
	}

}
