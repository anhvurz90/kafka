Apache Kafka
1.Install Zookeeper: {
  - tar -zxf zookeeper-3.4.6.tar
  - cd zookeeper-3.4.6
  - mkdir data
  - vi conf/zoo.cfg (clone & update zoo_sample.cfg)
  - bin/zkServer.sh start
  - bin/zkCli.sh
  - bin/zkServer.sh stop
}
2.Install Apacke Kafka: {
  - tar -zxf kafka_2.12-0.10.2.1.tgz
  - cd kafka_2.12-0.10.2.1
  - bin/kafka-server-start.sh conf/server.properties
  - bin/kafka-server-stop.sh conf/server.properties
}
3.Basic Operations: {
  3.1.Single Node - Single Broker Configuration: {
    - Create a Kafka Topic:
        bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Hello-Kafka
    - List of Topics:
        bin/kafka-topics.sh --list --zookeeper localhost:2181  
        Hello
        My first message
        My second message
    - Start Producer to Send Messages:
	bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Hello-Kafka
    - Start Consumer to Receive Messages
  	bin/kafka-console-consumer.sh --zookeeper localhost:2181 —topic Hello-Kafka 
--from-beginning
  }
  3.2.Single Node - Multiple Brokers Configuration: {
    - Clone from config/server.properties:
      + config/server-one.properties {
          broker.id=1
          port=9093
          log.dirs=/tmp/kafka-logs-1
      }
      + config/server-two.properties {
          broker.id=2
          port=9094
          log.dirs=/tmp/kafka-logs-2
      }
    - Start Multiple Brokers: {
      bin/kafka-server-start.sh conf/server.properties
      bin/kafka-server-start.sh conf/server-one.properties
      bin/kafka-server-start.sh conf/server-two.properties
    }
    - Create a Topic: {
      bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partition 1 --topic MultiBrokerApplication
    }
    - Describe a Topic: {
      bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic MultiBrokerApplication
    }
    - Start Producer to Send Messages: {
      bin/kafka-console-producer.sh --broker-list localhost:9092 --topic MultiBrokerApplication
    }
    - Start Consumer to Receive Messages: {
      bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic MultiBrokerApplication --from-beginning
    }
  }
  3.3.Basic Topic Operations: {
    - Modify a Topic: {
      bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic Hello-kafka --partitions 2
    }
    - Deleting a Topic: {
      bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Hello-kafka
    }
  }
}
4.Simple Producer Example: {
  4.1.KafkaProducer API: {
    - producer.send(new ProducerRecord<byte[], byte[]>(topic, partition, key1, value1),
                    callback);
    - public void flush()
    - public Map metrics()
    - public void close()      
  }
  4.2.Producer API: {
    - The Producer Class: {
        public void send(KeyedMessage<K, V> message)
        public void send(List<KeyedMessage<K, V>> messages)
        Properties prop = new Properties();
        prop.put(producer.type, "async")
        ProducerConfig config = new ProducerConfig(prop);
 	public void close()        
    }
  }
  4.3.Configuration Settings: {
    - client.id: identifies producer application
    - producer.type: either sync or async
    - acks: the acks config controls the criteria under producer requests are considered complete
    - retries: if producer request fails, then automatically retry with specific value.
    - bootstrap.servers: bootstrapping list of brokers
    - linger.ms: if you want to reduce the number of requests you can set linger.ms to something greater than some value
    - key.serializer: serializer interface for key
    - value.serializer: serializer interface for value
    - batch.size: buffer size
    - buffer.memory: controls the total amount of memory available to the producer for buffering
  }
  4.4.ProducerRecord API: {
    - public ProducerRecord(String topic, int partition, K key, V value)
    - public ProducerRecord(String topic, K key, V value)
    - public ProducerRecord(String topic, V value)
    - public String topic()
    - public K key()
    - public V value()
    - public int partition()
  }
  4.5.SimpleProducer application: {
        import java.util.Properties;
	import org.apache.kafka.clients.producer.KafkaProducer;
	import org.apache.kafka.clients.producer.Producer;
	import org.apache.kafka.clients.producer.ProducerConfig;
	import org.apache.kafka.clients.producer.ProducerRecord;
	import org.apache.kafka.common.serialization.StringSerializer;

	public class SimpleProducer {

	    private static final String TOPIC_NAME = "Hello-Kafka";
    
	    public static void main(String[] args) {
        	String topicName = TOPIC_NAME;
        
	        Properties props = new Properties();
	        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        	props.put(ProducerConfig.ACKS_CONFIG, "all");
	        props.put(ProducerConfig.RETRIES_CONFIG, 0);
	        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
	        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        
	        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
	        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
	        Producer<String, String> producer = new KafkaProducer<>(props);
        
        	for (int i = 0; i < 10; i++) {
	            producer.send(new ProducerRecord<String, String>(topicName,
        		            Integer.toString(i), Integer.toString(i)));
	        }
        
        	System.out.println("Message sent successfully!");
	        producer.close();
	    }
	}
  }
  4.6.KafkaConsumer API: {
    - public KafkaConsumer(Map<String, Object> configs)
    - public Set<TopicPartition> assignment()
    - public String subscription()
    - public void subscribe(List<String> topics, ConsumerRebalanceListener listener)
    - public void unsubscribe()
    - public void subscribe(List<String> topics)
    - public void subscribe(java.util.regex.Pattern pattern, ConsumerRebalanceListener listener)
    - public void assign(List<TopicPartition> partitions)
    - poll()
    - public void commitSync()
    - public void seek(TopicPartition partition, long offset)
    - public void resume()
    - public void wakeup()
    - 
  }
  4.7.ConsumerRecord API: {
    - public ConsumerRecord(String topic, int partition, long offset, K key, V value)
  }
  4.8.ConsumerRecords API: {
    - public ConsumerRecords(Map<TopicPartition, List<ConsumerRecord<K, v>>> records)
    - public int count()
    - public Set partitions()
    - public Iterator iterator()
    - public List records()
  }
  4.9.Configuration Settings: {
    - bootstrap.servers: Bootstrapping list of brokers.
    - group.id: Assigns an individual consumer to a group
    - enable.auto.commit: Enable auto commit for offsets if the value is true, otherwise not committed
    - auto.commit.interval.ms: Retun how often updated consumed offsets are written to Zookeeper.
    - session.timeout.ms: Indicates how many milliseconds Kafka will wait for the ZooKeeper to respond to a request(read or write) before giving up and continuing to consume messages.
  }
  4.10.SimpleConsumer application: {
	import java.util.Properties;
	import java.util.Arrays;

	import org.apache.kafka.clients.consumer.KafkaConsumer;
	import org.apache.kafka.common.serialization.StringDeserializer;
	import org.apache.kafka.clients.consumer.ConsumerConfig;
	import org.apache.kafka.clients.consumer.ConsumerRecord;
	import org.apache.kafka.clients.consumer.ConsumerRecords;

	public class SimpleConsumer {
    
	    private static final String TOPIC_NAME = "Hello-Kafka";
    
	    public static void main(String[] args) {
        	String topicName = TOPIC_NAME;
        
	        Properties props = new Properties();
        	props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        	props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
	        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        	props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
	        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        	props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
	        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        
        	consumer.subscribe(Arrays.asList(topicName));
        
	        System.out.println("Subscribed to topic: " + topicName);
        	int i = 0;
        
	        while (true) {
        	    ConsumerRecords<String, String> records = consumer.poll(100);
	            for (ConsumerRecord<String, String> record: records) {
        	        System.out.format("offset = %d, key = %s, value = %s\n",
                	        record.offset(), record.key(), record.value());
		    }
		}
        
	    }

	}	
  }
}
