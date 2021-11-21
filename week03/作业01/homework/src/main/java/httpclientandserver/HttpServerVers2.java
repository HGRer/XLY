package httpclientandserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerVers2 {
	/*
	 * Requests: 19610
		RPS: 630.6
		90th Percentile: 18ms
		95th Percentile: 20ms
		99th Percentile: 23ms
		Average: 11.1ms
		Min: 1ms
		Max: 145ms
	 * */
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(8801);
			while(true) {
				Socket socket = serverSocket.accept();
				new Thread(() -> {
					try {
						print(socket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print(Socket socket) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(socket.getOutputStream());
		sw.write("HTTP/1.1 200 OK\n");
		sw.write("Content-Type:text/html;charset=utf-8\n");
		sw.write("Content-length:" + "Hello world".length());
		sw.write("\n\n");
		sw.write("Hello world");
		sw.flush();
		sw.close();
		socket.close();
	}
}
