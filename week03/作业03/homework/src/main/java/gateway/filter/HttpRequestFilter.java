package gateway.filter;

import io.netty.handler.codec.http.HttpRequest;

public class HttpRequestFilter {
	private static HttpRequestFilter instance = new HttpRequestFilter();
	private HttpRequestFilter() {}
	
	public static HttpRequestFilter getInstance() {
		return instance;
	};
	
	public void addSpecialHeader(HttpRequest req) {
		if (req != null) {
			req.headers().set("header-test", "from gateway");
		}
	}
}
