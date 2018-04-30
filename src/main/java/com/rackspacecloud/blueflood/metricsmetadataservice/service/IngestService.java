package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestService implements IIngestService {

    @Override
    public void ingest(List<Metrics> metricsList) {
        for (Metrics metrics : metricsList){
            System.out.println(metrics.toString());
        }

    }
}
