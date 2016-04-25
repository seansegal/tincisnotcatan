#!/bin/bash
echo "Building Catan!"
# Set up any local, temporary things.
# run maven package
mvn package
echo "Cleaning up!"
# Remove any local, temporary things.
