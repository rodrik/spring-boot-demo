package io.github.rodrik.tracehandler.persistence;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.rodrik.tracehandler.model.TraceFile;

public interface TraceRepository extends JpaRepository<TraceFile, Long> {
	
	Collection<TraceFile> findAllByScanDateTimeBetween(LocalDateTime from, LocalDateTime to);

}
