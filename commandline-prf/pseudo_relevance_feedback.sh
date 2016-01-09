#!/bin/bash

INDEX=../indices/tu-no-expansion-no-stop/
DOC_K=3

QUERY=$1

# count=0
for doc in $(IndriRunQuery -index=$INDEX -query="$QUERY" -baseline=tfidf -count=$DOC_K| cut -f 2); do
    # do stuff
    doc_id=$(dumpindex $INDEX di docno $doc)
    doc_ids+="$doc_id|" # The bar is for the regex I use later on

    # Words
    # Skip the first two lines, then only take the last (third) column
    words+=$(dumpindex $INDEX dv $doc_id | tail -n+3 | cut -d" " -f 3 )

    # # loop termination
    # let count++
    # if [[ $count == $DOC_K ]]; then
    #     break
    # fi
done
# echo $(echo $words | wc -w)
for word in $(echo $words | ./make_set.py); do
    tf=0
    for c in $(dumpindex $INDEX t $word | grep -E "^($doc_ids) " | cut -d" " -f 2); do
        let tf+=c
    done
    let df=$(dumpindex $INDEX t $word | wc -l)-1
    echo $word $tf $df
done
