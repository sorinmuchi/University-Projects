#include "list.h"
void main()
{
	char filename[DEFAULT_STRING_SIZE];
	INPUT: 	printf("Input a file name: ");
	gets(filename);
	FILE *fp=fopen(filename,"r");
	if (fp == NULL)
		goto INPUT;
	struct NODE* head=(struct NODE*)malloc(sizeof(struct NODE));
	head->next=NULL;
	while (!feof(fp))
	{
		char c=fgetc(fp);
		if (c != '\n')
		add(head,c-'0');
	}
	fclose(fp);
	prettyPrint(head);
	int toDelete;
	printf("Please input an integer value");
	scanf("%d",&toDelete);
}
