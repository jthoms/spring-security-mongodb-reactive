package com.demo.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

/**
 * Production Mongodb configuration for @Profile(value="PROD"), not activated in this sample.
 * 
 * @author John Thoms
 */
@Configuration
@AutoConfigureAfter(EmbeddedMongoAutoConfiguration.class)
@Profile(value="PROD")
public class MongoConfig extends AbstractReactiveMongoConfiguration {	
	@Value("${spring.data.mongodb.port:27017}")
	private int port;
	
	@Value("${spring.data.mongodb.database:'demo'}")
	private String databaseName;

	@Bean
	public LoggingEventListener mongoEventListener() {
		return new LoggingEventListener();
	}

	@Override
	@Bean
	@DependsOn("embeddedMongoServer")
	public MongoClient reactiveMongoClient() {
		return MongoClients.create(String.format("mongodb://localhost:%d", port));
	}

	@Override
	protected String getDatabaseName() {
		return this.databaseName;
	}
}