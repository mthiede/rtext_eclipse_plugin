/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

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
		} catch (Exception e) {
			Exceptions.rethrow(e);
			return null; //not reachable
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
		} catch (Exception e) {
			Exceptions.rethrow(e);
			return null; //not reachable
		} finally {
			writeLock.unlock();
		}
	}
	
	protected void afterModify(P state, Object result, IUnitOfWork<?, P> work) {}

}