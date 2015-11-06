package org.kdea.sgp.member;

import java.util.List;
import java.util.Random;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("MemService")
public class MemService {
	
	@Autowired
	protected JavaMailSender  mailSender;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public boolean editPWcheck(String pw) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		System.out.println("����ҽ�:"+pw);
		System.out.println("���Ȯ�ΰ��0.5:"+mdao.ETpwCheck(pw));
		boolean ok = mdao.ETpwCheck(pw)>0?true:false;
		System.out.println("���Ȯ�ΰ��:"+ok);
		return ok;
	}

	public boolean editIDcheck(String id) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		boolean ok = mdao.ETidCheck(id)>0?true:false;
		System.out.println("���̵�Ȯ�ΰ��:"+ok);
		return ok;
	}
	
	public boolean updatePW(BoardVO bvo) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		boolean ok = mdao.ETpwUpdate(bvo)>0?true:false;
		System.out.println("������Ʈ Ȯ�ΰ�� : "+ok);
		return ok;
	}

	public boolean editNickcheck(String nicksc) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		boolean ok = mdao.ETnickCheck(nicksc)>0?false:true;
		System.out.println("�ߺ�Ȯ�� ���:"+ok);
		return ok;
	}

	public boolean newNickRegi(BoardVO bvo) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		boolean ok = mdao.ETnewNickRegi(bvo)>0?true:false;
		return ok;
	}

	public boolean emailEdit(BoardVO bvo) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		boolean ok = mdao.ETemailEdit(bvo)>0?true:false;
		return ok;
	}

	public boolean idsearchCheck(BoardVO bvo) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		boolean ok = mdao.ETidsearchCheck(bvo)>0?true:false;
		return ok;
	}

	public boolean sendMail(BoardVO bvo) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		try{
		        MimeMessage msg = mailSender.createMimeMessage();
		     	msg.setFrom("syasyapeu@naver.com"); 
		        msg.setSubject("�ȳ��ϼ��� XX����Ʈ�� ������ ID ã�� ����Դϴ�.");
		        
		        msg.setContent("ã���ô� ���̵�� "+mdao.SCidsc(bvo)+"�Դϴ�.", "text/html; charset=utf-8");
		        msg.setRecipient(RecipientType.TO , new InternetAddress(bvo.getEmail()));
		         
		        mailSender.send(msg);
		        return true;
	        }catch(Exception ex) {
	        	ex.printStackTrace();
	        }
	        return false;
	}

	public String IdSearchResult(BoardVO bvo) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		
		return mdao.SCidsc(bvo);
	}

	public boolean sendTempMail(BoardVO bvo) {
		MemDAO mdao = sqlSessionTemplate.getMapper(MemDAO.class);
		bvo.setNum(mdao.SCpwsc(bvo));
	      Random randomMaking = new Random();
	      int i = 1;
	       String randomPwdCheakingResult = "";
	      while (i < 10) {
	       int randomMakingNumber = randomMaking.nextInt(25) + 65;
	       char randomMakingNumberChange = (char) randomMakingNumber;
	       randomPwdCheakingResult += randomMakingNumberChange;
	       i++;
	      }
	      bvo.setPw(randomPwdCheakingResult);
	      bvo.setTemp(1);
	      mdao.setTempPw(bvo);
		    try{
		        MimeMessage msg = mailSender.createMimeMessage();
		     	msg.setFrom("syasyapeu@naver.com"); 
		        msg.setSubject("�ȳ��ϼ��� XX����Ʈ�� ������ �ӽú�й�ȣ�Դϴ�..");
		        
		        msg.setContent("�ӽú�й�ȣ�� "+randomPwdCheakingResult+"�Դϴ�.", "text/html; charset=utf-8");
		        msg.setRecipient(RecipientType.TO , new InternetAddress(bvo.getEmail()));
		         
		        mailSender.send(msg);
		        return true;
	        }catch(Exception ex) {
	        	ex.printStackTrace();
	        }
	        return false;
		
	}
	
	//=====================================================================================================
    public boolean sendMail(EmailVO email) throws Exception {
        try{
	        MimeMessage msg = mailSender.createMimeMessage();
	     	msg.setFrom("someone@paran.com"); 
	        msg.setSubject(email.getSubject());
	        msg.setContent(email.getContent(), "text/html; charset=utf-8");
	        
	        msg.setRecipient(RecipientType.TO , new InternetAddress(email.getReceiver()));
	         
	        mailSender.send(msg);
	        return true;
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        return false;
    }

	public boolean nickCheck(String nick) {
		MemDAO dao = sqlSessionTemplate.getMapper(MemDAO.class);
		int result= dao.nickCheck(nick);
		if(result==0){
			return true;
		}
		return false;
	}

	public boolean idCheck(String id) {
		MemDAO dao = sqlSessionTemplate.getMapper(MemDAO.class);
		int result= dao.idCheck(id);
		System.out.println("�ߺ��˻� ��� �ҽ�:"+result);
		if(result==0){
			return true;
		}
		return false;
	}

	public boolean regist(MemberVO member) {
		MemDAO dao = sqlSessionTemplate.getMapper(MemDAO.class);
		int result1=dao.memregist(member);
		int mnum=dao.getMnum(member.getId());
		member.setMnum(mnum);
		
		int result2=dao.pwregist(member);
		
		if(result2>0&&result1>0){
			return true;
		}
		
		return false;
	}
	


}
