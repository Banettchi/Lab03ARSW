# University Wellness System with gRPC

Escuela Colombiana de Ingeniería Julio Garavito

Arquitecturas de Software - ARSW

---

## Exercise Description

This project introduces gRPC as a remote communication mechanism. gRPC is a remote procedure call framework originally developed by Google that uses Protocol Buffers as its data serialization format. Unlike RMI, which is tied exclusively to the Java ecosystem, gRPC is language-agnostic: the same contract can be compiled to generate code in Java, Python, Go, C#, or any other supported language.

In this phase, the university wellness system is modeled as a single service managing appointments for various university care areas.

---

## What Was Asked

The exercise required defining a service contract using the Protocol Buffers definition language in a `.proto` file, configuring a Maven project with the necessary plugin to compile that contract and auto-generate Java classes, and implementing the gRPC server and a test client. The service had to allow requesting appointments, canceling them, and querying a student's existing appointments.

---

## Project Structure

```text
5.3_ejercicio_4_grpc_wellness/
├── src/
│   └── main/
│       ├── proto/
│       │   └── appointment.proto
│       └── java/
│           └── edu/eci/arsw/wellness/
│               ├── appointment/
│               │   ├── AppointmentServer.java
│               │   └── AppointmentClient.java
│               └── (classes generated in target/ upon compiling)
├── pom.xml
└── README.md
```

---

## How the Architecture Works

The heart of this system is the `appointment.proto` file. Using the Protocol Buffers language, this file defines what messages exist and what methods a client can invoke on the server. It is the only artifact both parties need to know to communicate.

When `mvn compile` runs, the Maven plugin reads the `.proto` file and automatically generates a set of complex Java classes. These classes handle all data serialization and deserialization to the binary Protobuf format, underlying HTTP/2 connection management, builders for safely constructing messages with strong typing, and ready-to-use client Stubs. The developer never writes this code manually.

On the server, the generated abstract base class is implemented, providing a method for each operation declared in the proto. Instead of returning a value directly, each method receives a `StreamObserver` object onto which `onNext` is called with the response, followed by `onCompleted` to signal the call's conclusion. This design naturally supports bidirectional streaming in the future.

---

## Class by Class Analysis

### appointment.proto

Declares the proto3 syntax and defines the `AppointmentService` with its three methods. It then defines the messages acting as data transfer objects. Each field inside a message has a unique tag number used by Protobuf to efficiently and unambiguously encode the field in binary format.

### AppointmentServer

Creates the gRPC server using `ServerBuilder` on port 50051 and registers an instance of the internal `AppointmentServiceImpl` implementation. This internal class inherits from the auto-generated base class and overrides each service method with the actual business logic. When the server receives an appointment request, it creates the response using the generated builder, emits it via the `StreamObserver`, and completes the call.

### AppointmentClient

Opens a `ManagedChannel` to localhost on port 50051. From that channel, it constructs a `BlockingStub`, the synchronous variant of the generated Stub. It uses the request message builder to construct the strongly-typed request, calls the stub's method, and directly receives the response object.

---

## How to Run

This project uses Maven. First, compile and generate the classes from the proto:

```bash
cd 5.3_ejercicio_4_grpc_wellness
mvn clean compile
```

Start the server in a terminal:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.appointment.AppointmentServer"
```

In another terminal, execute the test client:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.appointment.AppointmentClient"
```