#!/bin/bash

cd "$(dirname "$0")"
gradle clean build fatjar -xtest
java -jar build/libs/Scraper-CherokeePhoenix.jar

