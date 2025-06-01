# final-bachelor-project
this repo provided for final bachelor project about vector search . simple implementation &amp; other use case in real world scenario


for fully understand project let me explain some concept

what is Information Retrieval?

    IR deals with retrieving documents from large collections, often in response to a user query.
    It focuses on unstructured data, such as free text, unlike traditional database systems that work with structured tables.


Applications:
    
    Web search engines (e.g., Google, Bing).
    Digital libraries, where research papers or books are indexed and searched.
    Enterprise search systems for internal organizational documents.


Boolean vs Ranked Retrieval

Boolean Retrieval: 

    Retrieves documents if they match the query exactly using Boolean operators (AND, OR, NOT). it only work for term matching

Ranked Retrieval:
    
    Orders documents by their relevance to the query, often using Cosine Similarity or other ranking methods. it can found setence meaning
    or Synonyms


--------------------------------------------------------------

introducing inverted index and component

## key concept

1. Text Processing
    <ul>
    <li>Tokenization: Splitting text into terms (e.g., words or phrases).</li>
    <li>Stop-word Removal: Eliminating common words like "the," "is," which do not help in retrieval.
    </li>
    <li>Normalization: Converting terms to a standard form (e.g., lowercase).
    </li>
    <li>Stemming/Lemmatization: Reducing terms to their root forms (e.g., "running" → "run").
    </li>
    </ul>



2. Inverted Index
    <ul>
    <li>An inverted index maps terms to the list of documents in which they appear, enabling efficient retrieval.</li>
    <li>Example:
   
        Term        Postings List
        -------------------------
        "cat"       [1, 3, 5]
        "dog"       [2, 3, 6]
    </li>
    <li>Postings List: Contains document IDs where the term appears, and optionally term frequencies or positions for advanced queries.
    </li>

    </ul>

3. Boolean Retrieval
    <ul>
    <li>Queries like "cat AND dog" are evaluated by intersecting the postings lists of "cat" and "dog."</li>
    <li>This retrieval model is simple but does not rank documents by relevance.</li>
   </ul>


---------------------------------------------------------------------------------------

intro to ranked retrieval

we want to introduce ranked retrieval using cosine similarity.

Cosine Similarity measures the angle between the vector representations of a query and a document in the Vector Space Model.

Formula:
    
    cos(θ) = (D • Q) / (||D|| ||Q||)

    D • Q: Dot product of document vector and query vector.
    ||D||: Magnitude of the document vector.
    ||Q||: Magnitude of the query vector.

example:
    
    Query = [1, 0, 1], Document = [2, 1, 0]
    Dot product: (1*2) + (0*1) + (1*0) = 2
    Magnitudes: ||D|| = sqrt(2^2 + 1^2 + 0^2) = sqrt(5), ||Q|| = sqrt(1^2 + 0^2 + 1^2) = sqrt(2)
    Cosine similarity: 2 / (sqrt(5) * sqrt(2)) ≈ 0.63

in classic approach we fund vector base on tf-idf document term .but also in newer version we using embedded with machine learning to find vector

TF-IDF Weighting

Term Frequency (TF) : Measures how often a term appears in a document. <br/>
Inverse Document Frequency (IDF): Gives higher importance to rare terms.

Formula)

    TF : duplication count at document/ total word count at document
    
    IDF : log N+1/DF+1 -> N : total doc  , DF : number of document containing term
    
    TF-IDF : TF * IDF


-----------------------------------------------------------------------------------

### Vector Space Model

each document and query represented as vector in n-dimensional space when n is size of vocab

value of each dimension is typically the tf-idf weight of corresponding term

#### it enable partial matching: <br/>
It can handle cases where documents partially overlap with the query, unlike Boolean retrieval.


Disadvantage of cosine

<ul>
<li>Sensitivity to Document Length: Cosine similarity doesn’t consider the length of documents. Longer documents may have lower cosine similarity scores even if they share substantial content.
</li>
<li>Lack of Semantic Understanding: Cosine similarity treats words or features as independent entities. It doesn’t capture the semantic meaning of words, making it less effective in understanding context.
</li>
<li>Sparse Data Issues: In high-dimensional spaces, where data is often sparse, cosine similarity can be less reliable. It might not accurately reflect the true similarity between data points.
</li>
<li>Normalization Dependency: Cosine similarity depends on vector normalization. Different normalization methods can yield different results, making comparisons sensitive to preprocessing choices.
</li>

</ul>


## embedding vector search

is process of finding most relevant vector to a query vector 

Applications:
<ul>
<li>Text Search (SentenceTransformers/BERT)</li>
<li>Image Similarity</li>
<li>Recommendation System</li>
</ul>

core idea : 

1. each item converted to <em>dense vector</em> (embedding)
2. Given <em>query vector</em> we compute similarity between it and all stored vector
3. Return <em>top-k closest</em> matches


## why is better than previous solution( tfidf + cosine)

1. it capture semantic meaning. <br/> forexample :


      "How to cook pasta" ≈ "Pasta cooking instructions"
      two above sentence is not same but meaning is same

      


   