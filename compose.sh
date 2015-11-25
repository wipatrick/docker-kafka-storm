#!/bin/bash

case "$1" in
        start-foreground)
            docker-compose up
            ;;

        start-background)
            docker-compose up -d
            ;;

        stop)
            docker-compose stop
            ;;

        *)
            echo $"Usage: $0 {start-foreground|start-background|stop}"
            exit 1
esac
