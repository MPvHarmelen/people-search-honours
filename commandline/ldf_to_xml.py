#!/usr/bin/python3
import logging
import fileinput
import re

logging.basicConfig(level='INFO')

OUTPUT_FILE = 'output.xml'

# id_re is also the starting re
id_re = re.compile(r'<DOC\s+([^>]+)>\n')
closing_re = re.compile(r'</DOC>\n')

FILE_START = '<parameters>\n'
FILE_END = '</parameters>\n'
XML_FORMAT = '''    <query>
        <number>{id}</number>
        <text>{text}</text>
    </query>
'''

if __name__ == '__main__':
    with open(OUTPUT_FILE, 'w') as outfd:
        outfd.write(FILE_START)
        inp = iter(fileinput.input())
        for line in inp:
            logging.debug(line)
            # input
            first = id_re.match(line)
            if first is None:
                logging.debug('skip')
                continue
            q_id = first.groups()[0]
            q_text = []
            for line in inp:
                if closing_re.match(line) is not None:
                    break
                logging.debug('query: {}'.format(line))
                q_text.append(line)

            # output
            logging.debug('save')
            outfd.write(XML_FORMAT.format(id=q_id, text=''.join(q_text)))
        outfd.write(FILE_END)
