package com.mayank.kafka.debezium.service;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.BEFORE;
import static io.debezium.data.Envelope.FieldName.OPERATION;
import static java.util.stream.Collectors.toMap;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Service;

import io.debezium.data.Envelope.Operation;
import io.debezium.engine.RecordChangeEvent;

@Service
public class CustomerService {

	public void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {

		SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
		Struct sourceRecordChangeValue = (Struct) sourceRecord.value();

		if (sourceRecordChangeValue != null) {
			// Check for the DDL field
			Field ddlField = sourceRecordChangeValue.schema().field("ddl");

			if (ddlField != null) {
				// This is a DDL event, handle it accordingly or skip
				String ddlStatement = sourceRecordChangeValue.getString("ddl");
				System.out.println("Received DDL event: " + ddlStatement);
				// If you're not interested in DDL events, you can return here
				return;
			}

			Operation operation = Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

			if (operation != Operation.READ) {
				String record = operation == Operation.DELETE ? BEFORE : AFTER;
				Struct struct = (Struct) sourceRecordChangeValue.get(record);
				Map<String, Object> payload = struct.schema().fields().stream().map(Field::name)
						.filter(fieldName -> struct.get(fieldName) != null)
						.map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
						.collect(toMap(Pair::getKey, Pair::getValue));

				// Process the change event (e.g., save to another database, publish to another
				// Kafka topic, etc.)
				System.out.println("Operation: " + operation.name() + ", Payload: " + payload);
			}
		}
	}
}
