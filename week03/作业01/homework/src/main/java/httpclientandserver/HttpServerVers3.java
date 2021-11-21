package httpclientandserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerVers3 {
	/*
	 * Requests: 20012
		RPS: 643.5
		90th Percentile: 17ms
		95th Percentile: 19ms
		99th Percentile: 23ms
		Average: 10.8ms
		Min: 1ms
		Max: 135ms
	 * */
	public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                32);
        
        try {
			ServerSocket serverSocket = new ServerSocket(8801);
			while(true) {
				Socket socket = serverSocket.accept();
				executorService.execute(() -> {
					try {
						print(socket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}); 
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
