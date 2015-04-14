#include "Login.h"
#define ARRAY_SIZE(a) (sizeof(a)/sizeof(a[0]))
size_t getSize(char* input)
{
	size_t i=0;
	while (input[i] != '\0')
		i++;
	return i;
}
int main( int argc, char *argv[] )
{
  char *CGIpass=(char*)malloc(getSize(argv[1]) * sizeof(char)); 
  strcpy(CGIpass,argv[1]);
  char *RC4_CGIpass=(char*)malloc(getSize(CGIpass) * sizeof(char)); //Pass it throu an ARC4 round
  strcpy(RC4_CGIpass,rc4_e(CGIpass,sizeof(CGIpass)));
  FILE *fp=fopen("key.dat","w"); //open the database
  if (fp == NULL)
  {
	printf("File not found\n,Exiting...\n");
	exit(1);
  }
 fputs(RC4_CGIpass,fp);
 fclose(fp);
 return 0;
}