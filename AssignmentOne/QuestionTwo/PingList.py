import os, random

checkTable = []
for i in range(65535):
    checkTable.append(0)

prefix = "129.100."
success = 0
for i in range(650):  # try 650 ip to determine
    host = random.randint(0, 65535)  # the number of possible hosts is 65536
    while checkTable[host] == 1:
        host = random.randint(0, 65535)  # check whether the new generated number has been generated before
    checkTable[host] = 1
    ip = prefix + str(host / 256) + "\." + str(host % 256)  # convert number to ip
    response = os.system("ping -c 1 " + ip)
    if response == 0:  # if the response is 0, the ping works
        success += 1

print success
print 65536 * success / 650