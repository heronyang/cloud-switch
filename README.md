# cloud-switch
Transfer your files between different cloud storage services.

## Install Java and Maven

- install java and mvn

## Get Google app credentials

Before running, you need to have a valid Google application credential to access Google API. Please follow [this tutorial](https://developers.google.com/identity/protocols/OAuth2WebServer) by Google Developers, section *Creating web application credentials*.

After getting the credentials, rename it as `client_secret.json` , and put this file in `/src/main/resources`. 

Please note that Google has given certain qoutas to each application, please check [Google API Console](https://console.developers.google.com/) for details. 

## Get Dropbox keys

```
cp src/plugins/dropbox/Config.java.sample src/plugins/dropbox/Config.java
```

## Build

```
$ mvn install -DskipTests
```

## Test

```
$ mvn test
```

## Run Local

Main:
```
$ mvn exec:java -Dexec.mainClass="main.Main"
```

## Run Remote

Server:
```
$ ./server.sh
```

Client:
```
$ telnet <SERVER_IP> 18756
```
