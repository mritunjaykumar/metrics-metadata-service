package com.rackspacecloud.blueflood.metricsmetadataservice.controller;

import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.MetricsIngestionException;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import com.rackspacecloud.blueflood.metricsmetadataservice.service.IMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/metrics")
@Component
public class MetricsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    private IMetricsService metricsService;

    @RequestMapping(
            value = "ingest",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> ingest(@RequestBody List<Metrics> metricsCollection) throws MetricsIngestionException {
        LOGGER.debug("Started ingest of the payload.");
        metricsService.ingest(metricsCollection);
        LOGGER.debug("Completed ingest of the payload.");
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
