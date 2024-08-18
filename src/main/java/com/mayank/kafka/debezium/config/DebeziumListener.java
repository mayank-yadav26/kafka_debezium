//package com.mayank.kafka.debezium.config;
//
//import static io.debezium.data.Envelope.FieldName.AFTER;
//import static io.debezium.data.Envelope.FieldName.BEFORE;
//import static io.debezium.data.Envelope.FieldName.OPERATION;
//import static java.util.stream.Collectors.toMap;
//
//import java.util.Map;
//
//import org.apache.kafka.connect.data.Field;
//import org.apache.kafka.connect.data.Struct;
//import org.apache.kafka.connect.source.SourceRecord;
//
//import com.mayank.kafka.debezium.service.CustomerService;
//import com.mysql.cj.conf.ConnectionUrlParser.Pair;
//
//import io.debezium.config.Configuration;
//import io.debezium.data.Envelope.Operation;
//import io.debezium.embedded.Connect;
//import io.debezium.engine.DebeziumEngine;
//import io.debezium.engine.RecordChangeEvent;
//import io.debezium.engine.format.ChangeEventFormat;
//
//public class DebeziumListener{
//	private DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
//	public  DebeziumListener(Configuration customerConnectorConfiguration, CustomerService customerService) {
//
//	    this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
//	      .using(customerConnectorConfiguration.asProperties())
//	      .notifying(this::handleEvent)
//	      .build();
//	}
//	
//	private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
//	    SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
//	    Struct sourceRecordChangeValue= (Struct) sourceRecord.value();
//
//	    if (sourceRecordChangeValue != null) {
//	        Operation operation = Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));
//
//	        if(operation != Operation.READ) {
//	            String record = operation == Operation.DELETE ? BEFORE : AFTER;
//	            Struct struct = (Struct) sourceRecordChangeValue.get(record);
//	            Map<String, Object> payload = struct.schema().fields().stream()
//	              .map(Field::name)
//	              .filter(fieldName -> struct.get(fieldName) != null)
//	              .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
//	              .collect(toMap(Pair::getKey, Pair::getValue));
//
//	            this.customerService.replicateData(payload, operation);
//	        }
//	    }
//	}
//	
//}
