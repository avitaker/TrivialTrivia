import subprocess, smtplib, ssl, sys, time
from email.message import EmailMessage

if(len(sys.argv) == 3):
    numLoop = int(sys.argv[1])
    receiver_email = sys.argv[2]
    sendEmail = True
elif(len(sys.argv) == 2):
    numLoop = int(sys.argv[1])
    sendEmail = False
else:
    numLoop = 1
    sendEmail = False

for i in range(numLoop):
    timeStart = time.time()
    # Command to execute
    cmdExe = "gradlew test"

    process = subprocess.Popen(cmdExe, stdout=subprocess.PIPE, shell=True)
    output = process.communicate()[0]

    output = str(output)

    formatted_output = output.replace('\\n', '\n')
    formatted_output = formatted_output[2:-1]
    timeEnd = time.time()

    elapsedTime = timeEnd - timeStart

    if "BUILD SUCCESSFUL" not in str(output):
        testResults = "\033[1;31;40m FAILED \x1b[0m"
        subject = "Subject: Test Failure\n"
    else:
        testResults = "\033[1;32;40m PASSED \x1b[0m"
        subject = "Subject: Test Pass\n"

    print("Loop " + str(i) + " Test: " + testResults +" Elapsed Time: " + str(round(elapsedTime, 3)) + "s")

if sendEmail:

    # Allows for emails to be sent
    port = 465  # For SSL
    smtp_server = "smtp.gmail.com"
    sender_email = "chesstestingteam@gmail.com"
    password = "ThisIsNotASecurePassword"

    email = subject + formatted_output

    context = ssl.create_default_context()
    with smtplib.SMTP_SSL(smtp_server, port, context=context) as server:
        server.login(sender_email, password)
        server.sendmail(sender_email, receiver_email, email)
        server.quit()
