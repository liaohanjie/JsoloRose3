package com.websocket;

import java.nio.charset.Charset;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
/**
 * 消息接受处理类
 * @author -琴兽-
 *
 */
public class websocketHandler extends SimpleChannelHandler {
  
    private WebSocketServerHandshaker handshaker;  
  
    @Override  
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {  
        Object msg = e.getMessage();  
        //如果是http请求
        if (msg instanceof HttpRequest) {  
            handleHttpRequest(ctx, (HttpRequest) msg);  
        //如果是websocket请求
        } else if (msg instanceof WebSocketFrame) {  
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);  
        }  
    }  
  
    /**
     * 响应http请求
     * @param ctx
     * @param req
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
    	//websocket协议工厂
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("", null, false);
        //客户端websocket版本对应的处理类
        handshaker = wsFactory.newHandshaker(req);
        //没有则通知客户端不支持
        if (handshaker == null) {  
            wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.getChannel());
        //回复客户端ok，并将编解码器置换成websocket的编解码器(客户端从下次开始，将采用websocket协议进行通信)
        } else {  
            handshaker.handshake(ctx.getChannel(), req);  
        }  
          
    }  
  
    /**
     * 处理websocket请求
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {  
    	//客户端请求关闭
        if (frame instanceof CloseWebSocketFrame) {  
            handshaker.close(ctx.getChannel(), (CloseWebSocketFrame) frame);  
            return;
        //接受消息
        }else{
        	//打印接收到消息
        	String msg = frame.getBinaryData().toString(Charset.defaultCharset());
        	System.out.println(frame.getBinaryData().toString(Charset.defaultCharset()));
        	
        	//回复消息
        	TextWebSocketFrame socketFrame = new TextWebSocketFrame(msg);
        	ctx.getChannel().write(socketFrame);
        }
    }  
}
