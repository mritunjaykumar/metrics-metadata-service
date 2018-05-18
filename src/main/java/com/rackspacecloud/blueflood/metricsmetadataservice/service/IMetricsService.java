package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.MetricsIngestionException;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;

import java.util.List;

public interface IMetricsService {
    void ingest(List<Metrics> metricsList) throws MetricsIngestionException;
}
