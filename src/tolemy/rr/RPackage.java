package tolemy.rr;

import java.util.HashSet;


/**
 * @author rawley
 * A package.
 */
public class RPackage {
	
	/**
	 * @author rawley
	 * The type of exception thrown when attempting to create a package that already exists.
	 */
	public class RPackageCreationException extends Exception {
		private static final long serialVersionUID = 2247892133287024712L;
		
		public RPackageCreationException() {
			initCause(null);
		}
		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return "Attempted to create a package "
					+ RPackage.this.toString()
					+ " which already exists.";
		}
	}
	
	
	/**
	 * The default package.
	 */
	final static RPackage defaultPackage = new RPackage(null, null);
	
	/**
	 * The package that the current package is nested in.
	 */
	RPackage parent;
	
	/**
	 * Stores the name of the package.  For example, name would be foo for the package com.example.foo.
	 */
	String name;
	
	/**
	 * The children packages of this package.
	 */
	HashSet<RPackage> children = new HashSet<>();
	
	/**
	 * In general, RPackage.createPackage(..) should be used.  This does no duplication checks.
	 * @param parent the parent of the package.  
	 * @param name the name of the package
	 */
	RPackage(RPackage parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	/**
	 * Returns the singular default package.
	 */
	public static RPackage getDefaultPackage() {
		return defaultPackage;
	}
	
	/**
	 * @param name the name of the new package, nested within this.
	 * @return the created package.
	 * @throws RPackageCreationException when a package with that name already exists.
	 */
	public RPackage createPackage(String name) throws RPackageCreationException {
		//	Check for duplicates.
		RPackage created = new RPackage(this, name);
		for (RPackage pkg : children) {
			if (created.equals(pkg)) {
				throw pkg.new RPackageCreationException();
			}
		}
		children.add(created);
		return created;
	}
	
	/**
	 * @return an array of the children of this package.  Mutating this array will have no effect.
	 */
	@SuppressWarnings("unchecked")
	public HashSet<RPackage> getChildren() {
		return (HashSet<RPackage>) children.clone();
	}
	
	/**
	 * @return the parent package of the current package.  Will be null if the package has no parent.
	 */
	public RPackage getParent() {
		return parent;
	}
	
	@Override
	public String toString() {
		if (this == defaultPackage)
			return "(default package)";
		else
			return new StringBuilder(parent.toString()).append('.').append(name).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RPackage other = (RPackage) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
}
