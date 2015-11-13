package org.kdea.sgp.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tiles.request.Request;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game/")
public class GameController {
	//��Ʈ�ѷ� Ŭ������ �Ϲ����� ��ɸ��� �þƼ� �����Ѵ�. ���� ����� ����, ������ �̵����� �Ϲ����� ��ɸ� ������ ����

	@RequestMapping(value = "roomList", method = RequestMethod.GET)
	public String roomList(HttpServletRequest request) {
		request.getSession().removeAttribute("rnum");
		return "game/roomList";
	}

	@RequestMapping(value = "roomIn", method = RequestMethod.GET)
	public String room(HttpServletRequest request, @RequestParam("rnum") int rnum) {
		request.getSession().setAttribute("rnum", rnum);
		request.getSession().removeAttribute("gnum");
		return "game/room";
	}

	@RequestMapping(value = "gameStart", method = RequestMethod.GET)
	public String gameStart(HttpServletRequest request, @RequestParam("gnum") int gnum) {
		request.getSession().setAttribute("gnum", gnum);
		return "game/game";
	}

	@RequestMapping(value = "testlogin", method = RequestMethod.POST)
	public String testLogin(HttpServletRequest request, @RequestParam("nick") String nick) {
		request.getSession().setAttribute("nick", nick);
		System.out.println(nick);
		return "game/roomList";
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String testLoginForm() {
		return "game/TestLogin";
	}
}