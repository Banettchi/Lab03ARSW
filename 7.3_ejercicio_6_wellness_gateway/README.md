# Wellness Gateway - API Gateway with gRPC

Escuela Colombiana de Ingeniería Julio Garavito

Arquitecturas de Software - ARSW

---

## Exercise Description

This project solves the coordination problem exposed by the previous exercise. When the wellness system was decomposed into four independent microservices, the client was forced to know the address and port of each one. The API Gateway is the component that centralizes access, acting as a single entry point for any client wishing to interact with the university wellness ecosystem.

---

## What Was Asked

The exercise required implementing a software component that sits between external clients and the internal microservices of the wellness system. The Gateway had to expose a simplified interface to the outside, completely hide the details of the internal network topology, and route each request to the corresponding microservice. The resulting client had to be simple: pointing to a single address and port to access all system functionalities.

---

## Project Structure

```text
7.3_ejercicio_6_wellness_gateway/
├── src/
│   └── main/
│       ├── proto/
│       │   ├── appointment.proto
│       │   ├── medical.proto
│       │   ├── gym.proto
│       │   └── recreation.proto
│       └── java/
│           └── edu/eci/arsw/wellness/
│               ├── appointment/
│               │   └── AppointmentServer.java
│               ├── medical/
│               │   └── MedicalServer.java
│               ├── gym/
│               │   └── GymServer.java
│               ├── recreation/
│               │   └── RecreationServer.java
│               └── gateway/
│                   └── WellnessGateway.java
├── pom.xml
└── README.md
```

---

## How the Architecture Works

The `WellnessGateway` operates as an intelligent intermediary. Upon startup, it opens gRPC channels to each of the four backend microservices, maintaining a reference to a stub for each. When a client request arrives, the Gateway identifies which domain it belongs to and forwards the call to the appropriate microservice using its internal stub. When it receives the response from the microservice, it consolidates it and returns it to the client.

From the client's perspective, there is only one point of contact. It doesn't know if there are four microservices, forty, or just one. It doesn't know what ports they are on, or what servers they run on. If the infrastructure team decides to move the `GymService` to a different server tomorrow, only the Gateway needs to be updated. The client remains intact.

This pattern also allows the Gateway to add cross-cutting functionalities such as authentication, logging of all requests, rate limiting, or data transformation, without any microservice having to worry about it.

The important architectural caveat of this design is that the Gateway becomes a Single Point of Failure. If it goes down, access to the entire wellness system is interrupted even if the four backend microservices continue to function perfectly. In production, this is solved by deploying multiple instances of the Gateway behind a load balancer.

---

## Class by Class Analysis

### The backend microservices

`AppointmentServer`, `MedicalServer`, `GymServer`, and `RecreationServer` are identical to those in the previous exercise. The backend does not change. The microservices decomposition remains the same. What changes is how the outside world accesses them.

### WellnessGateway

This is the new and central component of this exercise. Upon startup, it creates a `ManagedChannel` to each internal microservice, pointing to their respective ports. With those channels, it builds a blocking stub for each service. Then it implements the routing logic: depending on the type of operation requested, it delegates the call to the corresponding stub, waits for the synchronous response, and returns it. The client calling the Gateway sees a unified and simple interface.

---

## How to Run

Compile the project with Maven:

```bash
cd 7.3_ejercicio_6_wellness_gateway
mvn clean compile
```

Open four terminals for the backend microservices:

```bash
# Terminal 1
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.appointment.AppointmentServer"

# Terminal 2
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.medical.MedicalServer"

# Terminal 3
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.gym.GymServer"

# Terminal 4
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.recreation.RecreationServer"
```

With all services running, start the Gateway in a fifth terminal:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.gateway.WellnessGateway"
```

The Gateway is now the only point of contact. Any client wanting to interact with the university wellness system must point solely to the Gateway.
