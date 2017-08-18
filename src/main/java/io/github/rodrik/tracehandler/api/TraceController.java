package io.github.rodrik.tracehandler.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.rodrik.tracehandler.model.TraceFile;
import io.github.rodrik.tracehandler.persistence.TraceRepository;
import io.github.rodrik.tracehandler.service.TraceHandler;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/scan")
public class TraceController {
	
	private TraceRepository repository;
	@Autowired
	private TraceHandler handler;
	@Value("${app.monitor.path}")
	private String basePath;
	
	public TraceController(TraceRepository repository) {
		super();
		this.repository = repository;
	}

	@ApiOperation(value="This endpoint list all the trace files on the instrument")
	@GetMapping
	public List<TraceFile> findAll() {
		return repository.findAll();
	}
	
	@GetMapping(value="/{id}")
	public TraceFile findOne(@PathVariable("id") Long id) {
		return repository.findOne(id);
	}
	
	@ApiOperation(value="Find all scans in a given date range.", notes="Add some more documentation")
	@GetMapping(value="/filter", params = {"from", "to"})
	public Collection<TraceFile> searchByDate(
			@RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from, 
			@RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate to) {
		
		return repository.findAllByScanDateTimeBetween(LocalDateTime.of(from, LocalTime.MIN), LocalDateTime.of(to, LocalTime.MAX));
		
	}
	
	@GetMapping(value="/{id}/download", produces=MediaType.TEXT_PLAIN_VALUE)
	public Resource downloadScanFile(@PathVariable("id") Long id, HttpServletResponse response) {
		Resource resource = handler.downloadScanFile(id, basePath);
		
        response.addHeader("Content-Disposition", "attachment; filename=\""+resource.getFilename()+"\"");
        
		return resource;
	}
}
