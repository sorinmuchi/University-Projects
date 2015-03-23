#ifndef TOKEN
#define TOKEN -1
#define DEFAULT_STRING_SIZE 255
#define TRUE 1
#define FALSE 0
#include <stdlib.h>
#include <stdio.h>
#endif
//Define a boolean
typedef int BOOLEAN;
//This is a node
struct NODE
{
	int value;
	struct NODE *next;
};
//Declare here to make both list.c and main.c see it
extern struct NODE *head;
//Function prototypes
void add(int);
void prettyPrint();
BOOLEAN delete(int);