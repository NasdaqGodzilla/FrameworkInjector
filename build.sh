#!/bin/bash

OUT="out""$RANDOM"
LIB="libs"
MANIFEST="MANIFEST.MF"
TARGET="frameworkinjector"_"$OUT"".jar"

function setup() {
    rm -rf "$OUT"
    mkdir "$OUT"
}

function clean() {
    rm -rf "$OUT"
}

function build_impl() {
    javac -cp "$LIB""/*" -d "$OUT"  src/frameworkinjector/*
    jar cvfm "$TARGET" "$MANIFEST" -C "$OUT" peacemaker/frameworkinjector/
}

setup
build_impl
clean

echo
ls "$TARGET"
