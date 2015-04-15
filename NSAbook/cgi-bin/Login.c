#include "Login.h"
int main() 
{
 printf( "Content-Type: text/html\n\n" );
 //Reading post request from STDIN
 char *CGIargs = (char *)malloc(SIZE * sizeof(char));
 char *lenstr;
char input[SIZE], data[SIZE];
long len;
lenstr = getenv("CONTENT_LENGTH"); //Get the length
if(lenstr == NULL || sscanf(lenstr,"%ld",&len)!=1 || len > SIZE)
{
  printf("<P>Error ");
printf("<meta http-equiv=\"refresh\" content=\"0; url=../index.html\" />\n");
}
else {
	fgets(input, len+1, stdin); // put the stdin into input
  }
 strcpy(CGIargs,input); //rename
 if (CGIargs == NULL)
 {
	printf("Please use the form to access the site.\n Exiting...\n");
	printf("<meta http-equiv=\"refresh\" content=\"0; url=../index.html\" />\n");
 }
 char* CGIuser=(char*)malloc(SIZE * sizeof(char));
 char* CGIpass=(char*)malloc(SIZE * sizeof(char));
 //username=user&password=pass template
 if (sscanf(CGIargs, "username=%[0-9a-zA-Z]&password=%[0-9a-zA-Z]", CGIuser, CGIpass) != 2)
 {
	// If we do not get 2 parameters that are successfully assigned, return back
	printf("<meta http-equiv=\"refresh\" content=\"0; url=../index.html\" />\n");
 }
 if (getSize(CGIuser) >= 16 || getSize(CGIpass) >=16)
 {
		// If we start having too long input
		printf("Please use the form to access the site.\n Exiting...\n");
		printf("<meta http-equiv=\"refresh\" content=\"0; url=../index.html\" />\n");
 }
 FILE* fp=mopen();
 ///////////REAL HAX///////////
 char *RC4_CGIpass=(char*)malloc(getSize(CGIpass) * sizeof(char)); 
 strcpy(RC4_CGIpass,CGIpass);//rc4_e(CGIpass,sizeof(CGIpass))); //Encryption works fine EXCEPT when we have a 6 char string, so omit it
 char * CGI_ENC_U=(char *)malloc(getSize(CGIuser) * sizeof(char)); //We want the chars[], not the pointer 
 strcpy(CGI_ENC_U,B64E(CGIuser));	//Thus we copy the return into another mem address
 char * CGI_ENC_P=(char *)malloc(SIZE * sizeof(char));
 strcpy(CGI_ENC_P,B64E(RC4_CGIpass));
 int pass=getColumn(fp,2,CGI_ENC_P);
 fclose(fp);
 FILE* fp2=mopen();
 int user=getColumn(fp2,1,CGI_ENC_U);
 fclose(fp2);
  ////////TEST IF EQUAL////////
  if ( user == pass & user!=-1 && pass !=-1 ) //If both return the same line, and find something (i.e. not -1)
  {
/*We have this:
<form action="Feed.py" method="post">
<input type=�hidden� name=�username� value=�CGIuser�>
<input type="submit" value="Login">
</form>
*/	printf("<meta http-equiv=\"refresh\" content=\"0; url=MyFacebookPage.py?id=0&username=%s\" />\n",CGI_ENC_U);
  }
  else {
 //Either not found, or on different lines. In this case, make them try again
		//printf("<script>alert(\"%s\")</script>\n",CGI_ENC_P); For debugging
		printf("<meta http-equiv=\"refresh\" content=\"0; url=../index.html\" />\n");
  }
  return 0;
}

//////////////////
//Helper methods//
//////////////////
////GET REAL STRING SIZE////
size_t getSize(char* input)
{
	size_t i=0;
	while (input[i] != '\0')
		i++;
	return i;
}
////GET LINE COUNT OF  A FILE////
int getLineCount(FILE* fp)
{
	int count=0; char c;
	while (c!= EOF) 
	{
		c =fgetc(fp);
		if( c== '\n')
			count++;
	}
	if(c != '\n' && count != 0) 
		count++;
	if (count < 0) return (int)NULL;
	return (int)(count-1); //Take care of the newline
}
/////GET A GIVEN COLUMN FROM THE MEMBRES.CSV/////
 int getColumn(FILE* fp,int column,char *operand)
{
  int flag=-1;
  char *line=(char*)malloc(SIZE * sizeof(char)); //Give it 1000 chars
  int lineCount=0;	  
  while ( fgets(line, SIZE * sizeof(char), fp))
  { 
	strtok(line, "\n");
    if (strcmp(line, "\n") == 0)
	    continue;
    char tokens[SIZE];
	strcpy(tokens,strtok(line," "));
	int i=0;
	while(tokens != NULL && i < column ) //Tokenize the row
	{
      strcpy(tokens,strtok(NULL, " "));
	  i++;
	}
	if (strcmp(tokens,operand) == 0)//we found a match
	flag=lineCount;
	lineCount++;
  }
  return flag;
}
////CHECK IF THE BUFFER OVERFLOWS////
BOOLEAN overflows(char *data)
{
	size_t i=0;
	while (data[i] != '\0')
	{
		if (data[i] == '\n')
		{
			return FALSE;
		}
		i++;
	}
	return TRUE;
}
///////ENCODE///////
char * B64E(char *block)
{
 size_t block_size=getSize(block);
 char block_enc[SIZE];
 size_t block_enc_size=sizeof(block_enc);
  if( base64encode((void*)block, block_size, block_enc,block_enc_size )) //if there was an encoding error
  {
	printf("Unable to encode\n Exiting... \n");
	exit(1);
  }
  return block_enc;
 }
 /////OPEN FILE/////
 FILE* mopen()
 {
	  FILE *fp=fopen("members.csv","r"); //open the database
  if (fp == NULL)
  {
	printf("Database not found\n,Exiting...\n");
	exit(1);
  }
  return fp;
 }