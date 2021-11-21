package gateway.appserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppHttpServer {
	public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        try {
			ServerSocket serverSocket = new ServerSocket(8801);
			while(true) {
				Socket socket = serverSocket.accept();
				executorService.execute(() -> {
					try {
						showInboundMsg(socket);
						print(socket);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (socket != null) {
							try {
								socket.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void showInboundMsg(Socket socket) throws IOException {
		byte[] byteArray = new byte[1024 * 2];
		socket.getInputStream().read(byteArray);
		System.out.println("inboungMsg ->");
		System.out.println(new String(byteArray, "UTF-8"));
	}
	
	public static void print(Socket socket) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(socket.getOutputStream());
		sw.write("HTTP/1.1 200 OK\n");
		sw.write("Content-Type:text/html;charset=utf-8\n");
		sw.write("Content-length:" + "This is the App Server".length());
		sw.write("\n\n");
		sw.write("This is the App Server");
		sw.flush();
		sw.close();
		socket.close();
	}
}
