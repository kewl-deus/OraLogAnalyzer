package oraloganalyzer.editor;

import java.util.ArrayList;
import java.util.List;

import oraloganalyzer.model.OraErrorType;
import oraloganalyzer.model.OraLineError;
import oraloganalyzer.model.ParsingResult;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class OraLogScanner extends BufferedRuleBasedScanner
{
	private static String[] sqlKeywords = { "CREATE", "SELECT", "DELETE",
			"INSERT", "UPDATE", "FROM", "SET", "TABLE", "COLUMN", "VALUES",
			"WHERE", "ORDER", "BY", "ASC", "DESC", "BETWEEN", "GROUP",
			"HAVING", "INTO", "GRANT", "REVOKE", "MERGE", "ALTER", "VIEW",
			"PACKAGE", "BODY", "TRIGGER", "AFTER", "BEFORE", "FUNCTION",
			"PROCEDURE", "DROP", "CONSTRAINT", "USING", "INDEX", "ON",
			"COMMENT", "PRIMARY", "FOREIGN", "KEY", "UNIQUE", "NOT", "BEGIN",
			"END", "AND", "OR", "CASE", "WHEN", "CONNECT", "DISCONNECT" };

	private static String[] sqlTypes = { "VARCHAR", "VARCHAR2", "CHAR",
			"NUMBER", "DATE", "INTEGER", "BYTE", "BLOB", "TEXT", "REAL",
			"DECIMAL", };

	private static String[] sqlConstants = { "TRUE", "FALSE", "NULL" };
	
	//Tokens
	private final IToken KEYWORD_TOKEN;
	private final IToken TYPE_TOKEN;
	private final IToken STRING_TOKEN;
	private final IToken COMMENT_TOKEN;
	private final IToken OTHER_TOKEN;
	private final IToken ORA_ERROR_TOKEN;
	
	private List<IRule> basicRules;
	
	
	
	public OraLogScanner(SyntaxColorProvider colorProvider)
	{
		super(5000);
		
		//create Tokens
		KEYWORD_TOKEN = new Token(new TextAttribute(colorProvider
				.getColor(SyntaxColorProvider.KEYWORD)));
		
		TYPE_TOKEN = new Token(new TextAttribute(colorProvider
				.getColor(SyntaxColorProvider.TYPE)));
		
		STRING_TOKEN = new Token(new TextAttribute(colorProvider
				.getColor(SyntaxColorProvider.STRING)));
		
		COMMENT_TOKEN = new Token(new TextAttribute(colorProvider
					.getColor(SyntaxColorProvider.SINGLE_LINE_COMMENT)));
		
		OTHER_TOKEN = new Token(new TextAttribute(colorProvider
				.getColor(SyntaxColorProvider.DEFAULT)));
		
		ORA_ERROR_TOKEN = new Token(new TextAttribute(colorProvider
				.getColor(SyntaxColorProvider.RED)));
		
		//create BasicRules
		basicRules = new ArrayList<IRule>();
		basicRules.add(new WhitespaceRule(new WhitespaceDetector()));
		basicRules.addAll(createSQLRules());
		//ruleList.addAll(createGenericLogErrorRules());
		
	}
	
	@Override
	public void setRules(IRule[] rules)
	{
		List<IRule> ruleList = new ArrayList<IRule>();
		
		//Add given Rules
		for (IRule rule : rules)
		{
			ruleList.add(rule);
		}
		
		//Add Basic Rules
		ruleList.addAll(this.basicRules);

		super.setRules(toArray(ruleList));
	}
	
	public IRule[] createGivenOffsetRules(ParsingResult pr)
	{
		List<IRule> ruleList = new ArrayList<IRule>();
		
		for (OraLineError ole : pr.getResults())
		{
			GivenOffsetRule gor = new GivenOffsetRule(ORA_ERROR_TOKEN, ole.getOffset(), ole.getMessage().length());
			ruleList.add(gor);
		}
		
		return toArray(ruleList);
	}
	
	private final List<IRule> createGenericLogErrorRules()
	{
		List<IRule> rules = new ArrayList<IRule>();

		for (OraErrorType errType : OraErrorType.values())
		{
			for (int i = 0; i < 3; i++)
			{
				rules.add(new PatternRule(errType.getTypename() + i, null, ORA_ERROR_TOKEN, ':', true));
			}
		}

		return rules;
	}

	private final List<IRule> createSQLRules()
	{
		List<IRule> rules = new ArrayList<IRule>();

		// Add rule for single line comments.
		rules.add(new EndOfLineRule("--", COMMENT_TOKEN));

		// Add rule for strings and character constants.
		rules.add(new SingleLineRule("\"", "\"", STRING_TOKEN, '\\'));
		rules.add(new SingleLineRule("'", "'", STRING_TOKEN, '\\'));

		// Add word rule for keywords, types, and constants.
		WordRule wordRule = new WordRule(new WordDetector(), OTHER_TOKEN);
		for (String sqlKeyword : sqlKeywords)
		{
			wordRule.addWord(sqlKeyword.toLowerCase(), KEYWORD_TOKEN);
			wordRule.addWord(sqlKeyword.toUpperCase(), KEYWORD_TOKEN);
		}
		for (String sqlType : sqlTypes)
		{
			wordRule.addWord(sqlType.toLowerCase(), TYPE_TOKEN);
			wordRule.addWord(sqlType.toUpperCase(), TYPE_TOKEN);
		}
		for (String sqlConstant : sqlConstants)
		{
			wordRule.addWord(sqlConstant.toLowerCase(), TYPE_TOKEN);
			wordRule.addWord(sqlConstant.toUpperCase(), TYPE_TOKEN);
		}
		rules.add(wordRule);

		return rules;
	}
	
	private IRule[] toArray(List<IRule> ruleList)
	{
		IRule[] rules = new IRule[ruleList.size()];
		return ruleList.toArray(rules);
	}
	
//	@Override
//	public IToken nextToken()
//	{
//		System.out.println("TokenOffset: " + super.getTokenOffset());
//		return super.nextToken();
//	}
}
