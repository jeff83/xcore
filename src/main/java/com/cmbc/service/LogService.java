package com.cmbc.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;

@Service
public class LogService {

	private final static Logger log = LogManager.getLogger("com.cmbc");

	@ExtDirectMethod
	public void debug(String msg) {
		log.debug(msg);
	}

	@ExtDirectMethod
	public void info(String msg) {
		log.info(msg);
	}

	@ExtDirectMethod
	public void warn(String msg) {
		log.warn(msg);
	}

	@ExtDirectMethod
	public void error(String msg) {
		log.error(msg);
	}
}
