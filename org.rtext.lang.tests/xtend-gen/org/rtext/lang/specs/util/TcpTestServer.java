package org.rtext.lang.specs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class TcpTestServer extends Thread {
  private ServerSocket serverSocket;
  
  private String address;
  
  private int port;
  
  private CountDownLatch signal;
  
  private Socket clientSocket = null;
  
  private final ArrayList<String> _responses = new Function0<ArrayList<String>>() {
    public ArrayList<String> apply() {
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public ArrayList<String> getResponses() {
    return this._responses;
  }
  
  private PrintWriter out;
  
  private BufferedReader in;
  
  public TcpTestServer(final String address, final int port) {
    this.address = address;
    this.port = port;
  }
  
  public void start(final CountDownLatch signal) {
    this.signal = signal;
    this.start();
  }
  
  public void run() {
    try {
      ServerSocket _serverSocket = new ServerSocket(this.port);
      this.serverSocket = _serverSocket;
      try {
        this.signal.countDown();
        InputOutput.<String>println("server: started");
        Socket _accept = this.serverSocket.accept();
        this.clientSocket = _accept;
        InputOutput.<String>println("server: client accepted");
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          RuntimeException _runtimeException = new RuntimeException(e);
          throw _runtimeException;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      OutputStream _outputStream = this.clientSocket.getOutputStream();
      PrintWriter _printWriter = new PrintWriter(_outputStream, true);
      this.out = _printWriter;
      InputStream _inputStream = this.clientSocket.getInputStream();
      InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
      BufferedReader _bufferedReader = new BufferedReader(_inputStreamReader);
      this.in = _bufferedReader;
      int c = this.in.read();
      int length = (-1);
      StringBuilder _stringBuilder = new StringBuilder();
      final StringBuilder result = _stringBuilder;
      boolean _and = false;
      int _minus = (-1);
      boolean _notEquals = (c != _minus);
      if (!_notEquals) {
        _and = false;
      } else {
        char _charAt = "{".charAt(0);
        boolean _notEquals_1 = (c != _charAt);
        _and = (_notEquals && _notEquals_1);
      }
      boolean _while = _and;
      while (_while) {
        {
          result.append(((char) c));
          int _read = this.in.read();
          c = _read;
        }
        boolean _and_1 = false;
        int _minus_1 = (-1);
        boolean _notEquals_2 = (c != _minus_1);
        if (!_notEquals_2) {
          _and_1 = false;
        } else {
          char _charAt_1 = "{".charAt(0);
          boolean _notEquals_3 = (c != _charAt_1);
          _and_1 = (_notEquals_2 && _notEquals_3);
        }
        _while = _and_1;
      }
      String _string = result.toString();
      Integer _valueOf = Integer.valueOf(_string);
      length = (_valueOf).intValue();
      String _plus = ("server: received " + Integer.valueOf(length));
      InputOutput.<String>println(_plus);
      StringBuilder _stringBuilder_1 = new StringBuilder();
      final StringBuilder message = _stringBuilder_1;
      int i = 0;
      boolean _and_1 = false;
      boolean _lessThan = (i < length);
      if (!_lessThan) {
        _and_1 = false;
      } else {
        boolean _ready = this.in.ready();
        _and_1 = (_lessThan && _ready);
      }
      boolean _while_1 = _and_1;
      while (_while_1) {
        {
          message.append(((char) c));
          int _read = this.in.read();
          c = _read;
          int _plus_1 = (i + 1);
          i = _plus_1;
        }
        boolean _and_2 = false;
        boolean _lessThan_1 = (i < length);
        if (!_lessThan_1) {
          _and_2 = false;
        } else {
          boolean _ready_1 = this.in.ready();
          _and_2 = (_lessThan_1 && _ready_1);
        }
        _while_1 = _and_2;
      }
      String _plus_1 = ("server: received " + message);
      InputOutput.<String>println(_plus_1);
      ArrayList<String> _responses = this.getResponses();
      final Procedure1<String> _function = new Procedure1<String>() {
          public void apply(final String it) {
            int _length = it.length();
            String _plus = (Integer.valueOf(_length) + it);
            String _plus_1 = ("server: send " + _plus);
            InputOutput.<String>println(_plus_1);
            int _length_1 = it.length();
            String _plus_2 = (Integer.valueOf(_length_1) + it);
            TcpTestServer.this.out.println(_plus_2);
          }
        };
      IterableExtensions.<String>forEach(_responses, _function);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void shutdown() {
    try {
      if (this.serverSocket!=null) this.serverSocket.close();
      if (this.out!=null) this.out.close();
      if (this.in!=null) this.in.close();
      if (this.clientSocket!=null) this.clientSocket.close();
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
