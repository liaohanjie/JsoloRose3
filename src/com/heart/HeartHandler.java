package com.heart;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
/**
 * 消息接受处理类
 * @author -琴兽-
 *
 */
public class HeartHandler extends IdleStateAwareChannelHandler {

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		
		System.out.println(e.getState());
		
	}

}
