package com.napzak.global.common.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.napzak.global.common.util.QueryCounter;

@Slf4j
@Aspect
@Component
public class QueryLoggingAspect {
	private final QueryCounter queryCounter;

	public QueryLoggingAspect(QueryCounter queryCounter) {
		this.queryCounter = queryCounter;
	}

	@Around("within(@org.springframework.web.bind.annotation.RestController *)")
	public Object logQueries(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long endTime = System.currentTimeMillis();

		log.info("Controller: {}, Method: {}, Query Count: {}, Processing Time: {}ms",
			joinPoint.getSignature().getDeclaringTypeName(),
			joinPoint.getSignature().getName(),
			queryCounter.getCount(),
			(endTime - startTime));

		return result;
	}
}