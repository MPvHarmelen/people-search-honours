#!/bin/bash
GTS="1 5"
SMOOTHING_METHODS="dirichlet"
LANGUAGES="nl uk"
MODELS="1 2"
MEASURE="map"
EXPANSION_METHODS="topics_klein topics_groot glove"

for LANGUAGE in $LANGUAGES; do
for SMOOTHING_METHOD in $SMOOTHING_METHODS; do
for MODEL in $MODELS; do
for EXPANSION_METHOD in $EXPANSION_METHODS; do
for GT in $GTS; do
    # echo "$LANGUAGE $SMOOTHING_METHOD model $MODEL $EXPANSION_METHOD gt$GT"
    echo "$LANGUAGE	m$MODEL	gt$GT	$EXPANSION_METHOD"
    FILES=$(ls -1v ../trec_out/ | grep -P "tu-$LANGUAGE-model$MODEL-$SMOOTHING_METHOD-($EXPANSION_METHOD-\d+|no_expansion)\.gt$GT" | awk '{print "../trec_out/"$0}')
    # for FILE in $FILES; do
    #     echo $FILE
    # done
    cat $FILES | grep "^$MEASURE " | cut -f 3
done
done
done
done
done
