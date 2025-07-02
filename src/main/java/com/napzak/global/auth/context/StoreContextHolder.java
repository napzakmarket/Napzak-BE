package com.napzak.global.auth.context;

import com.napzak.domain.store.core.vo.Store;

public class StoreContextHolder {
	private static final ThreadLocal<Store> context = new ThreadLocal<>();

	public static void set(Store store) {
		context.set(store);
	}

	public static Store get() {
		return context.get();
	}

	public static void clear() {
		context.remove();
	}
}
