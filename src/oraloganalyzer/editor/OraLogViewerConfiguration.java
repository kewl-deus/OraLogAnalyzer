
package oraloganalyzer.editor;


import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;



public class OraLogViewerConfiguration extends
        SourceViewerConfiguration
{

    private OraLogScanner scanner;
    

    public OraLogScanner getScanner()
    {
        if (scanner == null)
        {
            SyntaxColorProvider colorProvider = new SyntaxColorProvider();
            scanner = new OraLogScanner(colorProvider);
            scanner.setDefaultReturnToken(new Token(new TextAttribute(
                    colorProvider.getColor(SyntaxColorProvider.DEFAULT))));
        }
        return scanner;
    }

    public IPresentationReconciler getPresentationReconciler(
            ISourceViewer sourceView)
    {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }
}
