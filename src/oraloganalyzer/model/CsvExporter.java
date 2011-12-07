package oraloganalyzer.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import oraloganalyzer.views.ErrorListLabelProvider;

public class CsvExporter
{
	private final String SEPARATOR = ";";
	private ParsingResult inputData;
	private File targetFile;
	private BufferedWriter writer;
	private ErrorListLabelProvider labelProvider;
		
	public CsvExporter(ParsingResult inputData, File targetFile)
	{
		this.inputData = inputData;
		this.targetFile = targetFile;
		this.labelProvider = new ErrorListLabelProvider();
	}
	
	public void export() throws IOException
	{
		this.writer = new BufferedWriter(new FileWriter(this.targetFile));
		this.writeHeader();
		for (OraLineError ole : this.inputData.getResults())
		{
			this.writeLine(ole);
		}
		writer.flush();
		writer.close();
	}
	
	private void writeHeader() throws IOException
	{
		this.writer.write("LfdNr");
		this.writer.write(SEPARATOR);
		this.writer.write("Zeile");
		this.writer.write(SEPARATOR);
		this.writer.write("Fehlercode");
		this.writer.write(SEPARATOR);
		this.writer.write("Fehlermeldung");
		this.writer.write(SEPARATOR);
		this.writer.newLine();
	}
	
	private void writeLine(OraLineError ole) throws IOException
	{
		this.writer.write(this.labelProvider.getColumnText(ole, ErrorListLabelProvider.LFD_NR));
		this.writer.write(SEPARATOR);
		this.writer.write(this.labelProvider.getColumnText(ole, ErrorListLabelProvider.ZEILE));
		this.writer.write(SEPARATOR);
		this.writer.write(this.labelProvider.getColumnText(ole, ErrorListLabelProvider.FEHLERCODE));
		this.writer.write(SEPARATOR);
		this.writer.write(this.labelProvider.getColumnText(ole, ErrorListLabelProvider.FEHLERMELDUNG));
		this.writer.write(SEPARATOR);
		this.writer.newLine();
	}
}
