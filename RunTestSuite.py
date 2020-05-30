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


def runAndroidTest():
    subprocess.call("gradle clean", shell=True)
    subprocess.call("gradle build -x lint", shell=True)
    cmdExe = "gradlew test"
    process = subprocess.Popen(cmdExe, stdout=subprocess.PIPE, shell=True)
    output = process.communicate()[0]
    output = str(output)
    return output

def createEmail(testResults, resultFormat, fails ):
    version = random.randint(0, 500)
    email = "Subject: Trivial Trivia Test Summary - " + testResults  + str(version) + " \n"
    email += resultFormat 
    email += fails
    return email


def formatLoopOutput(loopNum, outputOrig, verbose):
    countPass = 0
    countFail = 0
    output = outputOrig.replace('\\n', '\n')

    allP = re.findall("PASSED", output)
    for p in allP:
        countPass+=1
    allFails = re.findall("com.avinashdavid.trivialtrivia.*FAILED", output)
    fails = set(allFails)
    for f in allFails:
        countFail+=1

    result = "  UNIT TESTS: Passed = " + str(countPass) + " Failed = " + str(countFail) + " - Loop " + str(loopNum) + "\n"
    if verbose:
        print(output)
        summary = ""
        allS = re.findall("RUNNING.*result: SUCCESS", output)
        for s in allS:
            summary += s + "\n"
        return summary +  result
    return (result, fails)




if __name__ == "__main__":
    """
    Can take up to 2 Command Line Arguments,  the number of times to run the entire
    suite of tests from android and the email the report should be sent to.
    """

