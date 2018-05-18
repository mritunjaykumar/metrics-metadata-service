package com.rackspacecloud.blueflood.metricsmetadataservice.controller;

import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.MetricsIngestionException;
import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.TokensIngestionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(TokensIngestionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String TokensIngestionExceptionHandler(final String message){
        LOGGER.error(message);
        return message;
    }

    @ExceptionHandler(MetricsIngestionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String MetricsIngestionExceptionHandler(final String message){
        LOGGER.error(message);
        return message;
    }
}
