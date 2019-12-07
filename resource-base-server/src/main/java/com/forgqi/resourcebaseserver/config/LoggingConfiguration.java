package com.forgqi.resourcebaseserver.config;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "logstash.enabled")
public class LoggingConfiguration {
    private static final String LOGSTASH_APPENDER_NAME = "LOGSTASH";

    private static final String ASYNC_LOGSTASH_APPENDER_NAME = "ASYNC_LOGSTASH";


    public LoggingConfiguration(@Value("${spring.application.name}") String appName,
                                @Value("${server.port:8080}") String serverPort,
                                @Value("${logstash.host}") String logstashHost,
                                @Value("${logstash.port}") Integer logstashPort,
                                ObjectProvider<BuildProperties> buildProperties,
                                ObjectMapper mapper) throws JsonProcessingException {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        buildProperties.ifAvailable(it -> map.put("version", it.getVersion()));
        String customFields = mapper.writeValueAsString(map);

        log.info("Initializing Logstash logging");

        LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
        logstashAppender.setName(LOGSTASH_APPENDER_NAME);
        logstashAppender.setContext(context);

        // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
        LogstashEncoder logstashEncoder = new LogstashEncoder();
        // Set the Logstash appender config from JHipster properties
        logstashEncoder.setCustomFields(customFields);
        // Set the Logstash appender config from JHipster properties
        logstashAppender.addDestinations(new InetSocketAddress(logstashHost, logstashPort));

        ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);
        logstashEncoder.setThrowableConverter(throwableConverter);

        logstashAppender.setEncoder(logstashEncoder);
        logstashAppender.start();

        // Wrap the appender in an Async appender for performance
        AsyncAppender asyncLogstashAppender = new AsyncAppender();
        asyncLogstashAppender.setContext(context);
        asyncLogstashAppender.setName(ASYNC_LOGSTASH_APPENDER_NAME);
        asyncLogstashAppender.setQueueSize(512);
        asyncLogstashAppender.addAppender(logstashAppender);
        asyncLogstashAppender.start();

        context.getLogger("ROOT").addAppender(asyncLogstashAppender);
    }
}
