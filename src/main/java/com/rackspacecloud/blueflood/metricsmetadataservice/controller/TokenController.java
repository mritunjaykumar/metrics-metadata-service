package com.rackspacecloud.blueflood.metricsmetadataservice.controller;


import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.TokensIngestionException;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import com.rackspacecloud.blueflood.metricsmetadataservice.service.ITokenService;
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
@RequestMapping("/token")
@Component
public class TokenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private ITokenService tokenService;

    @RequestMapping(
            value = "ingest",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> ingest(@RequestBody List<Metrics> metricsCollection) throws TokensIngestionException {
        LOGGER.debug("Started ingest of the payload.");
        tokenService.ingest(metricsCollection);
        LOGGER.debug("Completed ingest of the payload.");

        return new ResponseEntity<String>("", HttpStatus.OK);
    }
}
