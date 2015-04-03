#if !defined(TOKEN)
#define SIZE 1000
#define BOOLEAN int
#define TRUE 1
#define true 1
#define FALSE 0
#define false 0
#define TOKEN -1
#define WHITESPACE 64
#define EQUALS     65
#define INVALID    66
#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>
#include <errno.h>
#include "base64.c"
#include "crypt.c"
#define ARRAY_SIZE(a) (sizeof(a)/sizeof(a[0]))
#endif
size_t getSize(char*);
int getLineCount(FILE*);
int getColumn(FILE*,int,char *);
BOOLEAN overflows(char *);
char * B64E(char *);
FILE* mopen();