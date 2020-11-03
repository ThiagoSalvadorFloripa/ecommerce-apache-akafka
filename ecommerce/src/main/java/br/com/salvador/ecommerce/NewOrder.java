package br.com.salvador.ecommerce;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrder {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // poderia ler esse properties de um arquivo tbm
        var producer = new KafkaProducer<String, String>(properties());

        /*
             Primeiro parametro é o topico, depois a chave e depois o valor, pode
             variar conforme o metodo que está senso usado
         */
        var value = "idDopedido, idUsuario, valor";
        var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER",value, value);
        /*
            Coloca o get é que faz esperar a mensagem pq não é assincrono
            O Segundo parametro é necessário para saber se deu sucesso ou não
         */
        producer.send(record, (data, ex)-> {
            if(ex != null){
                ex.printStackTrace();
                return;
            }
            System.out.println(
                    "Sucesso enviado" + data.topic()
                    + ":::patition" + data.partition()
                    + "/ offset" + data.offset()
                    + "/ timestamp" + data.timestamp()
            );
        }).get();

        //NOTA: nessa etapa o zookeeper e o kafka tem que está rodando no terminal
        // para ver as mensagens tem que criar um consumidor do inicio
        //./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic ECOMMERCE_NEW_ORDER --from-beginning
    }
    private static Properties properties() {
        //adicionando propriedades para o properties
        var properties = new Properties();

        // defininado conexao
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        //seriadores de strings para bytes
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //seriadores para mensagem bytes
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }
}
