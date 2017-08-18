package io.github.rodrik.tracehandler.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorTask implements Runnable {
	
	private TraceHandler handler;
	private String monitorPath;
	
	public MonitorTask(TraceHandler handler, @Value("${app.monitor.path}") String monitorPath) {
		super();
		this.handler = handler;
		this.monitorPath = monitorPath;
	}

	@Scheduled(fixedRate=10000)
	@Override
	public void run() {
		handler.importTraceFiles(monitorPath);
	}

}
