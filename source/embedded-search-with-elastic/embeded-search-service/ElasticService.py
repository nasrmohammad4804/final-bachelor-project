import json

from elasticsearch import Elasticsearch

es = Elasticsearch(
    "http://localhost:9200",
    api_key=("fSaoKpcBZC7B-3-2kXCi", "e-AblgRdS-Se5-AfuqbV1g")
)

INDEX_NAME = "embedding_vector"
DEFAULT_TOP_K_RESULTS = 10


def initialize_index():
    print("Initializing index")
    try:
        if not es.indices.exists(index=INDEX_NAME):

            with open('Mapping.json', 'r') as f:
                data = json.load(f)
            es.indices.create(index=INDEX_NAME, mappings=data)
            print("index created")
        else:
            print("Index already exists.")

    except Exception as e:
        print(e.__cause__)


def remove_index():
    if es.exists(index=INDEX_NAME):
        es.indices.delete(index=INDEX_NAME)


def add_document(document):
    try:

        es.index(index=INDEX_NAME, document=document)
    except Exception as ex:
        print(ex)


def term_base_search(input):
    query = {
        "query": {
            "match": {
                "body": input
            },

        },
        "_source": ["body"]
    }
    try:
        response = es.search(index=INDEX_NAME, body=query, size=DEFAULT_TOP_K_RESULTS)
        results = [hit["_source"] for hit in response["hits"]["hits"]]
        return results
    except Exception as ex:
        print(ex)
        return None


def semantic_base_search(embedding_vector):
    query = {

        "field": "dimensions",
        "query_vector": embedding_vector,
        "k": DEFAULT_TOP_K_RESULTS,
        "num_candidates": 100

    }

    try:

        print(embedding_vector)

        response = es.search(index=INDEX_NAME, knn=query, source=["body"], size=DEFAULT_TOP_K_RESULTS)
        results = [hit["_source"] for hit in response["hits"]["hits"]]
        return results
    except Exception as ex:
        print(ex)
        return None
