import threading

from flask import Flask, request, jsonify
from ElasticService import *
from EmbeddingModel import embed
from ElasticService import initialize_index
from EtlDataConsumer import start_consumer
from elasticsearch import __version__ as es_version

app = Flask(__name__)

@app.route('/embed-search', methods=['POST'])
def embedding_search():
    data = request.json
    input = data["query"]
    print(input)
    embedding_vector = embed(input)
    result = semantic_base_search(embedding_vector)
    return jsonify(result)


@app.route('/search', methods=['POST'])
def search():
    data = request.json
    input = data["query"]
    result = term_base_search(input)
    return jsonify(result)

@app.route('/embed-data',methods=['POST'])
def embed_data():
    data = request.json
    input = data["query"]
    embedding_vector = embed(input)
    return jsonify(embedding_vector)


if __name__ == "__main__":
    print("Client version:", es_version)
    initialize_index()

    consumer_thread = threading.Thread(target= start_consumer,daemon=True)
    consumer_thread.start()

    app.run(port=5000)
