/**
 * The input to refactoring scripts.  It allows accessing what the user was doing when it invoked the script.  
 */
package tolemy.rr;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;

import tolemy.internal.InsideGatherer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.*;

/**
 * @author rawley
 *
 */
public class Context {
	
	HashSet<CompilationUnit> compilationUnits = new HashSet<>();
	ArrayList<ASTNode> relevantASTs;
	ArrayList<IBinding> relevantBindings;
	HashSet<RPackage> topLevelPackages = new HashSet<>();
	
	/**
	 * 
	 */
	public Context(IProject project, IDocument document, ITextSelection selection) throws ContextCreationException {
		PackageDeclaration selectedPackage;
		TypeDeclaration publicType;
		{
			ASTParser parser = ASTParser.newParser(AST.JLS10);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(document.get().toCharArray());
			CompilationUnit selectedCU = (CompilationUnit)parser.createAST(null);
			selectedPackage = selectedCU.getPackage();
			publicType = getPublicType(selectedCU);
		}
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		ArrayList<ICompilationUnit> sources = new ArrayList<ICompilationUnit>();
		IJavaProject ijp = JavaCore.create(project);
		assert ijp != null;
		parser.setProject(ijp);
		IPackageFragment[] fragments;
		try {
			 fragments = ijp.getPackageFragments();
		} catch (JavaModelException jme) {
			throw new ContextCreationException("Unable to get packages.", jme);
		}
		for (IPackageFragment fragment : fragments) {
			try {
				 sources.addAll(Arrays.asList(fragment.getCompilationUnits()));
			} catch (JavaModelException jme) {
				throw new ContextCreationException("Unable to get compilation units.", jme);
			}
		} 
		class MyASTRequestor extends ASTRequestor {
			boolean foundSelectedCompilationUnit = false;
			@Override
			public void acceptAST(ICompilationUnit source, CompilationUnit ast) {
				CompilationUnit cu = (CompilationUnit)ast;
				compilationUnits.add(cu);
				if (eq(selectedPackage, ast.getPackage()) && eq(publicType, getPublicType(cu))) {
					assert !foundSelectedCompilationUnit;
					foundSelectedCompilationUnit = true;
					InsideGatherer ig = new InsideGatherer(selection.getOffset());
					cu.accept(ig);
					relevantASTs = ig.relevantASTs;
					relevantBindings = ig.relevantBindings;
				}
			}
		}
		MyASTRequestor requestor = new MyASTRequestor();
		parser.createASTs(
				sources.toArray(new ICompilationUnit[] {}), 
				new String[] {}, 
				requestor,
				null);
		assert requestor.foundSelectedCompilationUnit;
		
	}
	
	/**
	 * @return the package being edited.
	 */
	public RPackage getCurrentPackage() {
		//	TODO: finish
		return null;
	}
	
	TypeDeclaration getPublicType(CompilationUnit cu) {
		for (Object t : cu.types()) {
			TypeDeclaration type = (TypeDeclaration)t;
			if (Modifier.isPublic(type.getModifiers())) {
				return type;
			}
		}
		assert false;
		return null;
	}
	
	boolean eq(Object l, Object r) {
		if (l == null && r == null) {
			return true;
		} else if (l != null && r != null) {
			return l.equals(r);
		} else {
			return false;
		}
	}

}
