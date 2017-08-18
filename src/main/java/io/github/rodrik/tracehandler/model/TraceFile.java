package io.github.rodrik.tracehandler.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
public class TraceFile {

	@Id
	private Long id;
	@Column(name="METHOD_NAME", nullable=false)
	private String methodName;
	private LocalDateTime scanDateTime;
	
	private String filename;
	
	public TraceFile(Long id, String methodName, LocalDateTime scanDateTime, String filename) {
		super();
		this.id = id;
		this.methodName = methodName;
		this.scanDateTime = scanDateTime;
		this.filename = filename;
	}
	

	public TraceFile() {
		super();
		// JPA
	}


	public Long getId() {
		return id;
	}

	public String getMethodName() {
		return methodName;
	}

	public LocalDateTime getScanDateTime() {
		return scanDateTime;
	}

	public String getFilename() {
		return filename;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}


	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}


	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	
}
