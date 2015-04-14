#!/usr/bin/python
import cgitb,cgi,base64
cgitb.enable()

form = cgi.FieldStorage()
print "Content-Type: text/html \n"
username=form["username"].value
username=base64.b64decode(username)
username=cgi.escape(username,quote=True)
formId=form["id"].value
if (formId is '0'):
    print "Logged in\n"
if (formId is '1'):
    print "Added friend\n"
if (formId is '2'):
    print "Posted news\n"
print "Username: "+username