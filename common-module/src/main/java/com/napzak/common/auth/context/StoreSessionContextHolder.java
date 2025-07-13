package com.napzak.common.auth.context;

public class StoreSessionContextHolder {
	private static final ThreadLocal<StoreSession> context = new ThreadLocal<>();

	public static void set(StoreSession session) { context.set(session); }

	public static StoreSession get() { return context.get(); }

	public static void clear() { context.remove(); }
}
