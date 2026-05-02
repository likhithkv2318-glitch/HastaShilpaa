#!/usr/bin/env sh
# Gradle start-up script for UN*X
GRADLE_OPTS="${GRADLE_OPTS} \"-Xdock:name=$APP_NAME\" \"-Xdock:icon=$APP_HOME/media/gradle.icns\""
APP_HOME="`pwd -P`"
exec "$JAVACMD" "${JVM_OPTS[@]}" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
