/**
 * 
 */
package tolemy.internal;

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.*;

import tolemy.rr.*;
import tolemy.rr.RPackage.RPackageCreationException;

/**
 * @author rawley
 * Converts the ASTs into a more refactorable form.
 */
public class Converter extends ASTVisitor {
	public HashSet<RPackage> allPackages;
	/**
	 * 
	 */
	public Converter() {
	}
	
	public boolean visit(PackageDeclaration declaration) {
		IPackageBinding binding = declaration.resolveBinding();
		if (binding.isUnnamed()) {
			allPackages.add(RPackage.getDefaultPackage());
		} else {
			String[] components = binding.getNameComponents();
			RPackage last = new RPackage(null, components[0]);
			allPackages.add(last);
			
			for (String component : Arrays.copyOfRange(components, 1, components.length)) {
				try {
					RPackage current = last.createPackage(component);
					allPackages.add(current);
					last = current;
				} catch (RPackageCreationException rpce) {
					last = rpce.getPreexisting();
				}
			}
		}
		return true;
	}
	public boolean visit(ImportDeclaration declaration) {
		System.out.println(declaration.resolveBinding().getClass().getInterfaces());
		return false;
	}
}
