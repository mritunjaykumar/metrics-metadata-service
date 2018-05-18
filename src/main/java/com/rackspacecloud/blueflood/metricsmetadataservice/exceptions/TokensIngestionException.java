package com.rackspacecloud.blueflood.metricsmetadataservice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokensIngestionException extends Exception {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokensIngestionException.class);

    public TokensIngestionException(String message, Throwable cause){
        super(message, cause);
        LOGGER.error("Message: {}", message);
        LOGGER.error("Cause: {}", cause.getMessage());
    }
}
