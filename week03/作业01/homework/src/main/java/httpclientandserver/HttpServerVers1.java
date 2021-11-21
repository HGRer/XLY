package httpclientandserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerVers1 {
	/*sb -u "http://localhost:8801" -c 8 -N 30
		Requests: 8801
		RPS: 583.8
		90th Percentile: 19ms
		95th Percentile: 21ms
		99th Percentile: 25ms
		Average: 12.7ms
		Min: 1ms
		Max: 103ms
	 * */
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(8801);
			while(true) {
				Socket socket = serverSocket.accept();
				print(socket);
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
