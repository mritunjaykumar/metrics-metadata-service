package com.rackspacecloud.blueflood.metricsmetadataservice.service;

public enum MetricsMetadataFieldLabel {
    TENANT_ID("tenantId"),
    METRIC_NAME("metric_name"),
    UNIT("unit");

    private String defaultValue;

    private MetricsMetadataFieldLabel(String value) {
        this.defaultValue = value;
    }

    @Override
    public String toString() {
        return defaultValue;
    }
}
