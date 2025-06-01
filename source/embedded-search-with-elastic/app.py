import json

import logging
from flask import Flask, request, jsonify
from elasticsearch import Elasticsearch
from sympy import false

from embeddingModel import embed
app = Flask(__name__)


es = Elasticsearch(
    "http://localhost:9200",
    api_key=("fSaoKpcBZC7B-3-2kXCi","e-AblgRdS-Se5-AfuqbV1g"),
    verify_certs=false
)

INDEX_NAME = "embedding_vector"
DEFAULT_TOP_K_RESULTS = 3

@app.route('/add', methods=['POST'])
def add_document():
    data = request.json
    print("test ")
    res=''
    try:
        text = data['text']
        embeddings = embed(text)
        res = es.index(index=INDEX_NAME, document={
            "text": text,
            "embedding": embeddings
            })
    except Exception as ex:
        print(ex)

    return jsonify(res["result"])

@app.route('/search', methods=['POST'])
def search():
    data = request.json
    query = data["query"]
    embedding_vector = embed(query)

    response = es.search(index=INDEX_NAME, body={
        "knn": {
            "field": "embedding",
            "query_vector": embedding_vector,
            "k": DEFAULT_TOP_K_RESULTS,
            "num_candidates": 100
        },
        "_source": ["text"]
    })
    results = [hit["_source"] for hit in response["hits"]["hits"]]
    return jsonify(results)


def initialize_index():

    with open('mapping.json', 'r') as f:
        data = json.load(f)

    try:
        if not es.indices.exists(index=INDEX_NAME):
            es.indices.create(index=INDEX_NAME, mappings=data)
            print("index created")
        else:
            print("Index already exists.")

    except Exception as e:
        print(e.__cause__)


# Run Flask app
if __name__ == "__main__":

    initialize_index()
    app.run(port=5000)



