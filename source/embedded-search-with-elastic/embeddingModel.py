# bert_api.py
from sentence_transformers import SentenceTransformer

model = SentenceTransformer('all-MiniLM-L6-v2')

def embed(sentence):

    embeddings = model.encode(sentence).tolist()
    return embeddings
