package httpclientandserver;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientTest {
	public static void main(String[] args) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://127.0.0.1:8801");
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			byte[] byteArray = new byte[(int) response.getEntity().getContentLength()];
			response.getEntity().getContent().read(byteArray);
			
			System.out.println(new String(byteArray, "utf8"));
			response.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
