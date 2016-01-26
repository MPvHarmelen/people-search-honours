import numpy as np
import pickle
from sklearn.neighbors import NearestNeighbors
from bs4 import BeautifulSoup
from embedding_utils import load_binary_representations
from multiprocessing import Process

# Run this once, to load words and vector neighbours separately into external files
def find_neighbours(vectors,k):
    vectors_array = np.array(vectors)
    nbrs = NearestNeighbors(n_neighbors=k, algorithm='auto').fit(vectors_array)
    return nbrs.kneighbors(vectors_array)

def find_words_neighbours(bin_file, result_file, GloVe):
    if(GloVe):
        with open(bin_file,'rb') as glove_file:
            word_vectors = pickle.load(glove_file)
            words, vectors = word_vectors
    else:
        word_vectors = list(load_binary_representations(bin_file))
        words,vectors = zip(*word_vectors)
    distances, neighbours = find_neighbours(vectors,20)
    words_neighbours = {}
    for index in range(len(words)-1):
        word_neighbours = []
        for neighbour in neighbours[index]:
            word_neighbours.extend([words[neighbour]])
        words_neighbours[words[index]] = (word_neighbours, distances[index])
    with open(result_file,'wb') as result:
        pickle.dump(words_neighbours, result)

find_words_neighbours('vectors_comp.bin', 'words_neighbours_klein_comp.p', False)


#find_words_neighbours('words_vectors_glove.p', 'words_neighbours_glove.p', True)

def expand(file_topics, folder, save_as, k):
    with open(file_topics) as topics, open(folder + '/k='+str(k-1)+save_as, 'w') as expanded_topics, open('words_neighbours_klein_comp.p', 'rb') as neighbours_file:
        neighbours = pickle.load(neighbours_file)
        text = ""
        for line in iter(topics):
            line = line.replace('\n','')
            if '<DOC' in line or '</DOC>' in line or line not in neighbours:
                text = text + line + '\n'
            else:
                query = neighbours[line][0][0:k]

                for term in query:
                    text = text + term + '\n'
        expanded_topics.write(text)
        print('done')

def rotzooi(file_topics, folder, save_as):
	expand(file_topics, folder, save_as, 1)
	expand(file_topics, folder, save_as, 2)
	expand(file_topics, folder, save_as, 3)
	expand(file_topics, folder, save_as, 5)
	expand(file_topics, folder, save_as, 9)
	expand(file_topics, folder, save_as,17)


file_topics = '../../tu-expert-collection/topics/topics_nl.ldf'
save_as = 'nl_expanded_topics_klein_comp.ldf'
folder = 'topicsnl_klein'

rotzooi(file_topics, folder, save_as)

file_topics = '../../tu-expert-collection/topics/topics_uk.ldf'
save_as = 'uk_expanded_topics_klein_comp.ldf'
folder = 'topicsuk_klein'

rotzooi(file_topics, folder, save_as)
