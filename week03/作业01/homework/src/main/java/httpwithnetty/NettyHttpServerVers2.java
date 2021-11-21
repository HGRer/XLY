package httpwithnetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyHttpServerVers2 {
	/*
	 * Requests: 224833
		RPS: 7221.9
		90th Percentile: 0ms
		95th Percentile: 0ms
		99th Percentile: 0ms
		Average: 0ms
		Min: 0ms
		Max: 194ms
		Max: 207m
	 * */
	public static void main(String[] args) {
		int port = 8801;

		EventLoopGroup eventGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();
		b.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.SO_REUSEADDR, true)
				.childOption(ChannelOption.SO_RCVBUF, 32 * 1024).childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
				.childOption(EpollChannelOption.SO_REUSEPORT, true).childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		b.group(eventGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new HttpInitializer());
		
		try {
			Channel ch = b.bind(port).sync().channel();
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			eventGroup.shutdownGracefully();
		}
	}
}
