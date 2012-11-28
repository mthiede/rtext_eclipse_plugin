package org.rtext.lang.backend;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.Response;

public class CommandQueue {
	
	public static class Task<T extends Response> {
		public static <T extends Response> Task<T> create(Command<T> command, Callback<T> callback){
			return new Task<T>(command, callback);
		}
		public final Command<T> command;
		public final Callback<T> callback;

		public Task(Command<T> command, Callback<T> callback) {
			this.command = command;
			this.callback = callback;
		}
	}
	
	private BlockingQueue<Task<?>> tasks = new LinkedBlockingQueue<Task<?>>();
	private Set<Command<?>> commands = new HashSet<Command<?>>();

	public void add(Task<?> task){
		if(commands.contains(task.command)){
			return;
		}
		commands.add(task.command);
		tasks.add(task);
	}

	public Task<?> take() throws InterruptedException{
		Task<?> result = tasks.take();
		commands.remove(result.command);
		return result;
	}

	public int size(){
		return tasks.size();
	}

	public void clear() {
		tasks.clear();
		commands.clear();
	}
}
