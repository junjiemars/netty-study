import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by junjie on 3/5/2015.
 */
public class TimeServer extends ChannelInboundHandlerAdapter {
  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
//    super.channelActive(ctx);
    final ByteBuf time = ctx.alloc().buffer(4); // (2)
    time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

    final ChannelFuture f = ctx.writeAndFlush(time); // (3)
    f.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) {
        assert f == future;
        ctx.close();
      }
    }); // (4)
  }
}
