#!/bin/bash
#################################################################################################
#                                                                                               #
# Name: Execute batch script;                                                                   #
# Description: Execute batch script daily for update the database that SpaceFlightsApi uses;    #
#                                                                                               #
# Developed by: Leonardo H. Manzini                                                             #
# Role: Mid Software Developer                                                                  #
#                                                                                               #
#################################################################################################

# Script variables
JAVA="java -jar"
JAR_NAME="SpaceFlightsBatch.jar"

# Script command
echo "Starting routine execution"
echo "Running ${JAVA} ${JAR_NAME}"

$JAVA $JAR_NAME

RETURN_CODE=$?

# Finished with errors
if [ $RETURN_CODE -ne 0 ]
then
    echo "Process execution failed"
    echo "Email was sent with error relatory"
    exit
fi

# Finished without errors
echo "Database updated successfully"
echo "Email with execution relatory was sent, exit ${RETURN_CODE}"
