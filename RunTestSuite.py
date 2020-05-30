import subprocess, smtplib, ssl, sys, time
from email.message import EmailMessage
import re, config
import random


"""
 RunTestSuite runs the unit tests in android studio a certain 
 number of times and emails the results using gradle commands. 

 Example of running this script:
 $ python RunTestSuite.py 2 clipsong@gmail.com
"""

def generateSummary(testResults, timeStart, timeEnd, i ):
    elapsedTime = timeEnd - timeStart
    outcome = "\nLoop " + str(i) + ": " + testResults +" Elapsed Time: " + str(round(elapsedTime, 3)) + "s\n"
    return outcome


def getTestResults(output):
    if "BUILD SUCCESSFUL" not in str(output):
        testResults = "\033[1;31;40m FAILED \x1b[0m"
        result = "Some Test Failure "
    else:
        testResults = "\033[1;32;40m PASSED \x1b[0m"
        result = "All Tests Pass "
    return result





if __name__ == "__main__":
    """
    Can take up to 2 Command Line Arguments,  the number of times to run the entire
    suite of tests from android and the email the report should be sent to.
    """

