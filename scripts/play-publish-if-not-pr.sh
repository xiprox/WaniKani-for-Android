#!/bin/bash
set -ev

# Prepare env. Keeping this in another script didn't work. I tried enough (15 times/builds)
cp WaniKani/google-services-dummy.json WaniKani/google-services.json

if [ "${TRAVIS_PULL_REQUEST}" = "false" ] && [ $TRAVIS_BRANCH == 'master' ]; then
  echo "Assembling and publishing release"
  ./gradlew assembleRelease publishApkRelease
else
  echo "Assembling Debug"
  ./gradlew assembleDebug
fi