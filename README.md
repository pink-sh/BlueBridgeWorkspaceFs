# Bluebridge workspace mounter driver

This application is a barebone driver to connect an iMarine workspace to a linux folder.

The driver implements FUSE (Filesystem in userspace)

In this version just tree browsing is implemented.

### Usage
To run on Debian/Ubuntu systems ensure that fuse is installed otherwise
```sh
sudo apt-get install fuse libfuse-dev
```

In the build/libs folder run the jar as:
```sh
$ java -jar BlueBridgeWorkspaceFs-all-0.1.jar mountpoint={path where the workspace will be mounted} username={your iMarine username} token={your iMarine token}
```

### Build

The application is built using Gradle, to compile it and build the jar run

```sh
gradle fatJar
```
in the application root folder.
