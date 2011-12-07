package oraloganalyzer;

/**
 * @author dgottschalk
 * Sammlung von Konstanten zur Identifikation der Komponenten f�r Referenzierung �ber ID
 */
public interface IComponentIdentities
{
	/** ID der Log-Analyzer-Persepektive */
	final String OLA_PERSPECTIVE = "oraloganalyzer.OlaPerspective";
	
    /** ID des Log-Editors */
    final String ORALOG_EDITOR = "oraloganalyzer.editor.OraLogEditor";
	
	/** ID der einfachen tabellarischen Darstellung der Fehlerliste */
	final String ERRORLIST_TABLE_VIEW = "oraloganalyzer.views.ErrorListTableView";
	
	/** ID der erweiterten Darstellung der Fehlerliste */
	final String ERRORLIST_GRID_VIEW = "oraloganalyzer.views.ErrorListGridView";
	
	/** Schalter f�r Auswahl der zu verwendenden Anzeige der Fehlerliste */
	final String ERRORLIST_VIEW = ERRORLIST_TABLE_VIEW;
	
	/** ID der �ffnen und Parsen Action */
	final String PARSE_FILE_ACTION = "oraloganalyzer.actions.OpenFileAction";
	
	/** ID der Fehlerliste-Exportieren Action */
	final String EXPORT_ERRORLIST_ACTION = "oraloganalyzer.actions.ExportErrorListAction";
}
