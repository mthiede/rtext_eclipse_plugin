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
import org.eclipse.xtend.lib.Property;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class TcpTestServer extends Thread {
  private ServerSocket serverSocket;
  
  private String address;
  
  private int port;
  
  private CountDownLatch signal;
  
  private Socket clientSocket = null;
  
  @Property
  private final ArrayList<String> _responses = CollectionLiterals.<String>newArrayList();
  
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
          throw new RuntimeException(e);
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
      final StringBuilder result = new StringBuilder();
      while (((c != (-1)) && (c != "{".charAt(0)))) {
        {
          result.append(((char) c));
          int _read = this.in.read();
          c = _read;
        }
      }
      String _string = result.toString();
      Integer _valueOf = Integer.valueOf(_string);
      length = (_valueOf).intValue();
      InputOutput.<String>println(("server: received " + Integer.valueOf(length)));
      final StringBuilder message = new StringBuilder();
      int i = 0;
      while (((i < length) && new Function0<Boolean>() {
        public Boolean apply() {
          try {
            boolean _ready = TcpTestServer.this.in.ready();
            return _ready;
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      }.apply().booleanValue())) {
        {
          message.append(((char) c));
          int _read = this.in.read();
          c = _read;
          i = (i + 1);
        }
      }
      InputOutput.<String>println(("server: received " + message));
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
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void shutdown() {
    try {
      if (this.serverSocket!=null) {
        this.serverSocket.close();
      }
      if (this.out!=null) {
        this.out.close();
      }
      if (this.in!=null) {
        this.in.close();
      }
      if (this.clientSocket!=null) {
        this.clientSocket.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Pure
  public ArrayList<String> getResponses() {
    return this._responses;
  }
}
