package com.rackspacecloud.blueflood.metricsmetadataservice.model;

import lombok.Data;

@Data
public class Locator {
    private String stringRep;
    private String tenantId;
    private String metricName;
}
