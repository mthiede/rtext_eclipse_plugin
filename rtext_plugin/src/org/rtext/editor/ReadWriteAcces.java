package org.rtext.editor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ReadWriteAcces<P>{

	protected abstract P getState();

	protected final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	protected final Lock writeLock = rwLock.writeLock();
	protected final Lock readLock = rwLock.readLock();

	public <T> T readOnly(IUnitOfWork<T, P> work) {
		readLock.lock();
		try {
			P state = getState();
			T exec = work.exec(state);
			return exec;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			readLock.unlock();
		}
	}

	public <T> T modify(IUnitOfWork<T, P> work) {
		writeLock.lock();
		try {
			P state = getState();
			T exec = work.exec(state);
			afterModify(state,exec,work);
			return exec;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			writeLock.unlock();
		}
	}
	
	protected void afterModify(P state, Object result, IUnitOfWork<?, P> work) {}

}