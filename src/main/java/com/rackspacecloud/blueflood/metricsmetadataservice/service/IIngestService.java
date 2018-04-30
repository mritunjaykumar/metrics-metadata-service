package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;

import java.util.List;

public interface IIngestService {
    void ingest(List<Metrics> metricsList);
}
