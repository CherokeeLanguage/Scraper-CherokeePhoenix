#!/bin/bash

cd "$(dirname "$0")"
./gradlew clean build shadowJar -xtest
java -jar build/libs/Scraper-CherokeePhoenix.jar

