import numpy as np
from embedding_utils import load_binary_representations

# Calculates distance to query vector for all word vectors
def all_distances(word_vector,generator,result):
    for word in generator:    
        dist = np.linalg.norm(word_vector-generator[word])
        result[word] = dist
    return result

# Finds k closest neighbours from a dict of words with distances
def find_neighbours(distances,k,neighbours):
    for word in distances:
        if neighbours: max_key = max(neighbours, key=neighbours.get)
        if k >= 0:
            neighbours[word] = distances[word]
            k -= 1
        elif neighbours[max_key] > distances[word]: 
            del neighbours[max_key]
            neighbours[word] = distances[word]
    return neighbours

# Expands the query with k closest neighbours
def expand_query(query,k):
    result = []
    generator = dict(load_binary_representations('../people-search-honours/vectors.bin'))
    for word in query.split(' '):
        word_vector = generator[word]
        distances = all_distances(word_vector,generator,{})
        neighbours = find_neighbours(distances,k,{})
        result.extend(neighbours.keys())
    return result

print(expand_query('dutch',2))