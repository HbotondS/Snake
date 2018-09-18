package com.hegyi.botond;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MyLogger {
	private static final Logger LOGGER = Logger.getLogger(MyLogger.class.getName());

	public static void INFO(String msg) {
		LOGGER.log(Level.INFO, msg);
	}

	public static void WARN(String msg) {
		LOGGER.log(Level.WARNING, msg);
	}
}
