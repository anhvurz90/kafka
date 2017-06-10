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
