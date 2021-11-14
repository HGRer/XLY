package httpclientandserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(8801);
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("request comes");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
