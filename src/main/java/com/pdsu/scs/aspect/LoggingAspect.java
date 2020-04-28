package com.pdsu.scs.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;

@Component
@Aspect
public class LoggingAspect {
	
	/**
	 * 写入日志
	 */
	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
	
	/**
	 * 如未登录, 则默认执行人为游客,账号为: 0
	 */
	private UserInformation user = null;
	
	@Before(value = "execution(* com.pdsu.scs.service..*(..))")
	public void before(JoinPoint joinPoint) {
		String args = SimpleUtils.toString(joinPoint.getArgs());
		user = ShiroUtils.getUserInformation() == null ? new UserInformation(0) : ShiroUtils.getUserInformation();
		String str = joinPoint.getTarget().getClass().getName() + "." 
					+ ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
		log.info("开始执行 " + str + " 方法, 请求参数为: " + args + ", 请求人学号为: " + user.getUid());
	}
	
	@AfterReturning(value = "execution(* com.pdsu.scs.service..*(..))", returning = "result")
	public void afterReturn(JoinPoint joinPoint, Object result) {
		
		String str = joinPoint.getTarget().getClass().getName() + "." 
				+ ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
		log.info("执行方法 " + str + " 成功");
	}
	
	@AfterThrowing(value = "execution(* com.pdsu.scs.service..*(..))", throwing = "ex")
	public void afterThrowing(JoinPoint joinPoint, Exception ex) {
		String str = joinPoint.getTarget().getClass().getName() + "." 
				+ ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
		log.error("执行方法 " + str + " 失败, 异常信息为: " + ex.getLocalizedMessage().substring(1, ex.getLocalizedMessage().length()));
	}
}
