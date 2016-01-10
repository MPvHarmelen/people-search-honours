#!/bin/bash
OUTPUT_DIR=../output

# Save working directory
WORKING_DIR=$(pwd)

# Convert input file to absolute path
INPUT_FILE=$(readlink -m $1)

# Create output directory if it doesn't exist
mkdir -p $OUTPUT_DIR

# Run ears
cd ../..
ears-reloaded/ears ef $INPUT_FILE

# Run trec_eval
cd $WORKING_DIR
./trec_eval.sh $OUTPUT_DIR/$(expr $INPUT_FILE : '.*/\(.*\)\.conf').trec
