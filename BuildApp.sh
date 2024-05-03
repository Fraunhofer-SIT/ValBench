#!/bin/bash
cd "valbenchbaseapk" || exit 1
./gradlew :app:assembleDebug || exit 1