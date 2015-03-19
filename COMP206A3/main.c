#include "list.h"
struct NODE	*head;
void main()
{
	char filename[DEFAULT_STRING_SIZE];
	INPUT: 	printf("Input a file name: ");
	gets(filename);
	FILE *fp=fopen(filename,"r");
	if (fp == NULL)
		goto INPUT;
	head=(struct NODE*)malloc(sizeof(struct NODE));
	head->next=NULL;
	while (!feof(fp))
	{
		char c=fgetc(fp);
		if (c != '\n')
		add(c-'0');
	}
	fclose(fp);
	prettyPrint();
	int toDelete;
	readBack: printf("Please input an integer value");
	scanf("%d",&toDelete);
	if (delete(toDelete))
		printf("Number was deleted\n");
	else
		printf("Number was not found!\n");
	prettyPrint();
	printf("Do it again [y/n] ?\n");
	char input[DEFAULT_STRING_SIZE];
	scanf("%s",input);
	if ((input[0]=='Y' && input[1]=='\0') || (input[0] =='y' && input[1]=='\0') || (input[0]=='Y' && input[1]=='E' && input[2]=='S' && input[3]=='\0') || (input[0]=='y' && input[1]=='e' && input[2]=='s' && input[3]=='\0') || (input[0]=='Y' && input[1]=='e' && input[2]=='s' && input[3]=='\0'))
	{
		goto readBack;
	}
}
