#!/usr/bin/python3
import os

LANGUAGES = ['nl', 'uk']
WORDKS = [1, 2, 4, 8, 16]
# SMOOTHING_METHODS = ['dirichlet', 'jelinek_mercer']
MODELS = [1, 2]


DIRECTORY = '../confs-ears'
FILENAME_FORMAT = os.path.join(
    '{dir}',
    'tu-{language}-model{model}-{smoothing_method}-prf-{word_k}.conf'
)
SMOOTHING_DICT = {
    'dirichlet': '  <smoothingMethod>dirichlet</smoothingMethod>',
    'jelinek_mercer': '  <smoothingMethod>jm</smoothingMethod>\n'
                      '  <smoothingParam>0.5</smoothingParam>'
}
FILE_FORMAT = '''<parameter>
  <runID>{language}, model {model}, {smoothing_method}, prf {word_k}</runID>
  <model>{model}</model>
  <index>indices/tu-no-expansion-no-stop</index>
  <queryFile>people-search-honours/prf-output/topics_{language}_{word_k}.ldf</queryFile>
  <associationFileFormat>5</associationFileFormat>
  <associationFile>tu-expert-collection/data/assoc/all.assoc</associationFile>
  {smoothing_method_xml}
  <outputFile>people-search-honours/output/tu-{language}-model{model}-{smoothing_method}-prf-{word_k}.trec</outputFile>
  <outputFileFormat>0</outputFileFormat>
</parameter>
'''


def create_file(name_format, content_format, kwargs):
    with open(name_format.format(**kwargs), 'w') as fd:
        fd.write(content_format.format(**kwargs))


if __name__ == '__main__':
    for language in LANGUAGES:
        for word_k in WORDKS:
            for model in MODELS:
                for smoothing_method in SMOOTHING_DICT:
                    create_file(FILENAME_FORMAT, FILE_FORMAT, {
                        'dir': DIRECTORY,
                        'language': language,
                        'word_k': word_k,
                        'model': model,
                        'smoothing_method': smoothing_method,
                        'smoothing_method_xml': SMOOTHING_DICT[smoothing_method]
                    })
