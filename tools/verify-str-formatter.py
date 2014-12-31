#!/usr/bin/env python3
"""small Tool to verfy all java resrouce string formatter follows following specification
          "%[argument_index$][flags][width][.precision][t]conversion"
   java.util.Formatter reference:  http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
* Author: clint  clintyoung123@gmail.com
* running requies python3 and following packages on Mac
    (which python3) cd /usr/local/bin 
    - easy_install-3.4 BeautifulSoup4
    - easy_install-3.4 requests
* running in terminal with unicode setting
    $ echo $LANG
    en_US.UTF-8

"""

import os
import re
import datetime
from bs4 import BeautifulSoup
#from bs4 import Tag
#from optparse import OptionParser

def main():
    # Get resource path
    resPathDefault="../../../android/managed/app/res"
    #resPathDefault="$PROJECT_HOME/android/managed/app/res"
    if (not os.path.isdir(resPathDefault)):
        print("[!!!] Resource Path not found: %s" % (resPathDefault))
        return
    print("Resource Path: %s" % (resPathDefault))

    # Get all sub-folders start with values
    filecount=0
    errorcount=0
    for localeDir in getSubFolders(resPathDefault):
        if (localeDir.startswith("values")):
            print ("Checking Folder: " + localeDir)
            filePath=os.path.join(resPathDefault,localeDir, "strings.xml")
            if os.path.exists(filePath):
                errorcount+=scanStringFile(filePath)                                 
            filecount += 1          
    print ("----- Summery -----")
    print ("locale file scanned: %d" % filecount)
    print ("errors found: %d" % errorcount)
    
                                  
def getSubFolders(a_dir):
    return [name for name in os.listdir(a_dir)
            if os.path.isdir(os.path.join(a_dir, name))]
                                  
def scanStringFile(filePath):    
    print ("... scanning file: %s" % (filePath))
    
    # Open string.xml file
    xmlDoc = open(filePath,"r")
    soup = BeautifulSoup(xmlDoc)

    # Regular Expression for formatter
    #   see java.util.Formatter reference:  http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
    #   see RE reference: https://docs.python.org/2/library/re.html
    # %[argument_index$][flags][width][.precision][t]conversion
    formatSpecifierJava="%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    formatSpecifierPython="%(\d+\$)?([-#+ 0,(\<]*)?(\d+)?(\.\d+)?([tT])?([a-zA-Z%])";
    
    # Parse strings
    iString=0
    iFormatter=0
    iError=0
    for s in soup.findAll('string'):
        iString += 1
        if (s.string) and ("%" in s.string):
            iFormatter+=1
            n=s.string.count('%')
            placeHolders=re.findall(formatSpecifierPython, s.string)
            if (n!=len(placeHolders)):
                print("[!!!] formatter string warning, please verify manually")
                print(s)
                print(placeHolders)
                iError += 1
    print ("...----- file scan summery -----")
    print ("...strings scanned: %d" % iString)
    print ("...fomatter checked: %d" % iFormatter)
    xmlDoc.close()
    return iError

# ==========================================================
if __name__ == "__main__":
    main()

