package io.github.rodrik.tracehandler.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import io.github.rodrik.tracehandler.model.TraceFile;
import io.github.rodrik.tracehandler.persistence.TraceRepository;

@Component
public class FileSystemTraceHandler implements TraceHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(FileSystemTraceHandler.class);
	
	private TraceRepository repository;
	
	public FileSystemTraceHandler(TraceRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Integer importTraceFiles(String path) {		
		logger.trace("Reading {}", path);
		Collection<File> files = FileUtils.listFiles(new File(path), new WildcardFileFilter("*.csv"), null);
	
		for (File file : files) {
			logger.trace("Found a scan file: {}", file);
			String filename = FilenameUtils.getBaseName(file.getName());
			String[] metadata = filename.split("-");
			String methodName = metadata[0];
			Long id = Long.valueOf(metadata[1]);
			LocalDateTime scanDateTime = LocalDateTime.parse(metadata[2]+metadata[3], DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			TraceFile traceFile = repository.save(new TraceFile(id, methodName, scanDateTime, file.getName()));
			logger.trace("Saved scan metadata: {}", traceFile);
		}
		return files.size();
	}
	
	@Override
	public FileSystemResource downloadScanFile(Long id, String basePath) {
		TraceFile traceFile = repository.findOne(id);
		return new FileSystemResource(FilenameUtils.concat(basePath, traceFile.getFilename()));
	}

}
