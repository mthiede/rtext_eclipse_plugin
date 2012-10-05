package org.rtext.editor;


public interface IUnitOfWork<R,P> {

	R exec(P state) throws Exception;
	
	public static abstract class Void<T> implements IUnitOfWork<Object,T> {
		public final Object exec(T state) throws Exception {
			process(state);
			return null;
		}

		public abstract void process(T state) throws Exception;
	}
	
}

