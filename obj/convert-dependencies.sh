#!/bin/sh
# AUTO-GENERATED FILE, DO NOT EDIT!
if [ -f $1.org ]; then
  sed -e 's!^J:/Android_x64/cygwin_x64/lib!/usr/lib!ig;s! J:/Android_x64/cygwin_x64/lib! /usr/lib!ig;s!^J:/Android_x64/cygwin_x64/bin!/usr/bin!ig;s! J:/Android_x64/cygwin_x64/bin! /usr/bin!ig;s!^J:/Android_x64/cygwin_x64/!/!ig;s! J:/Android_x64/cygwin_x64/! /!ig;s!^J:!/cygdrive/j!ig;s! J:! /cygdrive/j!ig;s!^G:!/cygdrive/g!ig;s! G:! /cygdrive/g!ig;s!^F:!/cygdrive/f!ig;s! F:! /cygdrive/f!ig;s!^E:!/cygdrive/e!ig;s! E:! /cygdrive/e!ig;s!^D:!/cygdrive/d!ig;s! D:! /cygdrive/d!ig;s!^C:!/cygdrive/c!ig;s! C:! /cygdrive/c!ig;' $1.org > $1 && rm -f $1.org
fi
