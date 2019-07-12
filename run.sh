#!/bin/bash

cd "$(dirname "$0")"
./gradlew clean build fatjar -xtest
java -jar build/libs/Scraper-CherokeePhoenix.jar

