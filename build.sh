#!/bin/bash

set -euo pipefail

ME=`basename $0`
OS=`uname`
if [ "$OS" = "Darwin" ] ; then
    MYFULL="$0"
else
    MYFULL=`readlink -sm $0`
fi
MYDIR=`dirname ${MYFULL}`
echo MYDIR=${MYDIR}

echo PWD=`pwd`

echo ${GO_PIPELINE_LABEL} > ./resources/build.txt

cd ${MYDIR}

echo "[${ME}] Preparing Backend application"
echo "[${ME}] Building backend..."
exec "${MYDIR}/sbt/bin/sbt" clean compile assembly -Dsbt.override.build.repos=true -Dsbt.repository.config=./repositories -Dsbt.log.noformat=true
