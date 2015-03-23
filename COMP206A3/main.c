#include "list.h"
struct NODE	*head;
void main()
{
	char filename[DEFAULT_STRING_SIZE];
	INPUT: 	printf("Input a file name: ");
	gets(filename); //We get the filename
	FILE *fp=fopen(filename,"r");
	if (fp == NULL) //File does not exist, repeat
		goto INPUT;
	head=(struct NODE*)malloc(sizeof(struct NODE)); //Allocate 1 NODE to head
	head->next=NULL; //Head=tail
	while (!feof(fp))
	{
		char c=fgetc(fp); //Read a char
		if (c != '\n') //Skip the newline characters
		add(c-'0'); //Convert to char right away, then add to the LL
	}
	fclose(fp); //We always close the file
	prettyPrint(); //Print the LL
	int toDelete;
	readBack: printf("Please input an integer value");
	scanf("%d",&toDelete);
	if (delete(toDelete)) //If it was deleted
		printf("Number was deleted\n");
	else //Was not found or bad pointer
		printf("Number was not found!\n");
	prettyPrint();
	printf("Do it again [y/n] ?\n");
	char input[DEFAULT_STRING_SIZE];
	scanf("%s",input); //Take care of all the inputs
	if ((input[0]=='Y' && input[1]=='\0') || (input[0] =='y' && input[1]=='\0') || (input[0]=='Y' && input[1]=='E' && input[2]=='S' && input[3]=='\0') || (input[0]=='y' && input[1]=='e' && input[2]=='s' && input[3]=='\0') || (input[0]=='Y' && input[1]=='e' && input[2]=='s' && input[3]=='\0'))
	{
		goto readBack; //If Y/y/Yes/YES/yes , we repeat
	}
}
