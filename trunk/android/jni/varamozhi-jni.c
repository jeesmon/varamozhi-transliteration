#include <string.h>
#include <jni.h>

#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include "mal_api.h"
#include "txt2html.h"

#define FONTNAME "AnjaliOldLipi" 
#define FONTAPI  mozhi_unicode_parse

#define BUFSIZE 614400

char *FONTAPI(const char *txt, long flags); 

jstring
Java_com_jeesmon_apps_varamozhi_Varamozhi_stringFromJNI( JNIEnv* env,
                                                  jobject thiz,
                                                  jstring jinput )
{
    long flags = FL_DEFAULT;
    const char *input = (*env)->GetStringUTFChars(env, jinput, NULL);
    const char *mtext = FONTAPI(input, flags);
    
    return (*env)->NewStringUTF(env, mtext);
}
