#ifndef TOKEN
#define TOKEN -1
#define DEFAULT_STRING_SIZE 255
#define TRUE 1
#define FALSE 0
#include <stdlib.h>
#include <stdio.h>
#endif
typedef int BOOLEAN;
//This is a node
struct NODE
{
	int value;
	struct NODE *next;
};
void add(struct NODE *, int);
void prettyPrint(struct NODE *);
BOOLEAN delete(struct NODE *,int);