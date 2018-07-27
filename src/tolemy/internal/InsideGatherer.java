/**
 * 
 */
package tolemy.internal;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.*;


/**
 * @author rawley
 *
 */
public class InsideGatherer extends ASTVisitor {
	int cursorPosition;
	public ArrayList<ASTNode> relevantASTs = new ArrayList<>();
	public ArrayList<IBinding> relevantBindings = new ArrayList<>();
	/**
	 * 
	 */
	public InsideGatherer(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}
	@Override
	public boolean preVisit2(ASTNode node) {
		if (selected(node)) {
			relevantASTs.add(node);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean visit(AbstractTypeDeclaration declaration) {
		if (selected(declaration)) {
			relevantBindings.add(declaration.resolveBinding());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean visit(MethodDeclaration declaration) {
		if (selected(declaration)) {
			relevantBindings.add(declaration.resolveBinding());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean visit(SimpleName name) {
		if (selected(name) ) {
			relevantBindings.add(name.resolveBinding());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean visit(EnumConstantDeclaration declaration) {
		if (selected(declaration)) {
			relevantBindings.add(declaration.resolveConstructorBinding());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean visit(PackageDeclaration declaration) {
		if (selected(declaration)) {
			relevantBindings.add(declaration.resolveBinding());
			return true;
		} else {
			return false;
		}
	}
	
	boolean selected(ASTNode node) {
		return node.getStartPosition() >= cursorPosition && node.getStartPosition() + node.getLength() <= cursorPosition;
	}
	
}
