package com.napzak.global.common.config;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.napzak.global.common.util.QueryCounter;

@Component
public class QueryInspector implements StatementInspector {
	private final QueryCounter queryCounter;

	public QueryInspector(QueryCounter queryCounter) {
		this.queryCounter = queryCounter;
	}

	@Override
	public String inspect(String sql) {
		if (RequestContextHolder.getRequestAttributes() != null) {
			queryCounter.increment();
		}
		return sql;
	}
}