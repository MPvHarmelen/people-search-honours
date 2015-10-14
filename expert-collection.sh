make
time ./word2vec -train expert-collection.txt -output vectors.bin -cbow 1 -size 200 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -binary 1 -iter 15
./distance vectors.bin

# - desired vector dimensionality
# - the size of the context window for either the Skip-Gram or the Continuous Bag-of-Words model
# - training algorithm: hierarchical softmax and / or negative sampling
# - threshold for downsampling the frequent words 
# - number of threads to use
# - the format of the output word vector file (text or binary)

