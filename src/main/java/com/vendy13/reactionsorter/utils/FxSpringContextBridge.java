package com.vendy13.reactionsorter.utils;

import org.springframework.context.ApplicationContext;

public class FxSpringContextBridge {
	private static ApplicationContext context;
	
	public static void setContext(ApplicationContext newContext) {
		context = newContext;
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
}
