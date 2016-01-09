#!/usr/bin/python3
import os
import subprocess
import fileinput
import logging
logging.basicConfig(level='INFO')


def save_frequencies(prog, output_folder, qid, query):
    command = [prog, query]
    with open(os.path.join(output_folder, qid + ".txt"), 'w') as fd:
        subprocess.check_call(command, stdout=fd)


if __name__ == '__main__':
    FREQ_COMMAND = './pseudo_relevance_feedback.sh'
    OUTPUT_FOLDER = 'frequencies'

    os.mkdir(OUTPUT_FOLDER)
    for line in fileinput.input():
        line = [l.strip() for l in line.split(';')]
        qid, query = line[0], " ".join(line[1:])
        logging.info("{}: {}".format(qid, query))
        save_frequencies(FREQ_COMMAND, OUTPUT_FOLDER, qid, query)
