import subprocess

subprocess.call("gradle clean", shell=True)
subprocess.call("gradle build -x lint", shell=True)
