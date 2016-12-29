#!/bin/bash
echo "Press [CTRL+C] to stop.."
while true
do
	rm -f /tmp/f; mkfifo /tmp/f
    echo "Listening"
	cat /tmp/f | mvn exec:java -Dexec.mainClass="main.Main" 2>&1 | nc -l 127.0.0.1 18756 > /tmp/f
    echo "One client handled"
    sleep 1
done
