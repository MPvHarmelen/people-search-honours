import nltk, re, pprint
from nltk import word_tokenize
from bs4 import BeautifulSoup
from glob import glob

# Take all documents in corpus
folder = glob('../tu-expert-collection/data/trectext/*')
with open('expert-collection.txt','w') as new_document:
		
	for document_path in folder:
		with open(document_path) as document:
			text = document.read()

			# Strip html tags
			text = BeautifulSoup(text).get_text()

			# Change text into lower case words without tabs or punctuation  etc.
			text = text.lower()
			text = re.sub(r'[^\w\s]','',text)	
			text = re.sub(r'\s+', ' ', text)
			new_document.write(text + '\n')
