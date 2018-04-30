package com.rackspacecloud.blueflood.metricsmetadataservice.model;

import lombok.Data;

@Data
public class Metrics {
    private float metricValue;
    private long collectionTime;
    private long ttlInSeconds;
    private String unit;
    private Locator locator;
}
