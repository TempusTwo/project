package org.kdea.sgp.game;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class GameInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		System.out.println("Before Handshake");

		ServletServerHttpRequest ssreq = (ServletServerHttpRequest) request;
		System.out.println("URI:" + request.getURI());

		HttpServletRequest req = ssreq.getServletRequest();
		System.out.println("param, id:" + req.getParameter("nick"));

		// ���� ���ʹ� ó�� �������� ������������ ����ȴ�.
		// ���ͼ��͸� ���ؼ� ����־�� �� ���� ��������� �� �������, ���� ����� ���������� �ʼ��� ����.
		String position = req.getParameter("position");
		String usrNick = req.getParameter("nick");

		attributes.put("usrNick", usrNick);
		attributes.put("position", position);
		
		// �ϴ� ������ ��ġ������ ���� 2�������� ���� ������
		int rnum;
		int gnum;

		// ������ �������� '��'�� ��� ���������� ���۵� '��'�� ��ȣ�� ���� ����������Ѵ�.(��� �ʿ� ������ ����������� �����ؾ� �ϱ� ����)
		if (position.equals("room")) {
			rnum = Integer.parseInt(req.getParameter("rnum"));
			attributes.put("rnum", rnum);
		}
		else if (position.equals("game")) {
			rnum = Integer.parseInt(req.getParameter("rnum"));
			attributes.put("rnum", rnum);
			gnum = Integer.parseInt(req.getParameter("gnum"));
			attributes.put("gnum", gnum);
		}

		

		return super.beforeHandshake(request, response, wsHandler, attributes);
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		System.out.println("After Handshake");

		super.afterHandshake(request, response, wsHandler, ex);
	}

}