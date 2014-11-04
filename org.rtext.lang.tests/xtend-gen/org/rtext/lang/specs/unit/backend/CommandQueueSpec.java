package org.rtext.lang.specs.unit.backend;

import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.runner.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.CommandQueue;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.util.Commands;

@Named("CommandQueue")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class CommandQueueSpec {
  @Subject
  public CommandQueue subject;
  
  @Test
  @Named("queues commands and callbacks")
  @Order(1)
  public void _queuesCommandsAndCallbacks() throws Exception {
    Callback<Response> __ = Should.<Callback<Response>>_();
    final CommandQueue.Task<Response> task = CommandQueue.Task.<Response>create(Commands.ANY_COMMAND, __);
    this.subject.add(task);
    int _size = this.subject.size();
    boolean _doubleArrow = Should.<Integer>operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(1));
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
    CommandQueue.Task<?> _take = this.subject.take();
    Assert.assertTrue("\nExpected subject.take => task but"
     + "\n     subject.take is " + new org.hamcrest.StringDescription().appendValue(_take).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
     + "\n     task is " + new org.hamcrest.StringDescription().appendValue(task).toString() + "\n", Should.<CommandQueue.Task<?>>operator_doubleArrow(_take, task));
    
  }
  
  @Test
  @Named("take removes tasks")
  @Order(2)
  public void _takeRemovesTasks() throws Exception {
    Callback<Response> __ = Should.<Callback<Response>>_();
    final CommandQueue.Task<Response> task = CommandQueue.Task.<Response>create(Commands.ANY_COMMAND, __);
    this.subject.add(task);
    CommandQueue.Task<?> _take = this.subject.take();
    boolean _doubleArrow = Should.<CommandQueue.Task<?>>operator_doubleArrow(_take, task);
    Assert.assertTrue("\nExpected subject.take => task but"
     + "\n     subject.take is " + new org.hamcrest.StringDescription().appendValue(_take).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
     + "\n     task is " + new org.hamcrest.StringDescription().appendValue(task).toString() + "\n", _doubleArrow);
    
    int _size = this.subject.size();
    boolean _doubleArrow_1 = Should.<Integer>operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected subject.size => 0 but"
     + "\n     subject.size is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow_1);
    
    this.subject.add(task);
    int _size_1 = this.subject.size();
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_size_1)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Integer>operator_doubleArrow(Integer.valueOf(_size_1), Integer.valueOf(1)));
    
  }
  
  @Test
  @Named("duplicate commands and callbacks are discared")
  @Order(3)
  public void _duplicateCommandsAndCallbacksAreDiscared() throws Exception {
    Command<Response> _newCommand = Commands.newCommand("a");
    Callback<Response> __ = Should.<Callback<Response>>_();
    CommandQueue.Task<Response> _create = CommandQueue.Task.<Response>create(_newCommand, __);
    this.subject.add(_create);
    Command<Response> _newCommand_1 = Commands.newCommand("a");
    Callback<Response> ___1 = Should.<Callback<Response>>_();
    CommandQueue.Task<Response> _create_1 = CommandQueue.Task.<Response>create(_newCommand_1, ___1);
    this.subject.add(_create_1);
    int _size = this.subject.size();
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Integer>operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(1)));
    
  }
  
  @Test
  @Named("clear all tasks")
  @Order(4)
  public void _clearAllTasks() throws Exception {
    Callback<Response> __ = Should.<Callback<Response>>_();
    CommandQueue.Task<Response> _create = CommandQueue.Task.<Response>create(Commands.ANY_COMMAND, __);
    this.subject.add(_create);
    this.subject.clear();
    int _size = this.subject.size();
    boolean _doubleArrow = Should.<Integer>operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected subject.size => 0 but"
     + "\n     subject.size is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
    Callback<Response> ___1 = Should.<Callback<Response>>_();
    CommandQueue.Task<Response> _create_1 = CommandQueue.Task.<Response>create(Commands.ANY_COMMAND, ___1);
    this.subject.add(_create_1);
    int _size_1 = this.subject.size();
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_size_1)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Integer>operator_doubleArrow(Integer.valueOf(_size_1), Integer.valueOf(1)));
    
  }
}
