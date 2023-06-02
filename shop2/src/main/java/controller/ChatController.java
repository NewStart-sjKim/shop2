package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("chat")
public class ChatController {
	@RequestMapping("*") //설정되지 않은 모든 요청시 호출되는 메서드
	public String chat() {
		return null;
	}
}
