#!/usr/bin/python3
import fileinput

if __name__ == '__main__':
    s = set()
    for line in fileinput.input():
        s = s.union(line.lower().split())
    for word in s:
        print(word)
