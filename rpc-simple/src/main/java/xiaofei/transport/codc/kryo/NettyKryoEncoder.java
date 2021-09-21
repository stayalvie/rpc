package xiaofei.transport.codc.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import xiaofei.serializer.Serializer;

/**
 * @author xiaofei
 * @create 2021-09-18 18:40
 */

@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer;
    private Class<?> genericClass;

    /*
    *
    * 目前规定的协议是 ： 头部四个字节是长度 后面是具体的
    * */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        if (genericClass.isInstance(msg)) {

            //序列化消息体
            byte[] body = serializer.serialize(msg);

            //写入消息的长度
            out.writeInt(body.length);

            //将消息体写入
            out.writeBytes(body);
        }

    }
}
