#include "list.h"
 //Add a node to the end of the LL
void add(int value)
{
	struct NODE* iterator;
	struct NODE* node;
	iterator=head;
	while (iterator->next != NULL)
	{
		iterator=iterator->next;
	}
	node=(struct NODE*)malloc(sizeof(struct NODE));
	node->value=value;
	node->next =NULL;
	iterator->next=node;
}
void prettyPrint()
{
	struct NODE* iterator=head->next;
	while (iterator->next != NULL)
	{
		if (iterator->next->next == NULL)
			printf("%d",iterator->value);
		else 
			printf("%d -> ",iterator->value);
		iterator=iterator->next;
	}
	printf("\n");
}
struct NODE* find(int value)
{
	struct NODE* iterator;
	iterator=head;
	while (iterator->next != NULL)
	{
		if (iterator->value == value)
			return iterator;
		iterator=iterator->next;
	}
	return NULL;
}
BOOLEAN delete(int value)
{
	//Create an iterator
	struct NODE* iterator;
	iterator=head;
	//Find the first occurrence of the value
	struct NODE *node=find(value);
	//Abort if not found
	if (node == NULL)
		return FALSE;
	//If last node, then only rewrite the previous node
	if (node->next == NULL)
	{
		while (iterator->next != NULL)
		{
			if (iterator->next == node)
			{
				iterator->next=NULL;
				return TRUE;
			}
			iterator=iterator->next;
		}
		return TRUE;
	}
	else 
	{
		while (iterator->next != NULL)
		{
			if (iterator->next == node)
			{
				iterator->next=iterator->next->next;
				return TRUE;
			}
			iterator=iterator->next;
		}
		return TRUE;
	}
	return TRUE;
}