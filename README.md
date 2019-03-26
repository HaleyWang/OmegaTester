# OmegaTester

## Run from jar

```
cd OmegaTester/target
java -jar omegatester-0.1.jar
```

## Run from the sources.
Prerequisites:

- JDK 8
- Maven > 3.2.1
- Lombok plugin for your IDE

```
cd OmegaTester
mvn clean install
mvn exec:java
```

To change port use the following commands.

```
cd OmegaTester
mvn exec:java -Dexec.args="8080"

```

## Run form the main method.
Open com.haleywang.monitor.App, and run the main method.

# TODO list
1. Drag request name to sort.
2. Save request into current group.
3. Create new Account.




