package com.rackspacecloud.blueflood.metricsmetadataservice.controller;

import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import com.rackspacecloud.blueflood.metricsmetadataservice.service.IMetricsService;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IngestControllerTest {

    @Test
    public void ingest_callsIngestServiceIngestMethodOnce() {
        IMetricsService ingestService = mock(IMetricsService.class);
        ingestService.ingest(new ArrayList<Metrics>());
        verify(ingestService, times(1)).ingest(anyList());
    }
}
