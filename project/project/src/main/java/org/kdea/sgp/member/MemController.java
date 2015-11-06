package org.kdea.sgp.member;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mem")
public class MemController {

	@Autowired
	private MemService memService;
	
	@RequestMapping("/editpage")	
	public String editpage(){

		
		return "memInfoedit/memInfopage";
	}
	
	@RequestMapping(value="/pwEdit", method=RequestMethod.POST)
	@ResponseBody
	public String pwEdit(BoardVO bvo){
		System.out.println("��й�ȣ Ȯ��:"+bvo.getPw());
		if(memService.editPWcheck(bvo.getPw())&&memService.editIDcheck(bvo.getId())){
			System.out.println("��й�ȣ, ���̵� üũ Ȯ��");
			return "{\"ok\":"+memService.updatePW(bvo)+"}";
		}
		return "{\"ok\":false}";
		
	}
	
	@RequestMapping(value="/nickcheck", method=RequestMethod.POST)
	@ResponseBody
	public String nickcheck(BoardVO bvo){
		System.out.println("�г��� �ߺ�Ȯ�μҽ�:"+bvo.getNick());
		return "{\"ok\":"+memService.editNickcheck(bvo.getNick())+"}";
	}
	
	@RequestMapping(value="nickRegi", method=RequestMethod.POST)
	@ResponseBody
	public String nickRegi(BoardVO bvo){
		
		return "{\"ok\":"+memService.newNickRegi(bvo)+"}";
	}
	
	@RequestMapping(value="emailEdit", method=RequestMethod.POST)
	@ResponseBody
	public String emailcheck(BoardVO bvo){
		System.out.println("�����ּ� Ȯ��:"+bvo.getEmail());
		
		return "{\"ok\":"+memService.emailEdit(bvo)+"}";
	}
	
	@RequestMapping("/searchID")
	public String searchID(){
		
		return "memInfoedit/IdSearchPage";
	}
	
	@RequestMapping("/searchPW")
	public String searchPW(){
		
		return "memInfoedit/PwSearchPage"; 
	}
	
	@RequestMapping(value="/idsearchget", method=RequestMethod.POST)
	@ResponseBody
	public String idsearchget(BoardVO bvo){
		System.out.println("�г��Ӽҽ� : "+bvo.getNick()+"�̸��ϼҽ� : "+bvo.getEmail());
        
		if(memService.idsearchCheck(bvo)){
			
			return "{\"ok\":"+memService.sendMail(bvo)+"}";
		}
		return null;
	}
	
	@RequestMapping("/idsearchget2")
    public String idsearchget2(Model model, @RequestParam String nick, @RequestParam String email){
     BoardVO bvo = new BoardVO();
     bvo.setNick(nick);
     bvo.setEmail(email);
   
     String idresult = memService.IdSearchResult(bvo);
     String idresult2="";

     idresult2+=idresult.substring(0, 2);   
     idresult2+="***";
     idresult2+=idresult.substring(5, idresult.length());
   
		model.addAttribute("idlist",idresult2);
		
		return "memInfoedit/IdSearchResult";
	}
	
	@RequestMapping(value="/pwSearchget", method=RequestMethod.POST)
	@ResponseBody
	public String pwSearchget(BoardVO bvo){
		
		return "{\"ok\":"+memService.sendTempMail(bvo)+"}";
	}
	
	//========================================================================================================
	@RequestMapping("send")
	@ResponseBody
	public String sendMail(HttpServletRequest request) throws Exception {

		EmailVO email = new EmailVO();
		String mail = request.getParameter("email");
		System.out.println(mail);
		String sId = request.getSession().getId();
		request.getSession().setAttribute("email", mail);
		/*
		 * String receiver = "jhchoi729@naver.com"; //Receiver.
		 */
		String subject = "��������";
		String content = "<html>"
				+ "<head>"
				+ "</head>"
				+ "<body>"
				+ "<a href=\"http://192.168.8.21:8888/SGP/mem/userInput?auth="
				+ sId + "\">������ ��ũ</a>" + "</body>" + "</html>";

		email.setReceiver(mail);
		email.setSubject(subject);
		email.setContent(content);
		boolean result = memService.sendMail(email);

		return "{\"ok\":" + result + "}";
	}

	@RequestMapping("userInput")
	public String returnlink(HttpServletRequest request, @RequestParam String auth) throws Exception {
		if(request.getSession().getId().equals(auth)){
			request.getSession().setAttribute("check", true);
		}
		return "redirect:/mem/input";
	}
	
	@RequestMapping("idCheck")
	@ResponseBody
	public String idCheck(@RequestParam("id") String id) {
		System.out.println("���̵��ߺ��˻� �ҽ� : "+id);
		boolean result = memService.idCheck(id);

		return "{\"ok\":" + result + "}";
	}
	@RequestMapping("nickCheck")
	@ResponseBody
	public String nickCheck(@RequestParam("nick") String nick) {
		boolean result = memService.nickCheck(nick);

		return "{\"ok\":" + result + "}";
	}
	
	@RequestMapping("input")
	public String meminput(Model model) {
		return "mem/memInput";
	}
	
	@RequestMapping("regist")
	@ResponseBody
	public String regist(MemberVO member) {
		System.out.println(member.getEmail());
		System.out.println(member.getId());
		System.out.println(member.getMnum());
		boolean ok = memService.regist(member);
		
		return "{\"ok\":" + ok + "}";
	}
	
	
}
