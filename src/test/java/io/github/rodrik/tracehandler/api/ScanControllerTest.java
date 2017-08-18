package io.github.rodrik.tracehandler.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import io.github.rodrik.tracehandler.api.TraceController;
import io.github.rodrik.tracehandler.model.TraceFile;
import io.github.rodrik.tracehandler.persistence.TraceRepository;
import io.github.rodrik.tracehandler.service.TraceHandler;

@RunWith(SpringRunner.class)
@WebMvcTest(TraceController.class)
public class ScanControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TraceRepository scanRepository;
	@MockBean
	private TraceHandler scanHandler;
	
	FileSystemResource scanFile;
	
	@Before
	public void init() {
		Mockito.when(scanRepository.findAll()).thenReturn(Arrays.asList(new TraceFile[] {new TraceFile(1L, "Method name", LocalDateTime.of(LocalDate.now(), LocalTime.NOON), "filename.csv")}));
		Mockito.when(scanRepository.findAllByScanDateTimeBetween(Mockito.any(), Mockito.any())).thenReturn(
				Arrays.asList(new TraceFile[] {
						new TraceFile(1L, "Method name A", LocalDateTime.of(LocalDate.of(2017, 07, 01), LocalTime.NOON), "filename 1.csv"),
						new TraceFile(1L, "Method name B", LocalDateTime.of(LocalDate.of(2017, 07, 02), LocalTime.NOON), "filename 2.csv"),
						new TraceFile(1L, "Method name C", LocalDateTime.of(LocalDate.of(2017, 07, 03), LocalTime.NOON), "filename 3.csv")
						})
				);
		
		scanFile = new FileSystemResource("test-scans/Ammonia SIM 30s-6666-20170612-112047.csv");
		Mockito.when(scanHandler.downloadScanFile(Mockito.anyLong(), Mockito.anyString())).thenReturn(scanFile);
	}
	
	@Test
	public void assertThatScansAreReturned() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders.get("/api/scan").accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.[0].scanDateTime").value(LocalDateTime.of(LocalDate.now(), LocalTime.NOON).format(DateTimeFormatter.ISO_DATE_TIME)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[0].methodName").value("Method name")
				);
	}

	@Test
	public void assertThatScansAreFilteredByDate() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders.get("/api/scan/filter")
				.param("from", "2017-07-01")
				.param("to", "2017-07-02")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
			
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].scanDateTime").value(LocalDateTime.of(LocalDate.of(2017, 07, 01), LocalTime.NOON).format(DateTimeFormatter.ISO_DATE_TIME)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].methodName").value("Method name A"))
			
			.andExpect(MockMvcResultMatchers.jsonPath("$.[1].scanDateTime").value(LocalDateTime.of(LocalDate.of(2017, 07, 02), LocalTime.NOON).format(DateTimeFormatter.ISO_DATE_TIME)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[1].methodName").value("Method name B"))
			
			.andExpect(MockMvcResultMatchers.jsonPath("$.[2].scanDateTime").value(LocalDateTime.of(LocalDate.of(2017, 07, 03), LocalTime.NOON).format(DateTimeFormatter.ISO_DATE_TIME)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[2].methodName").value("Method name C")
					);
	}
	
	@Test
	public void assertThatFileDownloadWorks() throws Exception {
		this.mvc.perform(MockMvcRequestBuilders.get("/api/scan/1/download"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+scanFile.getFilename()+"\""))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
			.andExpect(MockMvcResultMatchers.content().bytes(IOUtils.toByteArray(scanFile.getURL())))
			;
	}
	
}
