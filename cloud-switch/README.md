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
$ ./server.hs
```

Client:
```
$ nc <SERVER_IP> 18756
```
