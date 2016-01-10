#!/bin/bash
for file in $(ls ../confs-ears); do
    ./ears_and_trec.sh ../confs-ears/$file
done
