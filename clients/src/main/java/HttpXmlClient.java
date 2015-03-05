import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;

/**
 * Created by junjie on 3/5/2015.
 */
public class HttpXmlClient {
    public static void main(String[] args) throws Exception {
    URI uri = new URI(args[0]);
    String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
    int port = uri.getPort() == -1 ? 8080 : uri.getPort();

     // Configure the client.
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
      .handler(new HttpXmlClientHandler());
          //.handler(new HttpSnoopClientInitializer(sslCtx));

      // Make the connection attempt.
      Channel ch = b.connect(host, port).sync().channel();

      // Prepare the HTTP request.
      HttpRequest request = new DefaultFullHttpRequest(
          HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
      request.headers().set(HttpHeaderNames.HOST, host);
      request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
      request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

      // Set some example cookies.
      request.headers().set(
          HttpHeaderNames.COOKIE,
          ClientCookieEncoder.encode(
              new DefaultCookie("my-cookie", "foo"),
              new DefaultCookie("another-cookie", "bar")));

      // Send the HTTP request.
      ch.writeAndFlush(request);

      // Wait for the server to close the connection.
      ch.closeFuture().sync();
    } finally {
      // Shut down executor threads to exit.
      group.shutdownGracefully();
    }
  }
}
