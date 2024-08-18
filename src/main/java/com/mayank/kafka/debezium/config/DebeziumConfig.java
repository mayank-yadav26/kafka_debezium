package com.mayank.kafka.debezium.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mayank.kafka.debezium.service.CustomerService;

import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;

@Configuration
public class DebeziumConfig {

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_NAME = "debezium_test";
    private static final String TABLE_INCLUDE_LIST = "debezium_test.customer";

    @Bean
    public io.debezium.config.Configuration customerConnectorConfig() {
        return io.debezium.config.Configuration.create()
            .with("name", "customer-mysql-connector")
            .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with("offset.storage.file.filename", "/tmp/offsets.dat")
            .with("offset.flush.interval.ms", "60000")
            .with("database.hostname", DB_HOST)
            .with("database.port", DB_PORT)
            .with("database.user", DB_USERNAME)
            .with("database.password", DB_PASSWORD)
            .with("database.dbname", DB_NAME)
            .with("table.include.list", TABLE_INCLUDE_LIST)
            .with("database.server.id", "10181")
            .with("database.server.name", "customer-mysql-db-server")
            .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
            .with("database.history.file.filename", "/tmp/dbhistory.dat")
            .with("topic.prefix", "customer-debezium") 
            .with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")
            .with("schema.history.internal.kafka.topic", "customer-debezium.schema.history")
            .build();
    }

    @Bean
    public DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine(CustomerService customerService, io.debezium.config.Configuration customerConnectorConfig) {
        return DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
            .using(customerConnectorConfig.asProperties())
            .notifying(customerService::handleChangeEvent)
            .build();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public ExecutorService startDebeziumEngine(DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine, ExecutorService executorService) {
        executorService.execute(debeziumEngine);
        return executorService;
    }
}
