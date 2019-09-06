package com.spring.common;
/*
nowPage:현재페이지
rowTotal:전체데이터갯수
blockList:한페이지당 게시물수
blockPage:한화면에 나타낼 페이지 메뉴 수
*/
public class Paging {				//표시할 페이지, 현재페이지, 전체 게시물 수, 한 페이지에 보여줄 게시물 수, 페이지메뉴 수
	public static String getPaging(String pageURL, int nowPage, int rowTotal, int blockList, 
					int blockPage, boolean isSearch, String category, String criteria, String keyword,
					String align, String order) {

		int totalPage/* 전체페이지수 */, startPage/* 시작페이지번호 */, endPage;/* 마지막페이지번호 */

		boolean isPrevPage = false;
		boolean isNextPage = false;
		StringBuffer sb; // 모든 상황을 판단하여 HTML코드를 저장할 곳
		
		// 표시될 총 페이지 수 구하기
		totalPage = (int) (rowTotal / blockList);	// 전체게시물 수 / 한페이지에 보여줄 게시물 수
		if (rowTotal % blockList != 0)	// 만약에 나누어떨어지지 않으면 
			totalPage++;				// 전체페이지 수를 하나 추가

		if (nowPage > totalPage)	// 만약 위 계산에 오류가 생겨서 지금 현재 페이지 값이 전체 페이지수보다 클 수 없음
			nowPage = totalPage;	// 이 경우 무조건 같게

		// 시작 페이지와 마지막 페이지를 구함.
		startPage = (int) (((nowPage - 1) / blockPage) * blockPage + 1);	// 결과는 무조건 현재페이지로 들어온 값
		endPage = startPage + blockPage - 1; // 

		// 마지막 페이지 수가 전체페이지수보다 크면(그러면 안됨) 마지막페이지 값을 변경
		if (endPage > totalPage)
			endPage = totalPage;

		// 마지막페이지가 전체페이지보다 작을 경우 다음 페이징(>>이거)이 적용할 수 있도록 boolean형 변수의 값을 설정
		if (endPage < totalPage)
			isNextPage = true;
		// 시작페이지의 값이 1보다 크면 이전페이징 적용할 수 있도록 값설정
		if (startPage > 1)	// eg. 5개페이지씩 표시하니까 6번째 페이지는 1보다 크고 이전페이지목록을 불러올 필요 있음
			isPrevPage = true;

		String str = "";
		if(isSearch) {	//검색
			str = ",\"" + category + "\",\"" + criteria + "\",\"" + keyword + "\"";
		}else {
			str = ",\"\",\"\",\"\"";
		}
		if(align!=null && order!=null) {
			str += ",\"" + align + "\",\"" + order + "\"";
		}
		// HTML코드를 저장할 StringBuffer생성=>코드생성
		sb = new StringBuffer();
//-----그룹페이지처리 이전 --------------------------------------------------------------------------------------------		
		sb.append("<li class='page-item'>");//board/boarList?&page=1이런식
		if(isPrevPage) {
			//무조건 첫번째 페이지로 가는 버튼
			sb.append("<a class='page-link' href='" + pageURL + "(");
			sb.append(1);
			sb.append(str);
			sb.append(")' aria-label='Previous'> <span aria-hidden='true'>&laquo;&laquo;</span></a></li>");
			
			//이전페이지메뉴
			sb.append("<a class='page-link' href='" + pageURL + "(");
			sb.append(nowPage - 1);
		}else {
			sb.append("<a class='page-link' href='" + pageURL + "(");
			if(nowPage==1) {
				sb.append(nowPage);
			}else {
				sb.append(nowPage - 1);
			}
		}
		sb.append(str);
		sb.append(")' aria-label='Previous'> <span aria-hidden='true'>&laquo;</span></a></li>");

//------페이지 목록 출력 -------------------------------------------------------------------------------------------------
		for (int i = startPage; i <= endPage; i++) {	//eg. 1~5번페이지까지
			if (i > totalPage) {	//전체 페이지수보다 숫자가 크면 break;
				break;
			}
			if (i == nowPage) { // 현재 있는 페이지. 파란색으로 블락표시..
				sb.append("<li class='page-item active' aria-current='page'>");
				sb.append("<span class='page-link'>");
				sb.append(i);
				sb.append("<span class='sr-only'>(current)</span>");
				sb.append(" </span></li>");
			} else {// 현재 페이지가 아니면 클릭시 해당 페이지로 이동할수 있게끔
				sb.append("<li class='page-item' data-page='" + i + "'>");
				sb.append("<a class='page-link' href='" + pageURL + "(");
				sb.append(i);
				sb.append(str);
				sb.append(")'>");
				sb.append(i);
				sb.append("</a></li>");
			}
		} // end for

//-----그룹페이지처리 다음 ----------------------------------------------------------------------------------------------
		sb.append("<li class='page-item'><a class='page-link' href='" + pageURL + "(");
		if (isNextPage) {	// 더 표시할 페이지가 있으면 해당 페이지 링크 걸기
			sb.append(nowPage + 1);
		} else {
			if(nowPage == totalPage) {
				sb.append(nowPage);
			}else {
				sb.append(nowPage + 1);
			}
		}
		sb.append(str);
		sb.append(")' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>");
		
		//맨 마지막 페이지
		sb.append("<li class='page-item'><a class='page-link' href='" + pageURL + "(");
		sb.append(totalPage);
		sb.append(str);
		sb.append(")' aria-label='Next'><span aria-hidden='true'>&raquo;&raquo;</span></a></li>");
//---------------------------------------------------------------------------------------------------------------------	    

		return sb.toString();
	}
}
