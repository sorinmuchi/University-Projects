#!/usr/bin/python

import cgi, cgitb, csv, base64, re,copy
cgitb.enable()

####################### Gets encrypted username and decrypts it #######################    

form = cgi.FieldStorage()			
username = form["username"].value	
username=cgi.escape(username,quote=True)

####################################################################################### 
 

print "Content-type:text/html\r\n\r\n"
print '<html>'
print '<head>'
print '<link rel="shortcut icon" href="../img/icons/favicon.png">'
print '</link>'
print '<title>NSAbook - Topics Page </title>'
print '</head>'
print '<body bgcolor="090E50">'
print '<table width="100%" cellspacing="0" cellpadding="0" border="0" background="../img/welcome_bg.png" >'
print '<tr>'
print '<td align="right" height="20">'

####################### Logout and redirect to login page #######################

print '<a href="../index.html">'
print '<button>Logout</button>'
print '</a>'

#################################################################################
####################### Gets the user's real name ####################### 
name=""   
membersRead = csv.reader(open("members.csv", "r"))
for row in membersRead:
	list = row[0].split()
	if (username == list[1]):
		name = list[0]
		name = base64.b64decode(name)
		name = cgi.escape(name,quote=True)
if not name:
	print '<meta http-equiv="refresh" content="0; url=../index.html">'
######################################################################### 

print '</td>'
print '</tr>'

####################### Print the username in large text #######################

print '<tr><td><center><hr><img src="../img/welcomeHax.png">,<font color=white size=8> ' , name,'</H1></font></hr></center></td></tr>'
print '<form><input type="hidden" id="0" name="username" value="%s"></input></form>' %username 

################################################################################

print '<tr>'
print '<td align="center" height="200">'
print '<form action="MyFacebookPage.py" method="get" id="1">'
print '<p> <img src="../img/updatePrompt.png"></p>'
print '<textarea id="1" name="update" cols="80" rows="5"></textarea>'
print '<br></br>'
print '<a href="../index.html">'
print '<input type="submit" value="Submit" align ="left"></input>'
print '</a>'
print '<input type="hidden" id="1" name="username" value="%s"></input>' %username
print '</form>'
print '</td>'
print '</tr>'

####################### Create a new status update #######################               

if (form.has_key("update")):
	status = form["update"].value
	status=cgi.escape(status,quote=True)
	status = base64.b64encode(status)
	membersRead = csv.reader(open("members.csv", "r"))
	for row in membersRead:
		list = row[0].split()
		if (username == list[1]):
			name = list[0]
	topicsWrite = open("topics.csv", "a")
	topicsWrite.write(name)
	topicsWrite.write(",")
	topicsWrite.write(status)
	topicsWrite.write("\n")
	topicsWrite.close()
	print '<meta http-equiv="refresh" content="0; url=MyFacebookPage.py?username=%s">' %username 

##########################################################################

print '<tr>'
print '<td align="center" height="500">'
print '<img src="../img/status.png"><br>'
####################### Display friends' status updates ####################### 
friendsRead = csv.reader(open("members.csv", "r"))
friends = []
for row in friendsRead:
	list = row[0].split()
	if (list[1] == username):
		friends.extend(list)
		del friends[2]
		del friends[1]
content =[]
with open("topics.csv", "r") as f:
	content=f.readlines()
f.close()
while len(content) > 0:
	row3=content.pop()
	row=row3.split(',')
	for names in friends:
		if (names == row[0]):
			name = row[0]
			name = base64.b64decode(name)
			status = row[1]
			status = base64.b64decode(status)
			print '<font color="white" size=4>', name, ' says <br />"', status, '"<br /><br /></font>'	
#stack = []
#with open('topics.csv', 'r') as in_file:
#	for line in in_file:
#		stack.append(line)
#with open('out.csv', 'w') as out_file:
#	while len(stack) >0:
#		out_file.write(stack.pop())
#outRead = csv.reader(open("out.csv", "r"))
#for row in outRead:
#	for names in friends:
#		nextRow = outRead.next()[0]
#		if (names == nextRow):
#			name = outRead.next()[0]
#			name = base64.b64decode(name)
#			status = row[0]
#			status = base64.b64decode(status)
#			print '<font color="white">', name, ':<br />"', status, '"<br /><br /></font>'	

###############################################################################

print '</td>'
print '</tr>'
print '<tr>'
print '<td align="center" height="80">' 
print '<form action="MyFacebookPage.py" method="get" id="2">'
print '<img src="../img/sniff.png"><br>'
print '<input type="text" name="user"></input>'
print '<input type="submit" value="Follow"></input>'
print '<input type="hidden" id="0" name="username" value="%s"></input>' %username
print '</form>'
print '</td>'
print '</tr>'

####################### Follow a user #######################
if (form.has_key("user")):
	friend = form["user"].value
	friend=cgi.escape(friend,quote=True)
	friend = base64.b64encode(friend)
	with open("members.csv", "r") as f:
		content=f.readlines()
	f.close()
	hasFriend=0
	for mname in content:
		namez=mname.split(' ')
		if (namez[1] == friend):
			hasFriend=1
	if (hasFriend == 0):
		friend=""
	membersRead = csv.reader(open("members.csv", "r"))
	for row in membersRead:
		list = row[0].split()
		user = list[1]
		if (user == username):	
			old = row[0]
			if friend not in old:
				new = old + " " + friend
			else:
				new = old
			tempWrite = open("temp.csv", "w")
			membersRead = (open("members.csv", "r"))
			csvRead = csv.reader(membersRead)
			for row in membersRead:
				row = row.rstrip('\n')
				if (row == old):
					tempWrite.write(new)
					tempWrite.write("\n")
				else:
					tempWrite.write(row)
					tempWrite.write("\n")
			membersRead.close()
			tempWrite.close()
	
	membersWrite = open("members.csv", "w")
	tempRead = csv.reader(open("temp.csv", "r"))
	for row in tempRead:
		membersWrite.write(row[0])
		membersWrite.write("\n")
	print '<meta http-equiv="refresh" content="0; url=MyFacebookPage.py?username=%s">' %username 
	
#############################################################

print '<tr>'
print '<td align="center" height="180">' 
print '<img src="../img/botnet.png"><br>'		

####################### List all registered users #######################

membersRead = csv.reader(open("members.csv", "r"))		
members = []
for row in membersRead:
	list = row[0].split()
	firstName = list[0]
	firstName = base64.b64decode(firstName)
	members.append(firstName)
print '<font color="white">', str(members).strip(), '</font>'

#########################################################################

print '</td>'
print '</tr>'
print '</table>'
print '</body>'
print '</html>'

