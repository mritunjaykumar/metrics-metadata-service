package com.rackspacecloud.blueflood.metricsmetadataservice.service;

import com.rackspacecloud.blueflood.metricsmetadataservice.config.ElasticsearchConfig;
import com.rackspacecloud.blueflood.metricsmetadataservice.exceptions.TokensIngestionException;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Locator;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Metrics;
import com.rackspacecloud.blueflood.metricsmetadataservice.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class TokenService implements ITokenService {
    private static final String DOCUMENT_TYPE = "tokens";
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    ElasticsearchRestHelper elasticsearchRestHelper;

    @Override
    public void ingest(final List<Metrics> metricsList) throws TokensIngestionException {
        List<Locator> locators = getLocators(metricsList);
        List<Token> tokens = new ArrayList<>();
        tokens.addAll(getTokens(locators));

        if(tokens.size() == 0) return;

        String bulkString = bulkStringifyTokens(tokens);
        String urlFormat = "%s/_bulk";
        try {
            elasticsearchRestHelper.index(urlFormat, bulkString);
        } catch (Exception e) {
            throw new TokensIngestionException(e.getMessage(), e.getCause());
        }
    }

    private String bulkStringifyTokens(List<Token> tokens){
        StringBuilder sb = new StringBuilder();

        for(Token token : tokens){
            sb.append(String.format(
                    "{ \"index\" : { \"_index\" : \"%s\", \"_type\" : \"%s\", \"_id\" : \"%s\", \"routing\" : \"%s\" } }%n",
                    ElasticsearchConfig.TOKEN_INDEX_NAME_WRITE, DOCUMENT_TYPE,
                    token.getId(), token.getLocator().getTenantId()));

            sb.append(String.format("{ \"%s\" : \"%s\", \"%s\" : \"%s\", \"%s\" : \"%s\", \"%s\" : \"%s\" }%n",
                    TokenMetadataFieldLabel.TOKEN.toString(), token.getToken(),
                    TokenMetadataFieldLabel.PARENT.toString(), token.getParent(),
                    TokenMetadataFieldLabel.IS_LEAF.toString(), token.isLeaf(),
                    TokenMetadataFieldLabel.TENANT_ID.toString(), token.getLocator().getTenantId()));
        }

        return sb.toString();
    }

    private List<Locator> getLocators(final List<Metrics> input) {
        return input.stream()
                .map(Metrics::getLocator)
                .collect(toList());
    }

    private Set<Token> getTokens(final List<Locator> locators) {
        return Token.getUniqueTokens(locators.stream()).collect(toSet());
    }
}
