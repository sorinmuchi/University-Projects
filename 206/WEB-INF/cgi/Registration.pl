#!D:/Lib/Perl/bin/perl.exe
use CGI;
use File::stat;
use strict;
use warnings;

print "Content-type: text/html\n\n";
print "<head>\n";
##Redirect##
print "</head>\n";
my $query = new CGI;
my $username = $query->param("username");
my $name = $query->param("name");
my $password = $query->param("password");
my $members = 'members.csv';
my $filesize = stat($members)->size;
open(my $fh, ">>", $members);
#if we have an empty file, write right away
if ($filesize == 0)
{
	print $fh "$name $username $password\n";
}
close($fh);

	open(my $fh, "<", $members);
	my ($username_dup,$residual);
	my @usernames;
	#read the rows, pushing usernames (2nd column) into stack
	while (my $row = <$fh>) {
	  chomp $row;
	  #split 2 times to get the 2nd (username) of each row
	 ($username_dup,$residual) = split /\s* \s*/, $row, 2;
	 ($username_dup,$residual) = split /\s* \s*/, $residual, 2;
	  #push to stack of usernames
	  push @usernames,$username_dup;
	}
	#look through the stack, if we have the username, report error
	if ( $username ~~ @usernames) #Access refused, error page
	{
		my $code=<<"END";
<html>
<head>
<link rel="shortcut icon" href="../img/icons/favicon.png">
<title>Welcome to NSAbook - Login</title>
</head>
<body bgcolor="#090E50">
<img src="../img/welcome_banner.png" width=100%>
<table width=100% cellspacing="0" cellpadding="0" border="0" background="../img/welcome_bg.png">
<tr> <td height="200" align=center>
<img src="../img/aboutUs.png">
</td>
</tr>
<tr>
<td align=center>
<img src="../img/aboutUs_detail.png">
</td>
</tr>
	<tr><td align=center><br><br>
	<img src="../img/loginBanner.png">
		<form action="Login.c">
		<br>
		<font color="red" size+=4>Login Error</font><br>
		<img src="../img/username.png">
		<input type="text" name="username">
		<br>
		<img src="../img/password.png">
		<input type="password" name="password">
		<br><br>
		<input type="submit" value="Login">
		</form>
		</td>
	</tr>
<tr> <td height="200" align=center><br><br><br>
<a href="../registration.html"><img src="../img/joinUs.png"></a>
</td><br>
</tr>
</table>
</body>
</html>
END
print "$code\n";
	}
	else #Access granted, log creds + goto login
	{
		open(my $fh, ">>", $members);
		print $fh "$name $username $password\n";
		my $url="../index.html";
		print "<meta http-equiv=\"refresh\" content=\"0; url=$url\" />\n";
	}

1;