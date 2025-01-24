package com.napzak.global.common.annotation;



import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(0) // 트랜잭션보다 먼저 실행되도록 우선순위 설정
public class TransactionMonitoringAspect {

	@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void transactionalMethods() {}

	@Around("transactionalMethods()")
	public Object monitorTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("트랜잭션 시작: {}", joinPoint.getSignature());

		try {
			Object result = joinPoint.proceed();
			log.info("트랜잭션 성공: {}", joinPoint.getSignature());
			return result;
		} catch (Exception ex) {
			log.error("트랜잭션 롤백 발생: {}, 예외: {}", joinPoint.getSignature(), ex.getMessage());
			throw ex;
		} finally {
			log.info("트랜잭션 종료: {}", joinPoint.getSignature());
		}
	}
}