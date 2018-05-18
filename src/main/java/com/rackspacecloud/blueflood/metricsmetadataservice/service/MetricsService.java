package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import com.rackspacecloud.blueflood.metricsmetadataservice.config.ElasticsearchConfig;
import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.MetricsIngestionException;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Locator;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MetricsService implements IMetricsService {
    private static final String DOCUMENT_TYPE = "metrics";
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

    @Autowired
    ElasticsearchRestHelper elasticsearchRestHelper;

    @Override
    public void ingest(List<Metrics> metricsList) throws MetricsIngestionException {
        String bulkString = bulkStringify(metricsList);
        String urlFormat = "%s/_bulk";

        try {
            elasticsearchRestHelper.index(urlFormat, bulkString);
        }
        catch(Exception e){
            throw new MetricsIngestionException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Creates formatted string as per Elasticsearch Bulk API contract.
     * @param metrics
     * @return Formatted string as per Elasticsearch Bulk API contract
     */
    private String bulkStringify(List<Metrics> metrics){
        StringBuilder sb = new StringBuilder();
        String actionAndMetaDataFormat =
                "{ \"index\" : { \"_index\" : \"%s\", \"_type\" : \"%s\", \"_id\" : \"%s\", \"routing\" : \"%s\" } }%n";

        for(Metrics metric : metrics){
            Locator locator = metric.getLocator();

            if(locator.getMetricName() == null)
                throw new IllegalArgumentException("trying to insert metric discovery without a metricName");

            String tenantId = locator.getTenantId();

            sb.append(String.format(actionAndMetaDataFormat,
                    ElasticsearchConfig.INDEX_NAME_WRITE,
                    DOCUMENT_TYPE,
                    tenantId + ":" + locator.getMetricName(),
                    tenantId));

            if(StringUtils.isEmpty(metric.getUnit())){
                sb.append(String.format(
                        "{ \"%s\" : \"%s\", \"%s\" : \"%s\", \"%s\" : \"%s\" }%n",
                        MetricsMetadataFieldLabel.TENANT_ID.toString(), locator.getTenantId(),
                        MetricsMetadataFieldLabel.METRIC_NAME.toString(), locator.getMetricName(),
                        MetricsMetadataFieldLabel.UNIT.toString(), metric.getUnit()));
            }
            else {
                sb.append(String.format(
                        "{ \"%s\" : \"%s\", \"%s\" : \"%s\" }%n",
                        MetricsMetadataFieldLabel.TENANT_ID.toString(), locator.getTenantId(),
                        MetricsMetadataFieldLabel.METRIC_NAME.toString(), locator.getMetricName()));
            }
        }

        return sb.toString();
    }
}
