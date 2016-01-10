#!/bin/bash
GTS="1 5"
SMOOTHING_METHODS="dirichlet"
LANGUAGES="uk"
MODELS="1 2"
MEASURE="map"

for LANGUAGE in $LANGUAGES; do
for SMOOTHING_METHOD in $SMOOTHING_METHODS; do
for MODEL in $MODELS; do
for GT in $GTS; do
    echo "$LANGUAGE $SMOOTHING_METHOD model $MODEL gt$GT"
    FILES=$(ls -1v ../trec_out/ | grep "tu-$LANGUAGE-model$MODEL-$SMOOTHING_METHOD-.*\.gt$GT" | awk '{print "../trec_out/"$0}')
    # for FILE in $FILES; do
    #     echo $FILE
    # done
    cat $FILES | grep "^$MEASURE " | cut -f 3
done
done
done
done
