package org.kdea.sgp.notice;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("pagingUtil")
public class PagingUtil {
	
	private boolean leftMore, rightMore;
	int pg;
	int rowsPerPage = 5; //�� ȭ��� ����� ���
	int numsPerPage = 5; //��ȭ��� ��ũ�� �� 
	int totalPages;
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	

	public NaviVO getNav(int page) {
		
		pg= page;
		getTotalrow();
		NaviVO nav = getNaviVO();
		
		return nav;
	}
	
	public NaviVO TgetNav(SearchVO svo){
		NoticeBoardDAO nbd = sqlSessionTemplate.getMapper(NoticeBoardDAO.class);
		pg= svo.getPage();
		System.out.println("����:"+svo.getTitle());
		NaviVO nav = getNaviVO();
		totalPages = (nbd.NBgetTitleTotalRows(svo.getTitle()) - 1) / rowsPerPage + 1;
		return nav;
	}
	
	public NaviVO AgetNav(SearchVO svo){
		NoticeBoardDAO nbd = sqlSessionTemplate.getMapper(NoticeBoardDAO.class);
		pg= svo.getPage();
		System.out.println("����:"+svo.getTitle());
		NaviVO nav = getNaviVO();
		totalPages = (nbd.NBgetAuthorTotalRows(svo.getAuthor()) - 1) / rowsPerPage + 1;
		return nav;
	}

	public NaviVO getnebiNav(SearchVO svo, String cate) {
		
		if(cate.equals("����")){
			NoticeBoardDAO nbd = sqlSessionTemplate.getMapper(NoticeBoardDAO.class);
			pg= svo.getPage();
			System.out.println("����:"+svo.getTitle());
			NaviVO nav = getNaviVO();
			totalPages = (nbd.NBgetTitleTotalRows(svo.getTitle()) - 1) / rowsPerPage + 1;
			return nav;
		}else{
			NoticeBoardDAO nbd = sqlSessionTemplate.getMapper(NoticeBoardDAO.class);
			pg= svo.getPage();
			System.out.println("����:"+svo.getTitle());
			NaviVO nav = getNaviVO();
			totalPages = (nbd.NBgetAuthorTotalRows(svo.getAuthor()) - 1) / rowsPerPage + 1;
			return nav;
			
		}
	}
	
	
	private NaviVO getNaviVO() {
		NaviVO nav = new NaviVO();
		nav.setCurrPage(pg);
		nav.setLinks(getNavlinks(pg, rowsPerPage, numsPerPage));
		nav.setLeftMore(leftMore);
		nav.setRightMore(rightMore);
		nav.setTotalpage(totalPages);
		return nav;
	}
	
	
	private void getTotalrow(){
		NoticeBoardDAO nbd = sqlSessionTemplate.getMapper(NoticeBoardDAO.class);
			
		totalPages = (nbd.NBgetTotalRows() - 1) / rowsPerPage + 1;
		
		
	}

	
	private int[] getNavlinks(int page, int rowsPerPage, int numsPerPage) {

		System.out.println("�ο�����������:"+rowsPerPage);		
        System.out.println("��Ż������:"+totalPages);

		int tmp = (page - 1) / numsPerPage + 1; // ���° ��ũ�׷쿡 ���ϴ°�?
		int end = tmp * numsPerPage;
		int start = (tmp - 1) * numsPerPage + 1;
		if (start == 1)
			leftMore = false; // << ���� �̵� ��¿���
		else
			leftMore = true;
		if (end >= totalPages) {  // >> ������ �̵� ��¿���
			end = totalPages;
			rightMore = false;
		} else
			rightMore = true;
		int[] links = new int[end - start + 1];
		for (int num = start, i = 0; num <= end; num++, i++) {
			links[i] = num;
		}
		return links; 
	}

}
