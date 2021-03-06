import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by junjie on 3/4/2015.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//    super.channelRead(ctx, msg);
//    ((ByteBuf)msg).release();
    ByteBuf in = (ByteBuf) msg;
    try {
      while (in.isReadable()) { // (1)
        System.out.print((char) in.readByte());
        System.out.flush();
      }
    } finally {
      ReferenceCountUtil.release(msg); // (2)
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
    ctx.close();
  }
}


