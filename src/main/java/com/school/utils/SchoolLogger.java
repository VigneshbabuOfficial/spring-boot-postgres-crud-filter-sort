package com.school.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

public class SchoolLogger {

	private transient Logger logger;
	
	@Autowired
	private RequestId			requestId;

	public static SchoolLogger getLogger(String loggerName) {

		SchoolLogger sLogger = new SchoolLogger();

		sLogger.logger = Logger.getLogger(loggerName);

		return sLogger;
	}

	public void info(String message) {
		logger.log(Level.INFO, "message = " + message + ", requestId = " + requestId.getId());
	}

	public void error(String message) {
		logger.log(Level.SEVERE,  "message = " + message + ", requestId = " + requestId.getId());
	}
	
	public void error(String message,Exception ex) {
		logger.log(Level.SEVERE,  "message = " + message +", exception = "+ex+ ", requestId = " + requestId.getId());
	}

	public void debug(String message) {
		logger.log(Level.FINE,  "message = " + message + ", requestId = " + requestId.getId());
	}

}
