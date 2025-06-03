from kafka import KafkaConsumer

from AppConfig import *
from ElasticService import *
from EmbeddingModel import embed
import json

def save_consumed_data(data):
    embedded_text = embed(data['body'])
    data['dimensions'] = embedded_text
    es.index(index=INDEX_NAME, document=data)


def start_consumer():
    consumer = KafkaConsumer(KAFKA_TOPIC_NAME,
                             bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
                             auto_offset_reset=KAFKA_AUTO_OFFSET_RESET,
                             enable_auto_commit=True,
                             group_id=KAFKA_TOPIC_GROUP_NAME,
                             value_deserializer=lambda m: json.loads(m.decode('utf-8')))


    for message in consumer:
        data = message.value
        save_consumed_data(data)
