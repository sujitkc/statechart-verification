#!/bin/bash

for f in c*.tex; do
    [ -e "$f" ] || continue
    xelatex "$f"
done


