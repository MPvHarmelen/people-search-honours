#!/bin/bash
for file in $(ls ../confs-ears | grep nl.*no_expansion); do
    ./ears_and_trec.sh ../confs-ears/$file
done
