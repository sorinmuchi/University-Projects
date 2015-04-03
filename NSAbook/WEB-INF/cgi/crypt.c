#include "Login.h"
unsigned char S[256];
unsigned int i, j;

void swap(unsigned char *s, unsigned int i, unsigned int j) {
    unsigned char temp = s[i];
    s[i] = s[j];
    s[j] = temp;
}

/* KSA */
void rc4_init(unsigned char *key, unsigned int key_length) {
    for (i = 0; i < 256; i++)
        S[i] = i;

    for (i = j = 0; i < 256; i++) {
        j = (j + key[i % key_length] + S[i]) & 255;
        swap(S, i, j);
    }

    i = j = 0;
}

/* PRGA */
unsigned char rc4_output() {
    i = (i + 1) & 255;
    j = (j + S[i]) & 255;

    swap(S, i, j);

    return S[(S[i] + S[j]) & 255];
}

char *rc4_e(char *text, size_t text_length)
{
	char *dup=(char *)malloc(text_length * sizeof(char));
	strcpy(dup,text);
    unsigned char *vector[2] = {"B897996E496EA1DAC43938CFDAF27", dup};
        int y;
        rc4_init(vector[0], strlen((char*)vector[0]));
	char *out=(char *)malloc(2 * text_length * sizeof(char) + 1);
	char *ptr=out;
        for (y = 0; y < strlen((char*)vector[1]); y++)
           ptr += sprintf(ptr,"%02X",vector[1][y] ^ rc4_output());
	sprintf(ptr,"\n");
	*(ptr + 1) = '\0';
    return out;
}