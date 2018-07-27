/**
 * 
 */
package tolemy.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import tolemy.rr.ReflectiveRefactoring;

/**
 * @author rawley
 *
 */
public class JavaContextMenu extends ContributionItem {

	/**
	 * 
	 */
	public JavaContextMenu() {
		super();
	}

	/**
	 * @param id
	 */
	public JavaContextMenu(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void fill(Menu menu, int index) {
		for (IConfigurationElement e : Platform.getExtensionRegistry().getConfigurationElementsFor("tolemy.rr")) {
			System.out.println(e.toString());
			Object pluginObject;
			try {
				pluginObject = e.createExecutableExtension("class");
			} catch (CoreException ce) {
				MessageDialog.openError(
						menu.getShell(), 
						"Could not get class", 
						ce.getMessage());
				continue;
			}
			try {
				ReflectiveRefactoring rr = (ReflectiveRefactoring)pluginObject;
				MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText(rr.getName());
				item.addSelectionListener(new RefactorCallback(rr));
			} catch (ClassCastException cce) {
				MessageDialog.openInformation(
						menu.getShell(), 
						"EXTENSION ERROR", 
						String.join(
								"Tolemy expects extensions to provide a class that implements ",
								"tolemy.rr.ReflectiveRefactoring, but the plugin ",
								e.getName(),
								" provides the class ",
								pluginObject.getClass().getName(),
								" which does not implement it."));
			}
		}
		super.fill(menu, index);
	}

}
