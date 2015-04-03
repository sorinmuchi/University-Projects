Login.cgi: base64.o Login.o
	gcc -o Login.cgi base64.o Login.o
Login.o: Login.c Login.h
	gcc -c Login.c
base64.o: base64.c Login.h
	gcc -c base64.c
clean: 
	rm -f base64.o Login.o