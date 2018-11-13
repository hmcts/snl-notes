# Scheduling and listing Notes
[![Build Status](https://travis-ci.org/hmcts/snl-notes.svg?branch=master)](https://travis-ci.org/hmcts/snl-notes)

## Purpose

The purpose of this template is to speed up the creation of new Spring applications within HMCTS
and help keep the same standards across multiple teams. If you need to create a new app, you can
simply use this one as a starting point and build on top of it.

## What's inside

The template is a working application with a minimal setup. It contains:
 * common plugins and libraries
 * setup script to prepare project
 * building the application
 * running the application
 * MIT license and contribution information

The application exposes health endpoint (http://localhost:8092/health) and metrics endpoint
(http://localhost:8092/metrics).

## Plugins

The template contains the following plugins:

  * checkstyle

    https://docs.gradle.org/current/userguide/checkstyle_plugin.html

    Performs code style checks on Java source files using Checkstyle and generates reports from these checks.
    The checks are included in gradle's *check* task (you can run them by executing `./gradlew check` command).

  * pmd

    https://docs.gradle.org/current/userguide/pmd_plugin.html

    Performs static code analysis to finds common programming flaws. Included in gradle `check` task.


  * jacoco

    https://docs.gradle.org/current/userguide/jacoco_plugin.html

    Provides code coverage metrics for Java code via integration with JaCoCo.
    You can create the report by running the following command:

    ```bash
      ./gradlew jacocoTestReport
    ```

    The report will be created in build/reports subdirectory in your project directory.

  * io.spring.dependency-management

    https://github.com/spring-gradle-plugins/dependency-management-plugin

    Provides Maven-like dependency management. Allows you to declare dependency management
    using `dependency 'groupId:artifactId:version'`
    or `dependency group:'group', name:'name', version:version'`.

  * org.springframework.boot

    http://projects.spring.io/spring-boot/

    Reduces the amount of work needed to create a Spring application

  * org.owasp.dependencycheck

    https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html

    Provides monitoring of the project's dependent libraries and creating a report
    of known vulnerable components that are included in the build. To run it
    execute `gradle dependencyCheck` command.

  * com.github.ben-manes.versions

    https://github.com/ben-manes/gradle-versions-plugin

    Provides a task to determine which dependencies have updates. Usage:

    ```bash
      ./gradlew dependencyUpdates -Drevision=release
    ```

## Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

```bash
  ./gradlew build
```

## Running the application

Create the image of the application by executing the following command:

```bash
  ./gradlew installDist
```

Create docker image:

```bash
  docker-compose build
```

### Running Locally (Recommended)

For this approach, the database must still be served via docker:
```bash
  docker-compose up snl-notes-db
```

The application can be run locally using IntelliJ or by executing the following command (in another terminal window):
```bash
  ./gradlew bootRun
```

### Alternative script to run application in Docker

To skip all the setting up and building, just execute the following command:

```bash
  ./bin/run-in-docker.sh
```

For more information:

```bash
  ./bin/run-in-docker.sh -h
```

Script includes bare minimum environment variables necessary to start api instance. Whenever any variable is changed or any other script regarding docker image/container build, the suggested way to ensure all is cleaned up properly is by this command:

```bash
  docker-compose rm
```

It clears stopped containers correctly. Might consider removing clutter of images too, especially the ones fiddled with:

```bash
  docker images

  docker image rm <image-id>
```

There is no need to remove postgres and java or similar core images.

## Testing

```bash
  ./gradlew test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
