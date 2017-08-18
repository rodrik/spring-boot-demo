package io.github.rodrik.tracehandler.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.rodrik.tracehandler.service.MonitorTask;
import io.github.rodrik.tracehandler.service.TraceHandler;


public class MonitorTaskTest {

	private MonitorTask monitorTask;
	
	@Mock
	private TraceHandler traceHandler;
	private String monitorPath = "./test-scans";
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		monitorTask = new MonitorTask(traceHandler, monitorPath);
	}
	
	@Test
	public void assertThatImportScansIsExecuted() {
		monitorTask.run();
	}
}
