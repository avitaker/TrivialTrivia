#!/bin/zsh
trap "exit" INT TERM
trap "kill 0" EXIT

if [ "$#" -ne 2 ]; then
    echo "Usage: StressTestApp.sh <number of iterations> <number of background processes>"
    exit -1
fi

NUM_ITERATIONS=$1
NUM_BACKGROUND_PROCESSES=$2

# Run once before to do any necessary compilation once
echo "Pre-compiling tests"
./RunTestSuite.py 1 > /dev/null 2>&1
START_TIME=$(date)
START_SECONDS=$SECONDS

PID_ARRAY=()
for ((i=0;i<$NUM_BACKGROUND_PROCESSES;i++))
do
    ./RunTestSuite.py $NUM_ITERATIONS > /dev/null 2>&1 &
    PID_ARRAY+=($!)
    echo "Started background process $((i+1)) with PID $!"
done

echo "Created ${#PID_ARRAY[@]} background processes"
for PID in "${PID_ARRAY[@]}"
do
    echo "Waiting for background process with ID $PID"
    wait $PID
done

COMPLETED_TIME=$(date)
ELAPSED_SECONDS=$(($SECONDS - $START_SECONDS))

echo "
Finished executing stress test with parameters:
Number of background processes: $NUM_BACKGROUND_PROCESSES
Number of iterations: $NUM_ITERATIONS
Start time: $START_TIME
End time: $COMPLETED_TIME
Elapsed seconds: $ELAPSED_SECONDS"