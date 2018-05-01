package com.rackspacecloud.blueflood.metricsmetadataservice.config;

public enum ElasticsearchConfig {
    HOSTS("127.0.0.1:9200,127.0.0.1:8200,127.0.0.1:8700"),
    CLUSTERNAME("elasticsearch"),
    INDEX_NAME_WRITE("metric_metadata"),
    INDEX_NAME_READ("metric_metadata"),
    TOKEN_INDEX_NAME_READ("metric_tokens"),
    TOKEN_INDEX_NAME_WRITE("metric_tokens");

    private String defaultValue;

    private ElasticsearchConfig(String value) {
        this.defaultValue = value;
    }

    @Override
    public String toString() {
        return defaultValue;
    }
}
