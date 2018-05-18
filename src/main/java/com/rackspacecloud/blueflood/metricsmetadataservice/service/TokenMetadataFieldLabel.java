package com.rackspacecloud.blueflood.metricsmetadataservice.service;

public enum TokenMetadataFieldLabel {
    METRIC_NAME("metric_name"),
    TENANT_ID("tenantId"),
    UNIT("unit"),
    TOKEN("token"),
    PARENT("parent"),
    IS_LEAF("isLeaf");

    private String defaultValue;

    private TokenMetadataFieldLabel(String value){
        this.defaultValue = value;
    }

    @Override
    public String toString(){
        return this.defaultValue;
    }
}