#!/bin/bash

# Easily updates the versions in the readme file after a release

if [ $# -ne 1 ]; then
    echo "Usage: $0 <new_version>"
    echo "Example: $0 1.0.7"
    exit 1
fi

NEW_VERSION=$1
FILE="README.md"

if [ ! -f "$FILE" ]; then
    echo "Error: $FILE not found in current directory"
    exit 1
fi

# Update Maven dependency version
sed -i -E "s/<version>[0-9]+\.[0-9]+\.[0-9]+<\/version>/<version>$NEW_VERSION<\/version>/g" $FILE

# Update Gradle implementation version
sed -i -E "s/version: '[0-9]+\.[0-9]+\.[0-9]+'/version: '$NEW_VERSION'/g" $FILE

# Update javadoc badge URL - ensure both badge and target use the same version
sed -i -E "s|/location4j/[0-9]+\.[0-9]+\.[0-9]+/javadoc\.svg|/location4j/$NEW_VERSION/javadoc.svg|g" $FILE
sed -i -E "s|javadoc\.io/doc/com\.tomaytotomato/location4j/[0-9]+\.[0-9]+\.[0-9]+\)|javadoc.io/doc/com.tomaytotomato/location4j/$NEW_VERSION)|g" $FILE

echo "Updated version references in $FILE to $NEW_VERSION:"