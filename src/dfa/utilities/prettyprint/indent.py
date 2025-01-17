#!/usr/bin/python3

def indent(filename):
    with open(filename, 'r') as fin:
        lines = fin.readlines()
        level = 0
        indentation = ''
        for line in lines:
            if(line.strip() == ''):
                continue
            if('{' in line):
                print(indentation + line.strip())
                level += 1
                indentation = '    ' * level
            elif('}' in line):
                level -= 1
                indentation = '    ' * level
                print(indentation + line.strip())
            else:
                print(indentation + line.strip())

if __name__ == "__main__":
    indent('../../data/unindented-sc.txt')
