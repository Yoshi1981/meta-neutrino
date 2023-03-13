#!/bin/sh

package="neutrino-plugins"

#srcdir=`dirname $0`
#test -z "$srcdir" && srcdir=.

cd "$srcdir"
echo "Generating configuration files for $package within $srcdir, please wait ..."

aclocal --force
libtoolize --force
autoconf --force
autoheader --force
automake --add-missing --force-missing
