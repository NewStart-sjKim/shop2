package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import logic.ShopService;
/*
 * @Controller  :  @Component + Controller 기능
 * 		호출되는 메서드 리턴타입 : ModelAndView : 뷰이름 + 데이터
 * 		호출되는 메서드 리턴타입 : String		  : 뷰이름 
 * @RestController :@Conponent + Controller 기능 + 클라이언트에 데이터를 직접 전달
 * 		호출되는 메서드 리턴타입 : String			 : 클라이언트 전달되는 문자열 값
 *		호출되는 메서드 리턴타입 : Object			 : 클라이언트 전달되는 값(JSON 평태)
 *
 *		Spring 4.0 이후에 추작
 * 		Spring 4.0 이전 보전에서@ResponseBody 기능으로 설정하였음
 * 		@ResponsBoady 어노테이션은 메서드에 설정함
 */
@RestController
@RequestMapping("ajax")
public class AjaxController {
	@Autowired
	ShopService service;
	
	@RequestMapping("select")
	   public List<String> select(String si, String gu, HttpServletRequest request) {
	      BufferedReader fr = null;
	      String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
	      try {
	         fr = new BufferedReader(new FileReader(path));
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	      //Set : 중복불가.
	      //LinkedHashSet : 순서유지. 중복불가. 리스트x(첨자사용 불가)
	      Set<String> set = new LinkedHashSet<>();
	      String data = null;
	      if(si == null && gu == null) { //시도 선택
	         try {
	        	 //fr.readLine() : 한줄씩 read
	            while((data=fr.readLine()) != null) {
	               // \\s+ : \\s(공백) +(1개이상)
	               String[] arr = data.split("\\s+");
	               if(arr.length >= 3) set.add(arr[0].trim()); //중복제거됨.
	            }
	         } catch(IOException e) {
	            e.printStackTrace();
	         }
	      } else if (gu == null) { //si파라미터 존재 => 시도 선택: 구군값 전ㅅ오
	         si = si.trim();
	         try {
	            while((data=fr.readLine()) != null) {
	               String[] arr = data.split("\\s+");
	               if(arr.length >=3 && arr[0].equals(si) && !arr[1].contains(arr[0])) {
	            	   set.add(arr[1].trim()); //구군 정보 설정
	               }
	            }
	         } catch(IOException e) {
	            e.printStackTrace();
	         }
	      } else { //si 파라미터, gu 파라미터 존재 => 구군선택 : 동값 전송
	         si = si.trim();
	         gu = gu.trim();
	         try {
	            while((data=fr.readLine()) != null) {
	               String[] arr = data.split("\\s+");
	               if(arr.length >=3 && arr[0].equals(si) &&
	                  arr[1].equals(gu) && !arr[0].contains(arr[1]) && !arr[2].contains(arr[1])) {
	                  if(arr.length > 3) {
	                     if(arr[3].contains(arr[1])) continue;
	                     arr[2] += " " + arr[3];
	                  }                        
	                  set.add(arr[2].trim()); //동리 정보 설정
	               }
	            }
	         } catch(IOException e) {
	            e.printStackTrace();
	         }
	      }
	      List<String> list = new ArrayList<>(set); //Set 객체 => List 객체로 변경
	      return list; //리스트 객체가 브라우저에 전달. 뷰x
	                //pom.xml의 fasterxml.jackson...의 설정에 의해 브라우저는 배열로 인식함
	   }
	
	//@RequestMapping("select2") //클라이언트로 문자열 전송. 인코딩 설정이 필요.
	/*
	 * produces : 클라이언트에 전달되는 데이터 특징을 설정
	 * 		text/plain : 데이터 특징. 순수문자
	 * 		text/html  : HTML 형식의 문자
	 * 		text/xml   : HTML 형식의 문자
	 *	carset=utf-8   : 한글은 utf-8로 인식
	 */
	/*
	@RequestMapping(value="select2", produces="text/plain; charset=utf-8")
	public String select2(String si, String gu, HttpServletRequest request){
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path)); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) set.add(arr[0].trim()); //중복제거됨.
					}
				}catch(IOException e) {
					e.printStackTrace();
				}
		}
		List<String> list = new ArrayList<>(set); 
		return list.toString();
	}
	*/
	
	@RequestMapping(value="exchange", produces="text/html; charset=utf-8")
	public String exchange() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>(); //미국,중국,일본,유로 통화들만 저장 목록
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); //tr 태그들
			//p.table-unit : class속성의 값이 table-unit 인 p 태그
			exdate = doc.select("p.table-unit").html(); //조회기준일 : 2023.06.02
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>(); //tr 태그의 td 태그 목록
				Elements tds = tr.select("td"); //tr 태그의 하위 td 태그들
				for(Element td : tds) {
					tdlist.add(td.html()); //
				}
				if(tdlist.size() > 0) {
					if(tdlist.get(0).equals("USD") //미달러 통화코드
					  || tdlist.get(0).equals("CNH") //중국 통화코드
					  || tdlist.get(0).equals("JPY(100)") // 일본 통화코드
					  || tdlist.get(0).equals("EUR")) {	//유로 통화코드
						trlist.add(tdlist);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<h5 class='w3-center'>수출입은행<br>" + exdate + "</h5>");
		sb.append("<table class='w3-table-all' style='margin:auto;'>");
		sb.append("<tr><th>통화</th><th>기준율</th><th>받으실때</th><th>보내실때</th></tr>");
		for(List<String> tds : trlist) {
			sb.append("<tr><td>" + tds.get(0)+"<br>" + tds.get(1) + "</td><td>" + tds.get(4) + "</td>");
			sb.append("<td>" + tds.get(2) + "</td><td>" + tds.get(3) + "</td><tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	/*
	@RequestMapping("exchange2")
	public List<List<String>> exchange2() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>(); //미국,중국,일본,유로 통화들만 저장 목록
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); //tr 태그들
			//p.table-unit : class속성의 값이 table-unit 인 p 태그
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>(); //tr 태그의 td 태그 목록
				Elements tds = tr.select("td"); //tr 태그의 하위 td 태그들
				for(Element td : tds) {
					tdlist.add(td.html()); //
				}
				if(tdlist.size() > 0) {
					if(tdlist.get(0).equals("USD") //미달러 통화코드
					  || tdlist.get(0).equals("CNH") //중국 통화코드
					  || tdlist.get(0).equals("JPY(100)") // 일본 통화코드
					  || tdlist.get(0).equals("EUR")) {	//유로 통화코드
						trlist.add(tdlist);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return trlist;
	}
	*/
	@RequestMapping("exchange2")
	public Map<String,Object> exchange2() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>(); //미국,중국,일본,유로 통화들만 저장 목록
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); //tr 태그들
			//p.table-unit : class속성의 값이 table-unit 인 p 태그
			exdate = doc.select("p.table-unit").html();
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>(); //tr 태그의 td 태그 목록
				Elements tds = tr.select("td"); //tr 태그의 하위 td 태그들
				for(Element td : tds) {
					tdlist.add(td.html()); //
				}
				if(tdlist.size() > 0) {
					if(tdlist.get(0).equals("USD") //미달러 통화코드
					  || tdlist.get(0).equals("CNH") //중국 통화코드
					  || tdlist.get(0).equals("JPY(100)") // 일본 통화코드
					  || tdlist.get(0).equals("EUR")) {	//유로 통화코드
						trlist.add(tdlist);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<>();
		map.put("exdate",exdate);//일자
		map.put("trlist",trlist);//환율목록
		return map;
	}
	@RequestMapping("graph1")
	public List<Map.Entry<String,Integer>> graph1(String id){
		Map<String,Integer> map = service.graph1(id); //{"홍길동"=10,"김삿갓"=7,....}
		List<Map.Entry<String,Integer>> list = new ArrayList<>();
		for(Map.Entry<String,Integer> m : map.entrySet()) {
			list.add(m);
		}
		Collections.sort(list,(m1,m2)->m2.getValue() - m1.getValue()); //등록 건수의 내림차순 정렬
		return list;
	}
}
