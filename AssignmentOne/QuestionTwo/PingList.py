import os, random

checkTable = []
for i in range(65535):
    checkTable.append(0)

prefix = "129.100."
success=0
for i in range(6500):
    host = random.randint(0, 65535)
    while checkTable[host] == 1:
        host = random.randint(0, 65535)
    checkTable[host]=1
    ip=prefix+str(host/256)+"\."+str(host%256)
    response = os.system("ping -c 1 "+ip)
    if response==0:
        success+=1

print 65536*success/6500;