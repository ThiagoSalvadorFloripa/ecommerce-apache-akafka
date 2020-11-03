package br.com.salvador.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class FraudeDectorService {
    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(properties());
        //escutando do topico, poderia ter vario topico para escutar mais é errado
        consumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER"));
        // verifica se tem mensagem
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Encontrei" + records.count() + "registros");
                for (var record : records) {
                    System.out.println("------------------------------");
                    System.out.println("Processing new order, checking  for fraud");
                    System.out.println(record.key());
                    System.out.println(record.value());
                    System.out.println(record.partition());
                    System.out.println(record.offset());
                }
            }
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        // definindo a porta onde vai rodar o consumer
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        //deserializador da chave
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //deserializador do valor
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // definindo nome do grupo é obrigatorio
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudeDectorService.class.getSimpleName());
//        properties.setProperty(ConsumerConfig.);
        return properties;
    }
}
