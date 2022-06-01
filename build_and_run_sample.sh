#!/bin/bash

source build.sh

INJECTED_TARGET="injected_""$TARGET"

java -jar "$TARGET" -cplist "$LIB" -pointcuts_json sample/sample.json -i "$TARGET" -o "$INJECTED_TARGET"

echo
ls "$INJECTED_TARGET"

