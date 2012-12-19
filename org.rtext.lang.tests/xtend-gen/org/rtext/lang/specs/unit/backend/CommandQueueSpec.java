package org.rtext.lang.specs.unit.backend;

import org.hamcrest.StringDescription;
import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.runner.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.CommandQueue;
import org.rtext.lang.backend.CommandQueue.Task;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.util.Commands;

@SuppressWarnings("all")
@Named("CommandQueue")
@RunWith(ExampleGroupRunner.class)
public class CommandQueueSpec {
  @Subject
  public CommandQueue subject;
  
  @Test
  @Named("queues commands and callbacks")
  @Order(1)
  public void _queuesCommandsAndCallbacks() throws Exception {
    Callback<Response> __ = Should.<Callback<Response>>_();
    final Task<Response> task = Task.<Response>create(Commands.ANY_COMMAND, __);
    this.subject.add(task);
    int _size = this.subject.size();
    boolean _doubleArrow = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(1));
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
    Task<? extends Object> _take = this.subject.take();
    boolean _doubleArrow_1 = Should.operator_doubleArrow(_take, task);
    Assert.assertTrue("\nExpected subject.take => task but"
     + "\n     subject.take is " + new StringDescription().appendValue(_take).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     task is " + new StringDescription().appendValue(task).toString() + "\n", _doubleArrow_1);
    
  }
  
  @Test
  @Named("take removes tasks")
  @Order(2)
  public void _takeRemovesTasks() throws Exception {
    Callback<Response> __ = Should.<Callback<Response>>_();
    final Task<Response> task = Task.<Response>create(Commands.ANY_COMMAND, __);
    this.subject.add(task);
    Task<? extends Object> _take = this.subject.take();
    boolean _doubleArrow = Should.operator_doubleArrow(_take, task);
    Assert.assertTrue("\nExpected subject.take => task but"
     + "\n     subject.take is " + new StringDescription().appendValue(_take).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     task is " + new StringDescription().appendValue(task).toString() + "\n", _doubleArrow);
    
    int _size = this.subject.size();
    boolean _doubleArrow_1 = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected subject.size => 0 but"
     + "\n     subject.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow_1);
    
    this.subject.add(task);
    int _size_1 = this.subject.size();
    boolean _doubleArrow_2 = Should.operator_doubleArrow(Integer.valueOf(_size_1), Integer.valueOf(1));
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new StringDescription().appendValue(Integer.valueOf(_size_1)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow_2);
    
  }
  
  @Test
  @Named("duplicate commands and callbacks are discared")
  @Order(3)
  public void _duplicateCommandsAndCallbacksAreDiscared() throws Exception {
    Command<Response> _newCommand = Commands.newCommand("a");
    Callback<Response> __ = Should.<Callback<Response>>_();
    Task<Response> _create = Task.<Response>create(_newCommand, __);
    this.subject.add(_create);
    Command<Response> _newCommand_1 = Commands.newCommand("a");
    Callback<Response> ___1 = Should.<Callback<Response>>_();
    Task<Response> _create_1 = Task.<Response>create(_newCommand_1, ___1);
    this.subject.add(_create_1);
    int _size = this.subject.size();
    boolean _doubleArrow = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(1));
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("clear all tasks")
  @Order(4)
  public void _clearAllTasks() throws Exception {
    Callback<Response> __ = Should.<Callback<Response>>_();
    Task<Response> _create = Task.<Response>create(Commands.ANY_COMMAND, __);
    this.subject.add(_create);
    this.subject.clear();
    int _size = this.subject.size();
    boolean _doubleArrow = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected subject.size => 0 but"
     + "\n     subject.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
    Callback<Response> ___1 = Should.<Callback<Response>>_();
    Task<Response> _create_1 = Task.<Response>create(Commands.ANY_COMMAND, ___1);
    this.subject.add(_create_1);
    int _size_1 = this.subject.size();
    boolean _doubleArrow_1 = Should.operator_doubleArrow(Integer.valueOf(_size_1), Integer.valueOf(1));
    Assert.assertTrue("\nExpected subject.size => 1 but"
     + "\n     subject.size is " + new StringDescription().appendValue(Integer.valueOf(_size_1)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow_1);
    
  }
}
