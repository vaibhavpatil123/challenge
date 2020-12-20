#!/bin/sh

heap_min_size=512m
heap_max_size=512m
gc_strategy=+UseG1GC

java \
  -XX:${gc_strategy} \
  -Xms${heap_min_size} \
  -XX:MetaspaceSize=64m \
  -Xmx${heap_max_size} \
  -Xss512k \
  -Djava.security.egd=file:/dev/./urandom \
  -jar \
  /app/app.jar
