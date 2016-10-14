package org.renatofq.linker;

public interface ObjectLinker {

	public <A, B> void link(A a, B b);
	
	public <A, B> void fill(A a, B b) throws LinkNotFoundException;
}
