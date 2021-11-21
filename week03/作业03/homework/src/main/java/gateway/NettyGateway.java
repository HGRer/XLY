package gateway;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import gateway.filter.HttpRequestFilter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyGateway {
	public static void main(String[] args) {
		int port = 8888;
		
		EventLoopGroup mainGroup = new NioEventLoopGroup(2);
		EventLoopGroup subGroup = new NioEventLoopGroup(8);
		
		ServerBootstrap b = new ServerBootstrap();
		b.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.SO_REUSEADDR, true)
			.childOption(ChannelOption.SO_RCVBUF, 32 * 1024).childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
			.childOption(EpollChannelOption.SO_REUSEPORT, true).childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		b.group(mainGroup, subGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new HttpInboundInitializer());
		
		try {
			Channel ch = b.bind(port).sync().channel();
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			mainGroup.shutdownGracefully();
			subGroup.shutdownGracefully();
		}
	}
	
	static class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpServerCodec());
			pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
			pipeline.addLast(new HttpInboundHandler());
		}
	}
	
	static class HttpInboundHandler extends ChannelInboundHandlerAdapter {
		HttpOutboundHandler outboundHandler = new HttpOutboundHandler();
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			FullHttpRequest fullRequest = (FullHttpRequest) msg;
			HttpRequestFilter.getInstance().addSpecialHeader(fullRequest); // request filter
			outboundHandler.handler(fullRequest, ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}
	}
	
	static class HttpOutboundHandler {
		public void handler(HttpRequest req, ChannelHandlerContext ctx) {
			FullHttpResponse gatewayResponse = null;
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet("http://127.0.0.1:8801"); // no route
			httpget.setHeader("header-test", req.headers().get("header-test"));
			try {
				CloseableHttpResponse response = httpclient.execute(httpget);
				byte[] byteArray = new byte[(int) response.getEntity().getContentLength()];
				response.getEntity().getContent().read(byteArray);
				gatewayResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(byteArray));
				gatewayResponse.headers().set("Content-Type", "application/json");
				gatewayResponse.headers().setInt("Content-Length", gatewayResponse.content().readableBytes());
				
				response.close();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				gatewayResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
			} catch (IOException e) {
				e.printStackTrace();
				gatewayResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
			} finally {
				if (req != null) {
					if (!HttpUtil.isKeepAlive(req)) {
						ctx.write(gatewayResponse).addListener(ChannelFutureListener.CLOSE);
					} else {
						gatewayResponse.headers().set(CONNECTION, KEEP_ALIVE);
						ctx.write(gatewayResponse);
					}
					ctx.flush();
				}
			}
		}
	}
}
