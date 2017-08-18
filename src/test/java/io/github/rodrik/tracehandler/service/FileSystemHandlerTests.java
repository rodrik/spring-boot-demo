package io.github.rodrik.tracehandler.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResource;

import io.github.rodrik.tracehandler.model.TraceFile;
import io.github.rodrik.tracehandler.persistence.TraceRepository;
import io.github.rodrik.tracehandler.service.FileSystemTraceHandler;

public class FileSystemHandlerTests {

	private FileSystemTraceHandler handler;
	@Mock
	private TraceRepository repository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		
		handler = new FileSystemTraceHandler(repository);
		
		Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(new TraceFile(1L, "Method name C", LocalDateTime.of(LocalDate.of(2017, 07, 03), LocalTime.NOON), "Soak with Quad stability SSV-2860-20170711-165051.csv"));
	}
	
	@Test
	public void assertThatScansArePersisted() {
		Integer numImportedScans = handler.importTraceFiles("test-scans");
		Assertions.assertThat(numImportedScans).isEqualTo(3);
		
		Mockito.verify(repository, Mockito.times(3)).save(Mockito.any(TraceFile.class));
		
		Mockito.verify(repository).save(new TraceFile(6666L, "Ammonia SIM 30s", LocalDateTime.of(2017, 06, 12, 11, 20, 47), "Ammonia SIM 30s-6666-20170612-112047.csv"));
	}
	
	@Test
	public void assertThatDownloadScanFileWorks() {
		FileSystemResource scanFile = handler.downloadScanFile(1L, "test-scans");
		Assertions.assertThat(scanFile.getFile()).isEqualTo(new File("test-scans/Soak with Quad stability SSV-2860-20170711-165051.csv"));
	}
}
