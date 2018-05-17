package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import com.rackspacecloud.blueflood.metricsmetadataservice.config.ElasticsearchConfig;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Locator;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MetricsService implements IMetricsService {
    private static final String DOCUMENT_TYPE = "metrics";
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${elasticsearch.api.url}")
    String ELASTICSEARCH_API_URL;

    @Override
    public void ingest(List<Metrics> metricsList) {
        String bulkString = bulkStringify(metricsList);
        String urlFormat = "%s/_bulk";
        index(urlFormat, bulkString);
    }

    /**
     * Bulk Index in Elasticsearch using rest api.
     * @param urlFormat
     * @param bulkString
     */
    private void index(String urlFormat, String bulkString){
        //TODO: For now using only one ES URL
        String url = String.format(urlFormat, ELASTICSEARCH_API_URL);
        HttpEntity<String> requestEntity = new HttpEntity<>(bulkString, getHeaders());

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            HttpStatus status = response.getStatusCode();
            if(status != HttpStatus.OK){
                LOGGER.error("index method failed with HTTP status: {}", status);
            }
        }
        catch(Exception e){
            if(response == null){
                LOGGER.error("index method failed with message: {}", e.getMessage());
//                url = String.format(urlFormat, getNextBaseUrl());
//                callQ.add(url);
            }
            else {
                LOGGER.error("index method failed with status code: {} and exception message: {}",
                    response.getStatusCode(), e.getMessage());
            }
        }
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
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
