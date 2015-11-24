#!/bin/bash
# Bash wrappers for docker run commands

# Helper Functions
docker rm -v $(docker ps --filter status=exited -q 2>/dev/null) 2</dev/null
docker rmi $(docker images --filter dangling=true -q 2>/dev/null) 2>/dev/null
