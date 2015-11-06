package org.kdea.sgp.board;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class PagingUtil {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	private boolean DEBUG = true;
	private boolean leftMore;
	private boolean rightMore;
	private int pg = 1;  //���� ������
	private int totalPages;
	private static int rowsPerPage = 4;  // �� ȭ��� ����� ���
	private static int numsPerPage = 2;
	private int totalRows;
	
	public NaviVO navi(int page, int totalRows) {
		this.pg = page;
		this.totalRows = totalRows;
		NaviVO nvo = getNavVO();  // �̿��ڰ� ����Ʈ�� ���⸸�ϸ� �׺���̼�VO�� ���󰡰� �ϰ� jsp�� ���̰� �Ѵ�.
		return nvo;
	}
	
	private NaviVO getNavVO() {  // �̿��ڰ� ����Ʈ�� ���� �;��Ҷ� �� �޼ҵ尡 ȣ�� �ǰ� �ؼ� jsp�� �Ѿ�� �Ѵ�. 
		NaviVO nav = new NaviVO();
		nav.setCurrPage(pg);
		nav.setLinks(getNavLinks(pg, rowsPerPage, numsPerPage));
		nav.setTotalPages(totalPages);
		nav.setLeftMore(leftMore);
		nav.setRightMore(rightMore);
		return nav;
	}
	
	private int[] getNavLinks(int page, int rowsPerPage, int numsPerPage) {
		System.out.printf("page=%d, RPP=%d, NPP=%d \n",page, rowsPerPage, numsPerPage);
        
        totalPages = (totalRows-1)/rowsPerPage+1;
        //System.out.printf("%d�྿ �������� ��:%d \n", rowsPerPage, totalPages);
         
        int tmp = (page-1)/numsPerPage+1; // ���° ��ũ�׷쿡 ���ϴ°�?
        //System.out.printf("%d��° ��ũ �׷�",tmp);
        int end = tmp*numsPerPage;
        int start = (tmp-1)*numsPerPage+1;
         
        if(start==1) leftMore = false; // << ���� �̵� ��¿���
        else leftMore = true;
        if(end>=totalPages) {         // >> ������ �̵� ��¿��� // �� ��ȣ�� �������� ���� �ʰ��ؼ��� �ȵǱ⶧���� 
            end = totalPages;  // end�� totalPages�� �Ѿ�� �ȵǱ⶧���� �̷��� ����
            rightMore = false;
        }else rightMore = true;
        //System.out.printf("START:%d~END:%d \n",start, end);
         
        int[] links = new int[end-start+1];  // ��ũ�� ���ڸ� ��´�
        for(int num=start,i=0;num<=end;num++,i++) {
            links[i] = num;
        }/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return links;
    }
}
