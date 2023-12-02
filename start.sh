#!/bin/bash

# Ensure, that docker-compose stopped
docker-compose down -v

# Start new deployment
docker-compose up --build -d

# For not closing console
$SHELL