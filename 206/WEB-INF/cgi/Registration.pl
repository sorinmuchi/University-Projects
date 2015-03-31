#!D:/Lib/Perl/bin/perl.exe
use CGI;
use File::stat;
use strict;
use warnings;

print "Content-type: text/html\n\n";
print "<head>\n";
my $url="http://google.com";
##Redirect##
#print "<meta http-equiv=\"refresh\" content=\"0; url=$url\" />\n";
print "</head>\n";
my $query = new CGI;
my $username = $query->param("username");
my $name = $query->param("name");
my $password = $query->param("password");
my $members = 'members.csv';
my $filesize = stat($members)->size;
open my $fh, '>', $members;
close($fh);
open(my $fh, ">>", $members);
#if we have an empty file, write right away
if ($filesize == 0)
{
	print $fh "$name $username $password\n";
}
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
if ( $username ~~ @usernames)
{print "There is a duplicate!\n";}
close($fh);
1;