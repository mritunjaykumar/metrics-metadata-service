package com.rackspacecloud.blueflood.metricsmetadataservice.controller;

import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import com.rackspacecloud.blueflood.metricsmetadataservice.service.IIngestService;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IngestControllerTest {

    @Test
    public void ingest_callsIngestServiceIngestMethodOnce() {
        IIngestService ingestService = mock(IIngestService.class);
        ingestService.ingest(new ArrayList<Metrics>());
        verify(ingestService, times(1)).ingest(anyList());
    }
}
