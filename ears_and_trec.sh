#!/bin/bash

# Save working directory
WORKING_DIR=$(pwd)

# Convert input file to absolute path
INPUT_FILE=$(readlink -m $1)

# Create output directory if it doesn't exist
mkdir -p output

# Run ears
cd ..
ears-reloaded/ears ef $INPUT_FILE

# Run trec_eval
cd $WORKING_DIR
./trec_eval.sh output/$(expr $INPUT_FILE : '.*/\(.*\)\.conf').out