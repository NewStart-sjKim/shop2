package util;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import logic.Exchange;
import logic.ShopService;

public class ConutScheduler {
	private int cnt;
	@Autowired
	ShopService service;
	/*
	 * cron
	 * 		1. 특정시간, 주기적으로 프로그램을 수행하는 프로세스. 유닉스(OS중 하나)기반의 프로세스
	 * 		2. 리눅스에서 crontab 명령 설정 가능함
	 * 		3. 스프링에서는 cron 기능을 Scheduler라 한다.
	 * 
	 * 크론식 0/5 * * * * ? => 5초마다 실행 
	 * 			 cron을 설정할 수 있는 형식
	 * 
	 * 	형식 : 초 분 시 일 월 요일 [년도]
	 * 	 초  : 0 ~ 59
	 * 	 분  : 0 ~ 59
	 *   시  : 0 ~ 23
	 *   일  : 1 ~ 31
	 *   월  : 1 ~ 12(JAN,FEB,MAR,APR,....DEC)
	 *   요일 : 1 ~ 7(MON,TUE,WED,THR,FRI,SAT,SUN)
	 *   
	 * 표현 방식
	 * 		* 	: 매번
	 * 		A/B	: 주기 A~B마다 한번씩 실행. 0/5 => 0 ~ 5초마다 실행.
	 * 		?	: 설정 없음. (일, 요일에서 사용됨.)
	 * 
	 * 크론식 예시
	 * 		0/10 * * * * ?  	: 10초마다 한번씩
	 * 		0 0/1 * * * ?  		: 1분마다 한번씩
	 * 		0 20,50 * * * ? 	: 매시간 20,50 마다 실행
	 * 		0 0 0/3 * * ?   	: 3시간마다 한번씩
	 * 		0 0 12  ? * 1   	: 월요일 12시에 실행.
	 * 		0 0 12  ? * MON 	: 월요일 12시에 실행.
	 * 		0 0 10  ? * SAT,SUN : 주말 10시에 실행.
	 * 		0 0 10  ? * 6,7		: 주말 10시에 실행.
	 * 크론식 작성 사이트 : http://cronmaker.com
	 *   
	 */
	//@Scheduled(cron="0/5 * * * * ?")//  0 ~ 5초마다 execute1()메서드 실행.
	public void execute1() {
		System.out.println("cnt:"+ cnt++);
	}
	//@Scheduled(cron="0 9 15 14 6 ?")//  6월 14일 15시 9분에 실행
	public void execute2() {
		System.out.println("3시 8분입니다.");
	}
	/*
	 * 1. 평일 아침 10시에 환율 정보를 조회해서 db에 등록
	 * 2. exchange 테이블 생성하기
	 * 	 create table exchange(
			ENUM INT PRIMARY KEY,
	 		code varchar(10), #통화코드
	 		name varchar(50), #통화명
	 		primeamt FLOAT, #매매기준일
	 		sellamt  FLOAT, #매도율
	 		buyamt   FLOAT, #매입율
	 		edate 	VARCHAR(10) #환율 기준일
	    )
	    # auto_increment 오라클에서는 없는 기능
		# 오라클 : 시퀀스 사용
		# auto_increment 기능 추가 : 자동으로 값  생성
		# 기존테이블에 auto_increment 기능 추가
		ALTER TABLE exchange MODIFY column eno int AUTO_INCREMENT;
	 */
	@Scheduled(cron="0/5 * * * * ?")
	public void excute3() {
		System.out.println("환율 등록 시작");
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>(); 
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); 
			exdate = doc.select("p.table-unit").html();
			exdate = exdate.substring(exdate.indexOf(":")+2);
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td");
				for(Element td : tds) {
					tdlist.add(td.html()); //
				}
				if(tdlist.size() > 0) {
					trlist.add(tdlist);
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		for(List<String> tds : trlist) {
			Exchange ex = new Exchange
					(0,tds.get(0),tds.get(1),
					Float.parseFloat(tds.get(4).replace(",","")),
					Float.parseFloat(tds.get(2).replace(",","")),
					Float.parseFloat(tds.get(3).replace(",","")),exdate.trim());
			service.exchangeInsert(ex);
		}
	}
}
