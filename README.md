# OmegaTester

## Run from the sources.
Prerequisites:
JDK 8
Maven > 3.2.1
Your favorite IDE

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

Run form the main method.
Open com.haleywang.monitor.App, and run the main method.

# TODO list
```
1. Add test cases before pre test tab.
2. Add requestData(urlPrams & postBody) as controller parameter.
3. Runner page.
4. Add test demo script.
5. Drag request name to sort.

```



