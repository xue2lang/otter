#!/bin/bash

#mvn clean package -Dmaven.test.skip -Denv=release
mvn clean
mvn package -Dmaven.test.skip