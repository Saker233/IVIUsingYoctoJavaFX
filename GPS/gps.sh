#!/bin/bash

# Stores the output of gpspipe, which contains JSON-formatted GPS data.
output_file="cgps_output.txt"
# The file where the extracted GPS speed will be written.
speed_file="/home/root/luxsoftBoard/src/luxsoftboard/img/speed.txt"
# The file where the extracted latitude and longitude values will be written.
location_file="/home/root/webPy/location.txt"


setup_gpsd() {
    # gpsd is a service that provides access to GPS data. First, it stops any currently running instances of gpsd and its system socket service, ensuring no previous instances interfere.
    echo "Stopping gpsd and gpsd.socket..."
    sudo systemctl stop gpsd
    sudo systemctl stop gpsd.socket
    # gpsd is a service that provides access to GPS data. First, it stops any currently running instances of gpsd and its system socket service, ensuring no previous instances interfere.
    echo "Starting gpsd with custom UDP address..."
    gpsd -N udp://10.145.17.1:8888 &
    gpsd_pid=$!
    echo "gpsd started with PID $gpsd_pid"
}

run_gpspipe() {
    
    echo "Starting gpspipe..."
    # gpspipe is a tool that continuously outputs GPS data from gpsd to the console or a file. In this case, it writes the data in JSON format (-w) to cgps_output.txt, the file defined in output_file.
    # The output is redirected (2> /dev/null) to suppress error messages.
    gpspipe -w > "$output_file" 2> /dev/null &
    #The process runs in the background, and its process ID is stored in gpspipe_pid.
    gpspipe_pid=$!
    echo "gpspipe started with PID $gpspipe_pid"
}

parse_gpspipe_output() {
    echo "Parsing GPS data..."
    # Checks if output_file exists
    if [ ! -f "$output_file" ]; then
        echo "Error: $output_file does not exist."
        return
    fi
    # Extracts the last line from cgps_output.txt
    last_line=$(tail -n 1 "$output_file")
    # Parses speed, latitude, and longitude using jq
    # jq is a command-line tool for parsing JSON data.
    #Speed: Extracted from the speed field. If it doesn't exist, it defaults to "n/a".
    #Latitude and Longitude: Extracted from the lat and lon fields, respectively. If either field doesn't exist, it defaults to "n/a".
    speed=$(echo "$last_line" | jq -r '.speed // "n/a"')
    lat=$(echo "$last_line" | jq -r '.lat // "n/a"')
    lon=$(echo "$last_line" | jq -r '.lon // "n/a"')
    # The extracted speed is written to the file
    echo "$speed" > "$speed_file"

    # both latitude and longitude are available (lat and lon are not "n/a"), the script writes the values in the format lat lon to file 
    # If the values are not available, the script writes "n/a, n/a" to indicate the absence of valid GPS data.
    if [ "$lat" != "n/a" ] && [ "$lon" != "n/a" ]; then
        echo "$lat $lon" > "$location_file"
    else
        echo "n/a, n/a" > "$location_file"
    fi
}


setup_gpsd
run_gpspipe

trap "kill $gpspipe_pid; kill $gpsd_pid; exit" SIGINT
while true; do
    parse_gpspipe_output
    sleep 5
done
