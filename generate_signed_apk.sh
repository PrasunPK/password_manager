#!/usr/bin/env bash

echo "Generating release apk ..."
./gradlew assembleRelease -Pandroid.injected.signing.store.file=$KEY_FILE -Pandroid.injected.signing.store.password=$KEY_PASSWORD -Pandroid.injected.signing.key.alias=$KEY_ALIAS -Pandroid.injected.signing.key.password=$KEY_PASSWORD