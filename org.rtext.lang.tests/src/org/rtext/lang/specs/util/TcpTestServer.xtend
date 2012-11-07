package org.rtext.lang.specs.util


import java.net.ServerSocket
import java.io.IOException
import java.net.Socket
import java.io.PrintWriter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.CountDownLatch

class TcpTestServer extends Thread{
	
	ServerSocket serverSocket
	String address
	int port
	CountDownLatch signal
	Socket clientSocket = null
	@Property val responses = <String>newArrayList
	PrintWriter out 
	BufferedReader in
	
	new(String address, int port){
		this.address = address
		this.port = port
	}
	
	def start(CountDownLatch signal){
		this.signal = signal
		start
	}
	
	override run(){
	    serverSocket = new ServerSocket(port)
		try {
			signal.countDown
			println("server: started")
		    clientSocket = serverSocket.accept();
		    println("server: client accepted")
		} 
		catch (IOException e) {
		   throw new RuntimeException(e)
		}
		
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
		var c = in.read();
		var length = -1;
		val result = new StringBuilder
		while (c != -1 && c != '{'.charAt(0)) {
			result.append(c as char)
			c = in.read();
		}
		length = Integer::valueOf(result.toString());
		println("server: received " + length);
		val message = new StringBuilder();
		var i = 0
		while(i < length && in.ready){
			message.append(c as char);
			c = in.read();
			i = i + 1
		}
		println("server: received " + message)
		responses.forEach[
			println("server: send " + (it.length + it))
			out.println(it.length + it)
		]
		
	}
	
	def shutdown(){
		serverSocket?.close
		out?.close
		in?.close
		clientSocket?.close
	}
	

}