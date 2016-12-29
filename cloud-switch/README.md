## Before Started

- install java and mvn

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
