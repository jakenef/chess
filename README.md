# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Chess Server Design UML Diagram

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysLwYeqLorE2IJoYLphm6ZIUgatLlqOJpEuGFoctyvIGoKwowEBMDnDAH5LKYmEajhLIerqmSxv604hq60qRhy0YwLx8ZylhyaQSW+4oWouaYP+ILQSUVwDERowLpOfTTrOzbjq2fTfleKmFNkPYwP2g69BpI5aYZ1ZTkG+nzo5bbLqu3h+IEXgoOge4Hr4zDHukmSYBZF5FNQ17SAAorucX1HFzQtA+qhPt0emNug7a-mcQIAdlc4QYV+WYXBQW+rCcnBahGIYZJ7GkbhMCet6QYBkGAkceawlWjaYnddojrOgSvXukYKDcDxnXFegJFMm65GlNI00UoY4naEKYTzWgo2JtJZXlLVvrZopymdj+VwmTFZldjkYB9gOQ5LpwXnroEkK2ru0IwAA4qOrKhaeEXnswqnXv9SWpfYo5ZS5OX7T+bLKVce2lambIVTAyCxDV0L1ehB1YeNLWce1s1+l1cbaItZoRoUUaDVt8gk81S2cbj0KA6MqiwvTZH9ZRtrAMqAOjhJMGdmje6EwpCB5jJ93XaWUxw3z4yVP0GsoAAktIWtPCemQGhWEwzN0Uw6AgoANmbo4rF8usAHKjhbewwI0t3HEm5ng89Nk9OrQNaxUOujgbRtTCb+qafcfSW0kfQ23bDtaRbTyu+7iee97ZjvZ43kbtgPhQNg3DwNxhi84YIPhZFEOXnd5Q3g0sPw8EiNzkO2ejD7HZHam6Pd+gsx9ygmNQc3TryjAaAoMktewrXRNYuz2Hk9q1e8TTwZ06GW+M+yA0xsNbNsZvnOkm11fL7ra+C8t-VwNXDHz4vEvipfQ-AuULuL1XgrJWZUrqmSuCHUYUdygAEZewAGYAAsMAB73UboHIckD9aG1gQg5Bb0VxF0+gESw014LJBgAAKQgDyL+ddU4gAbGDR62NwGVCqJSO8LRdYIzrEjIcFdgCkKgHACA8EoDj0jtIVBqNlYjz4XOd8tthGiPEZIqB0ilLK1YTBcoAArGhaBl7UJ5I-DeZNr6TXJGAXe-ED6CT6kzDk-0KRDVpvIGAsJIRgDLkkICjVpZXwZnhMA98pFPyEk48oIs6E0TCLrA25iZATXKDEVR0AFBUF+LobgsIEnSCUUIyg6SoABNnqTZJR9yh+C0JkMJGjCkqLEdAWY-jD6WJWnybAtSa6SxGj-P8ciqGGKATmRWWjQF+2impUsMipnoKsi9XoBCPo+QCF4IRXYvSwGANgCuhB4iJBSGFM8LDIZpgqPFRKyVUrGDyrI46syJlY1UuCNq3A8CMj0AYAWSSHGTRAB8qAvFanAF+f8zpa0ZqbSDKCv5KT3nbOotocFE1IXrUyHaAU-SmpBPDOUQF2za6MlRUfdF0LYk4sCb-EsiBtnnXGZdKZqsehzNOAs6yr0C4riAA