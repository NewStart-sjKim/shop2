package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
public class UserLoginAspect {
	/* 강사님 코드
	 * @Around("execution(* controller.User*.idCheck*(..)) && args(userid,session)")
	public void userIdCheck(ProceedingJoinPoint joinPoint, String userid, HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("회원만 가능합니다.","../user/login");
		}
		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(userid)) {
			throw new LoginException("본인만 가능합니다.","../item/list");
		}
		return joinPoint.proceed();
	}
	 */
	@Before("execution(* controller.User*.idCheck*(..)) && args(userid,session)")
	public void userIdCheck(String userid, HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("[idCheck]회원만 가능합니다.","../user/login");
		}
		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(userid)) {
			throw new LoginException("본인만 가능합니다.","../item/list");
		}
	}
	@Before("execution(* controller.User*.upDatePass*(..)) && args(..,session)")
	public void userPsCheck(HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("[idCheck]회원만 가능합니다.","login");
		}
		
	}
	
}
