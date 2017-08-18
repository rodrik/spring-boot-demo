package io.github.rodrik.tracehandler.service;

import org.springframework.core.io.FileSystemResource;

public interface TraceHandler {
	
	public Integer importTraceFiles(String path);

	public FileSystemResource downloadScanFile(Long id, String basePath);
	
}
