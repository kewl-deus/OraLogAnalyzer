package oraloganalyzer.model;

import java.util.regex.Pattern;

/**
 * @author dgottschalk Oracle-Fehler-Typen
 */
public enum OraErrorType implements Comparable<OraErrorType>
{
	ORA("ORA-", 5), SP2("SP2-", 4), SQL("SQL-", 5), IMP("IMP-", 5), CPY("CPY-",
			4), TNS("TNS-", 5), TS("TS-", 5), PCB("PCB-", 5), PLS("PLS-", 5), PCC(
			"PCC-", 5), O2F("O2F-", 5), O2I("O2I-", 5), O2U("O2U-", 5), DRG(
			"DRG-", 5), VIR("VIR-", 5), VID("VID-", 5), EPC("EPC-", 5), EXP(
			"EXP-", 5), IMG("IMG-", 5), LCD("LCD-", 5), LFI("LFI-", 5), LRM(
			"LRM-", 5), MOD("MOD-", 5), NCR("NCR-", 5), NMP("NMP-", 5), NNC(
			"NNC-", 5), NNF("NNF-", 5), NNL("NNL-", 5), NNO("NNO-", 5), NPL(
			"NPL-", 5), NZE("NZE-", 5), PCF("PCF-", 5), QSM("QSM-", 5), RMAN(
			"RMAN-", 5), SDO("SDO-", 5), SLL("SLL-", 5);

	/** Abkürzung des Fehlertyps inkl. "-" Zeichen */
	private final String typename;

	/** Anzahl Ziffern der Fehlernummer */
	private final int errNoLength;

	/** Länge des Namens */
	private final int typenameLength;

	/** Länge des gesamten Fehlercodes (Name + Nr.) */
	private final int errCodeLength;

	private OraErrorType(String typename, int errNoLength)
	{
		this.typename = typename;
		this.errNoLength = errNoLength;
		this.typenameLength = typename.length();
		this.errCodeLength = typename.length() + errNoLength;
	}

	public int getErrCodeLength()
	{
		return errCodeLength;
	}

	public String getTypename()
	{
		return typename;
	}

	public int getTypenameLength()
	{
		return typenameLength;
	}

	public int getErrNoLength()
	{
		return errNoLength;
	}

	public static String getRegExp()
	{
		StringBuilder b = new StringBuilder();
		b.append("(");
		OraErrorType[] types = OraErrorType.values();
		for (int i = 0; i < types.length; i++)
		{
			b.append(types[i].getTypename());
			if (i < types.length - 1)
			{
				b.append("|");
			}
		}
		b.append(")");
		b.append("[0-9]+");
		return b.toString();
	}

	public static Pattern getRegExpPattern()
	{
		return Pattern.compile(getRegExp());
	}

}
