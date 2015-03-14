#ifndef TOKEN
#define TOKEN -1
#define DEFAULT_STRING_SIZE 255
#include <stdlib.h>
#include <stdio.h>
#endif
typedef BOOLEAN int;
typedef TRUE 1;
typedef FALSE 0;
//This is a node
struct NODE
{
	int value;
	struct NODE *next;
};
void add(struct NODE *, int);
void prettyPrint(struct NODE *);
BOOLEAN delete(struct NODE *,int);