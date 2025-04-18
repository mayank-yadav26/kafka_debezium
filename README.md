# Kafka Debezium Change Data Capture

A Spring Boot application that uses Debezium and Kafka for change data capture (CDC) from MySQL databases.

## Overview

This project demonstrates how to use Debezium's embedded connector to capture data changes from a MySQL database. It uses Debezium's event-driven architecture to listen for database changes and process them in real-time without using Kafka Connect.

## Features

- Real-time database change capture using Debezium
- Embedded Debezium engine configuration
- MySQL connector setup
- Processing of CDC events (CREATE, UPDATE, DELETE operations)
- Spring Boot REST API for monitoring status

## Prerequisites

- Java 17 or higher
- MySQL 5.7 or higher
- Maven 3.6 or higher

## Setup

### Database Configuration

1. Create a MySQL database named `debezium_test`
2. Create a table named `customer` in the `debezium_test` database
3. Make sure the MySQL user has the appropriate permissions for CDC (binlog access)

### Configuration

The application is pre-configured with the following settings in `DebeziumConfig.java`:

- MySQL Host: localhost
- MySQL Port: 3306
- MySQL Username: root
- MySQL Password: root
- Database Name: debezium_test
- Table Include List: debezium_test.customer

Modify these settings in `DebeziumConfig.java` if your setup differs.

## Running the Application

```bash
# Clone the repository
git clone <repository-url>
cd kafaka_debezium

# Build the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

## API Endpoints

- `GET /status`: Check if the Debezium engine is running

## How It Works

1. The application starts a Debezium engine when the Spring context is initialized
2. Debezium connects to MySQL and starts monitoring the binlog for changes
3. When a change occurs (INSERT, UPDATE, DELETE) in the monitored table, Debezium captures it
4. The `CustomerService.handleChangeEvent()` method processes these events
5. The processed events can be used for various purposes (sent to another system, stored, etc.)

## Customizing

To customize the event handling, modify the `CustomerService.handleChangeEvent()` method to implement your business logic.

## Development

This project uses:

- Spring Boot 3.3.2
- Debezium 2.5.0.Final
- MySQL Connector for Debezium

## Troubleshooting

- Ensure MySQL binlog is enabled
- Check that the MySQL user has sufficient permissions
- Verify that the MySQL connection details are correct
- Examine the logs for any Debezium-related errors
