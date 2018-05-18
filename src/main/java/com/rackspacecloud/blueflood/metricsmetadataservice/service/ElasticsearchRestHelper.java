package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ElasticsearchRestHelper {
    private static final String DOCUMENT_TYPE = "metrics";
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchRestHelper.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${elasticsearch.api.url}")
    String ELASTICSEARCH_API_URL;

    /**
     * Bulk Index in Elasticsearch using rest api.
     * @param urlFormat
     * @param bulkString
     */
    public void index(String urlFormat, String bulkString) throws Exception {
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
                //TODO: Need to work on multiple ES endpoints.
//                url = String.format(urlFormat, getNextBaseUrl());
//                callQ.add(url);
            }
            else {
                String errorMessage = String.format(
                        "index method failed with status code: %s and exception message: %s",
                        response.getStatusCode(), e.getMessage());
                LOGGER.error(errorMessage);

                throw new Exception(errorMessage, e);
            }
        }

        if(response == null) {
            throw new Exception("Index method failed with null response");
        }
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
