package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("naver")
public class NaverController {
	@GetMapping("*")	//localhost:8080/naver/search 요청 => /WEB-INF/view/naver/search.jsp 뷰
	public String naver() {
		return null;//뷰이름. null : url과 같은 이름의 뷰를 선택
	}
	@RequestMapping("naversearch") // /naver/search.jsp 페이지에서 ajax 으로 요청됨.
	@ResponseBody //뷰없이 바로 데이터를 클라이언트로 전송
	public JSONObject naversearch(String data, int display, int start, String type) {
		String clientId = "HntOK7hfs9KJXx93tUyA";
		String clientSecret = "pIg2guBO6Q";
		StringBuffer json = new StringBuffer();
		int cnt = (start - 1) * display+1; //네이버에 요청 시작 건수
		String text=null;
		try{
			text = URLEncoder.encode(data,"UTF-8"); //유니코드값으로 변경
			System.out.println(text);
			
			String apiURL = "https://openapi.naver.com/v1/search/"+type+".json?query="
						  + text+"&display="+display+"&start="+cnt; //json 결과
		
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); //apiURL에 접속됨
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode(); //네이버에서 응답코드.
			BufferedReader br;	
			if(responseCode == 200){ //정상처리. 
				br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
			} else { //오류 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
			}
			String inputLine;
			while((inputLine = br.readLine()) != null){
				json.append(inputLine); //네이버에서 전송한 데이터 저장
			}
			br.close();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch(MalformedURLException e1){
			e1.printStackTrace();
		} 
		catch(IOException e1){
			e1.printStackTrace();
		}
		//json : 동적 문자열객체. 네이버에서 전송한 json 형태의 문자열 데이터
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			//json.toString() : String 객체. 문자열 개체
			//json
			jsonObj = (JSONObject)parser.parse(json.toString());
		}catch(ParseException e) {
			e.printStackTrace();
		}
		System.out.println(json);
		return jsonObj;
	}
}
