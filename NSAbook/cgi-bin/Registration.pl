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
### DDOS / Bot protection ###
my $timestamp=time();
open (my $tsfh,"<","ts.dat") or die "No file found";
my $last=<$tsfh>;
chomp($last);
close($tsfh);
if (abs($timestamp-$last) <60) # If last registration was less than 1 minute away, we reload and try later
{
	goto ERROR;
}
else	# Is fine, update the last timestamp and grant access to login script
{
	open (my $tsfh, ">","ts.dat");
	print $tsfh $timestamp."\n";
	close($tsfh);
}
my $query = new CGI;
### Log IP to trace l33t hax0rs ###
open (my $ipfh,">>","ip.dat");
print $ipfh $query->remote_host();
close($ipfh);
### End Log IP ###
### Encode everything B64 and rc4 (check the C crypto function :D ) ###
my $usernameb=$query->param("username");
my $username = encode_base64($usernameb,'');
my $nameb=$query->param("name");
my $name = encode_base64($nameb,'');
my $passwordb=$query->param("password");
my $password=$query->param("password");
### Chomp new lines ###
chomp($username);
chomp($name);
chomp($password);
# UNCOMMENT EVERYTHHING UNTIL '=cut' AND IN THE C PROGRAM IF YOU WANT TO TRY RC4
=head
system("./crypto $password"); # Encrypt the password by passing a syscall to our C script
open($fh, "<", "key.dat");
my $row = <$fh>;
chomp $row;
$password=$row;
=cut
$password=encode_base64($password);
my $members = 'members.csv';
my $filesize = stat($members)->size;
#if we have an empty file, write right away
if ($filesize == 0)
{
	goto LOGIN;
}
# If the inputs are out of bounds i.e. not it the interval I=[1,15]
if (length($passwordb) > 15 || length($passwordb) <2 || length($usernameb) > 15 || length($usernameb) <2 || length(passwordb) >15 || length($passwordb) <2)
{
	goto ERROR;
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
	ERROR:
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
		<form action="Login.cgi" method="post">
		<br>
		<font color="red" size+=6>Registration Error - Username taken or DDOS prevention triggered (exception code 0xDEADBEEF)</font><br>
		<img src="../img/username.png">
		<input type="text" pattern="[A-Za-z0-9]*" title="A-Z0-9 pliz" maxlength=15 name="username">
		<br>
		<img src="../img/password.png">
		<input type="password" pattern="[A-Za-z0-9]*" title="A-Z0-9 pliz" maxlength=15 name="password">
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
print "$code\n"; # Print the above string until EOF as a common error page (either bad params or ddos protection)
	}
	else #Access granted, log creds + goto login
	{
		LOGIN:
		open(my $fh, ">>", $members);
		print $fh "$name $username $password";
		my $url="../index.html";
		print "<meta http-equiv=\"refresh\" content=\"0; url=$url\" />\n";
	}
####### DEPRECATED BLOWFISH ENCRYPTION - DO NOT GRADE ########
####### HERE FOR EDUCATIONNAL PURPOSES ONLY	      ########
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