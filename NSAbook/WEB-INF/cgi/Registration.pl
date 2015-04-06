#!/usr/bin/perl
use CGI;
use lib 'mod';
#use Digest::SHA::PurePerl qw(sha1 sha1_hex); Deprecated since easily crackable
#use Crypt::Blowfish::Blowfish_pure;
use File::stat;
use MIME::Base64;
use constant block => 8;

print "Content-type: text/html\n\n";
print "<head>\n";
##Redirect##
print "</head>\n";
my $query = new CGI;
my $username = encode_base64($query->param("username"),'');
my $name = encode_base64($query->param("name"),'');
my $password=$query->param("password");
system("./crypto $password");
open($fh, "<", "key.dat");
my $row = <$fh>;
chomp $row;
$password=$row;
$password=encode_base64($password);
my $members = 'members.csv';
my $filesize = stat($members)->size;
#if we have an empty file, write right away
if ($filesize == 0)
{
	goto LOGIN;
}
	open($fh, "<", $members);
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
<meta charset="UTF-8">
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
		<form action="Login.cgi" method="get">
		<br>
		<font color="red" size+=6>Registration Error - Username taken (exception code 0xDEADBEEF)</font><br>
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
		LOGIN:
		open(my $fh, ">>", $members);
		print $fh "$name $username $password";
		my $url="../index.html";
		print "<meta http-equiv=\"refresh\" content=\"0; url=$url\" />\n";
	}
#######ENCRYPTION#########
###PROTOTYPE ENCRYPTION###
=head
sub encode() ##We cannot feed the string right away , blowfish accepts 8-bit blocks. we break it and /or pad with 0s
{
	my $password=$_[0];
	my $key='F1B4AD74CF9B9728667C56EE9EC46'; #should be generated, but we don't bother with this
	my $blowfish= Crypt::Blowfish::Blowfish_pure->new($key); #create a ciphering obj
	my $output;
	my $byteLength=getByteSize($password);
	if ($byteLength < block)
		{
			$password=padd($password);
			$output=$blowfish->encrypt($password);
		}
	else
		{
			while (length($rest) >= 0)
			{
				my $substr=substr($password,0,block);
				my $rest=substr($password,-(length($password)-block));
				$output .=$blowfish->encrypt($substr); #Encrypt with length/8-byte block iterations of blowfish+1 iteration of base_64 but no salt
				$output .= encode($rest);
			}
		}
	return $output;
}
sub padd()
{
	my $data=$_[0];
	my $residual=block-getByteSize($data);
	while ($residual > 0)
	{
		$data.='\0';
	}
	return $data;
}
sub getByteSize()
{
	my $data=$_[0];
	my $byteLength;
	use bytes;
		$byteLength=length($data); #each char uses 4 bits
	no bytes;
	return $byteLength;
}
=cut
1;