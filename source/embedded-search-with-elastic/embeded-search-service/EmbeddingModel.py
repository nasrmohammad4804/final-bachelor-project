# bert_api.py
from sentence_transformers import SentenceTransformer
from AppConfig import MODEL_NAME

model = SentenceTransformer(MODEL_NAME)

def embed(sentence):

    embeddings = model.encode(sentence).tolist()
    return embeddings
