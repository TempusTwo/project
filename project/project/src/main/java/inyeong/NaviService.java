package inyeong;

import java.util.HashMap;
import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("navisvc")
public class NaviService {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate; 
	
	
	private boolean leftMore, rightMore;
	
	int pg;
	int totalPg;
	int rowsPerPage = 5;
	int numsPerPage = 3;


	public boolean isLeftMore() {
		return leftMore;
	}

	public void setLeftMore(boolean leftMore) {
		this.leftMore = leftMore;
	}

	public boolean isRightMore() {
		return rightMore;
	}

	public void setRightMore(boolean rightMore) {
		this.rightMore = rightMore;
	}

	public List<QnABoardVO> svclist(int pnum) {
		QnADAO dao = sqlSessionTemplate.getMapper(QnADAO.class);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("num", pnum);
		dao.getPagelist(map);
		List<QnABoardVO> list = (List<QnABoardVO>) map.get("key");
		
		return list;
	}
	public NaviVO getNaviVO(int pnum) {
		
		pg =pnum;
		NaviVO nav = new NaviVO();
		nav.setCurrPage(pg);
		nav.setLinks(getNavlinks(pg, rowsPerPage, numsPerPage));
		nav.setLeftMore(leftMore);
		nav.setRightMore(rightMore);
		nav.setTatalPages(totalPg);
		System.out.println(nav.getLinks().length+"link");
		return nav;
	}
	
	public int[] getNavlinks(int page, int rowsPerPage, int numsPerPage) {
		QnADAO dao = sqlSessionTemplate.getMapper(QnADAO.class);
		int totalPages = (dao.getTotalRows() - 1) / rowsPerPage + 1;
		
		totalPg = totalPages;

		int tmp = (page - 1) / numsPerPage + 1;
		
		int end = tmp * numsPerPage;
	
		int start = (tmp - 1) * numsPerPage + 1;
		
		if (start == 1)
			leftMore = false;
		
		else
			leftMore = true;
		
		if (end >= totalPages) {
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

	public NaviVO getsearchNaviVO(int pnum, String cate, String word) {
		pg =pnum;
		NaviVO nav = new NaviVO();
		nav.setCurrPage(pg);// ���� ������
		
		nav.setLinks(getsearchNavlinks(pg, rowsPerPage, numsPerPage,cate,word));// ���� �������� ����
		
		nav.setLeftMore(leftMore);// ���� �̵� �ۼ�����
		nav.setRightMore(rightMore);// ������ �̵� �ۼ�����
		nav.setTatalPages(totalPg);
		System.out.println(nav.getLinks().length+"link");
		return nav;
	}
	
	public int[] getsearchNavlinks(int page, int rowsPerPage, int numsPerPage,String cate, String word) {
		QnADAO dao = sqlSessionTemplate.getMapper(QnADAO.class);
		int totalPages =0;
		if(cate.equals("������")){
			totalPages = (dao.getsearchTotalRows(word) - 1) / rowsPerPage + 1;
		}
		else if(cate.equals("�۾���")){
			totalPages = (dao.getsearchaTotalRows(word) - 1) / rowsPerPage + 1;
		}
		// �ο���� �ִ� ����(�� �ο�� ������ ����)�� 1�� ���� ���������� ����� ������ 1�� ���ϸ� �� ������ ���� ���´�(��
		// �������� ���� ���� ���ϴ� �İ� ����������)
		totalPg = totalPages;

		int tmp = (page - 1) / numsPerPage + 1;
		// �� �������� ���ϴ� ������ ���ܱ��� ����(��� ���� ��� �������� ���� ���� �����ϴ� �İ� ����)
		int end = tmp * numsPerPage;
		// ������ ���ܱ��� ������ ���ܱ��� �ִ� ����� ���ϸ� �� ������ ���ܱ��� �ִ� ���ڰ� ���´�.
		int start = (tmp - 1) * numsPerPage + 1;
		// �ش� ������ ���ܱ� �ٷ� �� ��ܱ��� �ִ� ���ڿ� 1�� ���ϸ� ���� ������ ���ܱ��� �ּ� ���ڰ� ���´�
		if (start == 1)
			leftMore = false;
		// Ȥ�� �� �ּ� ���ڰ� 1�� ��� ����(<<) ��ũ�� ������� �ʵ��� ��½� Ȯ���ϴ� boolean ���� false�� �ش�
		else
			leftMore = true;
		// �ƴ� ���(1���� Ŭ���) ���� ��ũ ��½� Ȯ���ϴ� boolean ���� true�� �ش�
		if (end >= totalPages) {
			end = totalPages;
			rightMore = false;
			// Ȥ�� �ִ� ���ڰ� �� ������ ���ں��� ũ�ų� ���� ��� ������(>>) ��ũ�� ������� �ʵ��� ��½� Ȯ���ϴ�
			// boolean ���� false�� �ش�
		} else
			rightMore = true;
		// �ƴҰ��(�� ������ ������ �ִ������ �� ������ ���ڰ� Ŭ���) ������ ��ũ ��½� Ȯ���ϴ� boolean ���� true��
		// �ش�
		int[] links = new int[end - start + 1];
		// ������ �ѹ����� ��� �迭�� �����. �� ũ��� �ִ� ����-�ּҼ���+1�� �ϸ� �� ������ �ִ� ���ڿ� �ּ� ������ ���̰�
		// �����ϴٰ� �Ҽ� ���� ����

		// ��) 5������ �� ����ϴ� ������ ��ũ ������ ���������� �� 13 �������� ��� 1,2��° ��ũ���� �ּ� �ִ� ���� 4������
		// 3��°
		// ��ũ ���� �ִ�, �ּ����̴� 2 ���ȴ�
		for (int num = start, i = 0; num <= end; num++, i++) {
			links[i] = num;
		}
		// ������ ���� �迭�� ���� �ѹ����� ������ �ѹ����� ���� �ִ´�.
		return links;
	}

}
