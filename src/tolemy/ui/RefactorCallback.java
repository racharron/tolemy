/**
 * 
 */
package tolemy.ui;

import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;

import tolemy.rr.Context;
import tolemy.rr.ContextCreationException;
import tolemy.rr.ReflectiveRefactoring;

/**
 * @author rawley
 * This is where all the magic happens.  
 *
 */
public class RefactorCallback implements SelectionListener {
	
	ReflectiveRefactoring refactoring;
	
	/**
	 * 
	 */
	public RefactorCallback(ReflectiveRefactoring refactoring) {
		this.refactoring = refactoring;
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		ITextEditor editor = (ITextEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IProject project = editor.getAdapter(IProject.class);
		IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		ITextSelection selection = (ITextSelection)editor.getSelectionProvider().getSelection();
		if (selection.getLength() != 0) {
			MessageDialog.openError(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"ERROR: selection",
					"tolemy requires nothing to be selected");
			return;
		}
		Context context;
		try {
			context = new Context(project, document, selection);
		} catch (ContextCreationException cce) {
			//	TODO: make a popup window instead.
			System.err.println("ERROR creating context: " + cce.getMessage());
			return;
		}
		refactoring.refactor(context);
	}
	
}
