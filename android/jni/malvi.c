#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include "mal_api.h"
#include "txt2html.h"

#define FONTNAME "AnjaliOldLipi" 
#define FONTAPI  mozhi_unicode_parse

#define BUFSIZE 614400
/* #define BUFSIZE 2 */

/* Don't put newlines in this preamble because it will spoil the newline count in editor.pl */

#define UNICODE_PREAMBLE "<html lang=\"ml\"><head><meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\">"

char *FONTAPI(char *txt, long flags); 

int main(int argc, char **argv) {
     
     int c;
     extern char *optarg;
     extern int optind;
     int errflg = 0;
     char text[BUFSIZE];
     unsigned long int readcount;
     char *mtext;
     long flags = FL_DEFAULT;
     
     while ((c = getopt(argc, argv, "bcrghuf")) != EOF)
     {
           switch (c) {
               
          case 'h':
               malfont = FONTNAME;
               malfontlen = strlen( malfont );
               fn_mal_trans = txt2htmlfont;
               /* fn_eng_trans = txt2html_nofont; */
               fn_eng_trans = NULL;
               break;
          case 'r':
               flags |= FL_ALLOW_RDOT;
               break;
 
           case 'b': /* basic */
               flags &= ~FL_PROCESS_MACRO;
               break;
 
          case 'c':
               flags |= FL_CARRY_COMMENT; /* carry comment seqenceses as such */
               break;
 
          case 'g':
               flags |= FL_DEBUG;
               break;
 
          case 'f':
               write(STDOUT_FILENO, FONTNAME, strlen(FONTNAME));
               exit(0);
               break;
 
          case 'u':
               write(STDOUT_FILENO, UNICODE_PREAMBLE, strlen(UNICODE_PREAMBLE));
               fn_mal_trans = txt2html_unicode;
               fn_eng_trans = NULL;
               break;

          case '?':
               errflg++;
          }
     }
 
     if (errflg) {
          fprintf(stderr, "usage: malvi_mozhi_* [-brhcuf]\n");
          exit (2);
     }
 

     while ((readcount = saferead(STDIN_FILENO, text, BUFSIZE-1)) > 0)
     {
          text[readcount] = '\0';

          mtext = FONTAPI(text, flags);
          write(STDOUT_FILENO, mtext, strlen(mtext));
          free(mtext); 
     }
     return 0;
}

