package com.rackspacecloud.blueflood.metricsmetadataservice.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdFlavor;
import io.micrometer.statsd.StatsdMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegrafConfig {

    @Bean
    StatsdConfig statsdConfig(){
        return new StatsdConfig() {
            @Override
            public String get(String k) {
                return null;
            }

            @Override
            public StatsdFlavor flavor() {
                return StatsdFlavor.TELEGRAF;
            }
        };
    }

    @Bean
    MeterRegistry registry(){
        return new StatsdMeterRegistry(statsdConfig(), Clock.SYSTEM);
    }
}
