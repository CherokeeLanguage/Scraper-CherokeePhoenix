#!/bin/bash

cd "$(dirname "$0")"
gradle clean build -xtest
java -jar build/libs/Scraper-CherokeePhoenix.jar

