#!/usr/bin/env bash

echo "Generating release apk ..."
./gradlew assembleRelease -Pandroid.injected.signing.store.file=$KEY_FILE -Pandroid.injected.signing.store.password=$KEY_PASSWORD -Pandroid.injected.signing.key.alias=$KEY_ALIAS -Pandroid.injected.signing.key.password=$KEY_PASSWORD

echo "Moving to apk directory ..."
mv ./app/build/outputs/apk/release/password-manager-1.1.1-release.apk ./release/apk/.