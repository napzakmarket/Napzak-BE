package com.napzak.global.auth.context;

import com.napzak.domain.store.core.vo.StoreSession;

public class StoreSessionContextHolder {
	private static final ThreadLocal<StoreSession> context = new ThreadLocal<>();

	public static void set(StoreSession session) { context.set(session); }

	public static StoreSession get() { return context.get(); }

	public static void clear() { context.remove(); }
}
