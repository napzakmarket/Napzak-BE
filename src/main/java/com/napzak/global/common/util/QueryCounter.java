package com.napzak.global.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import lombok.Getter;

@Component
@RequestScope
@Getter
public class QueryCounter {
	private int count = 0;

	public void increment() {
		count++;
	}
}