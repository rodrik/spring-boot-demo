package io.github.rodrik.tracehandler.api;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GaugeController {
	
	private final static Logger logger = LoggerFactory.getLogger(GaugeController.class);
	
	@Autowired
    private SimpMessagingTemplate template;

	@Scheduled(fixedRate=500)
	public void readGauge() throws Exception {
		Random generator = new Random(); 
		int randomNum = generator.nextInt(Integer.MAX_VALUE) + 1;
		
		logger.trace("Gauge reading: {}", randomNum);
		
		template.convertAndSend("/topic/messages", String.valueOf(randomNum));
	}
	
}
