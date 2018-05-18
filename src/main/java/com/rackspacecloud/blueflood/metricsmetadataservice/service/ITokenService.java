package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.TokensIngestionException;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;

import java.util.List;

public interface ITokenService {
    void ingest(List<Metrics> metricsList) throws TokensIngestionException;
}
