# ARSW 2026-I: Distributed Architectures Evolution Lab

Escuela Colombiana de Ingeniería Julio Garavito

Arquitecturas de Software - ARSW

---

## Overview

This repository contains the complete implementation of all applied exercises from the distributed architectures workshop. The project follows a deliberate pedagogical progression: each exercise takes the same core problem, a university management system, and solves it using a different architectural style. By the end, the student has experienced firsthand why modern distributed systems look the way they do, and what concrete problems motivated the invention of each successive approach.

The progression moves from raw TCP Sockets, through HTTP, then to RMI, then to gRPC with a single service, then to a decomposed microservices architecture, and finally to a full API Gateway pattern. The last exercise is a design-only integrative challenge that asks the student to synthesize everything learned.

---

## Exercise 1 - Client-Server Architecture with TCP Sockets

### What Was Asked

The workshop asked for a server capable of managing university study room reservations using only low-level Java network primitives. No HTTP, no RPC, no frameworks. The server had to maintain an in-memory list of four rooms, expose operations to check availability, reserve a room, and release it, and communicate with clients using a custom text-based protocol invented by the developer.

### What Was Built

The server listens on port 35000 and waits indefinitely for incoming connections. When a client connects, it reads a single line of text sent by the client, splits it by a comma to separate the action from the room identifier, looks up the room in an in-memory repository, executes the operation, writes back a plain text response, and closes the connection. The valid actions are to query the status of a room, reserve a room that is currently free, and release a room that is currently occupied. For each of these cases the server returns a descriptive string confirming the result, and for any malformed or unrecognized input it returns an error string.

The business logic that modifies the room state is protected with the synchronized keyword. This guarantees that even if multiple clients were to connect simultaneously, only one reservation operation can execute at a time, preventing two clients from successfully reserving the same room in the same instant.

The client side reads text typed by the user in the console, opens a connection to the server, sends that text, waits for the server to respond, and prints the result. The connection is opened fresh for every message and closed immediately after.

### Why This Approach Has Limits

The critical weakness of this design is that the protocol is entirely arbitrary and fragile. If a client sends a badly formed string, the server crashes or returns a generic error. Adding a new operation requires modifying the server code and updating every single client application manually. There is no versioning, no schema, and no interoperability with external tools. A JavaScript frontend, a Python script, or a mobile app written in Swift cannot consume this server without writing a custom TCP client using the exact same text convention.

---

## Exercise 2 - HTTP Architecture

### What Was Asked

The workshop asked to evolve the room reservation system to use the HTTP protocol instead of the custom TCP text protocol. The server had to expose standard REST-style endpoints: a GET route to list all rooms or query a specific one, and POST routes to reserve or release a room. The constraint was to use only Java's built-in server classes, without any external framework.

### What Was Built

The server binds to port 8080 and registers a custom handler that intercepts all HTTP requests. Inside the handler, the code reads the HTTP method from the incoming request and the path of the URL to determine which operation the client is asking for. When the path points to the listing endpoint and the method is GET, it returns all rooms. When the path points to the reserve or release endpoints and the method is POST, it extracts the room identifier from the URL query string, performs the operation on the in-memory repository, and writes back a plain text result along with an appropriate HTTP status code.

The model and the repository are exactly the same as in Exercise 1. The only thing that changed is the communication layer. Instead of parsing raw strings sent over a socket, the server now delegates to the HTTP infrastructure to parse the incoming request and format the outgoing response.

### Why This Is Better Than TCP

The shift to HTTP means the server is now universally interoperable. A browser, a Postman window, a curl command in the terminal, or any application in any programming language can consume it without knowing anything specific about this project. The use of HTTP verbs communicates intent semantically: GET is understood globally to be a read-only operation, while POST implies a state change. Status codes carry standardized meaning that any client can interpret correctly without reading custom documentation.

---

## Exercise 3 - Java RMI

### What Was Asked

The workshop introduced the RPC paradigm through Java's native remote invocation system. The exercise asked to implement a laboratory equipment inventory where the client invokes methods on a remote object as if it were a local one. The contract had to include operations to list all equipment, query a single item, reserve it, and release it. The model class had to be transmissible over the network.

### What Was Built

The system is composed of four pieces. The first is a remote interface that declares all four operations and signals to the Java runtime that these methods can be invoked from another machine. The second is a concrete implementation class that extends the base class required to export the object for remote access and contains the actual business logic using an in-memory map to store equipment data. The third is the server, which creates a registry process on port 23000 and publishes the implementation under a name that clients can look up. The fourth is the client, which connects to that registry, retrieves a reference to the remote object, and then calls methods on it exactly as if the object were instantiated locally in the same JVM.

The Equipment class that represents each physical item in the inventory must implement the Serializable interface. This is required because when the client calls a method that returns an Equipment object, Java must convert that object into bytes to send it over the network, and then reconstruct it on the client side. Without Serializable, Java would refuse to do this at runtime.

### Why This Has Limits Despite Its Elegance

RMI is elegant from a developer experience standpoint because the network is completely invisible to the programmer writing the client code. However, it creates an absolute dependency on the Java ecosystem. A Python microservice, a Go service, or a mobile application cannot consume an RMI server natively. The object serialization format is Java-specific and opaque to the outside world. As systems grow to include teams using different languages and stacks, this becomes an architectural dead end.

---

## Exercise 4 - gRPC with Protocol Buffers

### What Was Asked

The workshop asked to implement a university wellness appointment system using gRPC. The exercise required defining the service contract in a proto file using the Protocol Buffers definition language, configuring a Maven project with the code generation plugin so that Java classes are automatically produced from that definition, and implementing the server and a test client. The service had to handle appointment requests carrying student information, the type of wellness service needed, and a preferred date.

### What Was Built

The entire contract of the service lives in a single proto file. This file declares the name of the service, the name and signature of each method, and the structure of every message type that flows in and out of those methods. Each field in a message is assigned a unique number that Protocol Buffers uses internally to encode the data in a compact binary format.

When Maven compiles the project, a plugin reads the proto file and generates a large amount of Java code automatically. This generated code includes data classes with builder patterns to construct messages safely, the base class that the server implementation must extend, and the stub classes that the client uses to make calls. The developer never writes any of this manually.

On the server side, the implementation overrides a method for each operation declared in the proto file. Instead of returning a value directly, each method receives a stream observer object, pushes the response into it with a call to onNext, and signals completion with a call to onCompleted. This design enables the framework to support streaming responses in the future without changing the server interface.

On the client side, a channel is opened to the server address and a blocking stub is created from that channel. Requests are built using the generated builder classes, which enforce that all required fields are provided with the correct types at compile time rather than at runtime.

### Why gRPC Solves What RMI Could Not

The proto file is completely language-agnostic. The same file can be compiled to generate client and server code in Java, Python, Go, Rust, C++, or a dozen other languages. All of them will communicate using the same binary wire format, which is far more compact and faster to parse than JSON or plain text. The framework also runs on top of HTTP/2, which allows multiple calls to share a single connection and enables server-side or bidirectional streaming.

---

## Exercise 5 - Microservices Decomposition

### What Was Asked

The workshop asked to take the wellness system and apply the Single Responsibility Principle at the architectural level, splitting it into four completely independent services: one for scheduling general appointments, one for medical consultations, one for gym reservations, and one for recreational activities. Each had to have its own proto contract and run as its own independent process on a separate port. The exercise also asked to build a client that consumed multiple services directly, deliberately exposing the coordination problem this creates.

### What Was Built

Four independent Maven services run on separate ports from 50051 to 50054. Each service has its own proto file defining only the domain it owns, its own server class with its own startup code, and its own in-memory business logic completely isolated from the others. A test client class was built to demonstrate the problem: to interact with more than one domain, it must create a separate channel pointing to each port, build a separate stub for each service, and manage all of them independently. If any port changes or any service moves to a different host, the client code breaks.

The separation of proto files is a deliberate design choice. If one team decides to change the structure of a gym reservation message, that change does not affect the proto file for medical appointments, and therefore does not require recompiling or redeploying the medical service.

### The Problem This Exposes

Each service can now be scaled, updated, and deployed independently, which is the primary benefit of this architecture. If gym reservations spike, only that service needs more capacity. But the client now carries the burden of knowing the entire internal network topology of the backend system. This is the architectural problem that the API Gateway in the next exercise comes to solve.

---

## Exercise 6 - API Gateway

### What Was Asked

The workshop asked to implement a gateway component that centralizes all external access to the wellness microservices. The gateway had to hide the internal topology from clients, exposing a single contact point and routing requests internally to the correct microservice based on the nature of the request.

### What Was Built

The WellnessGateway is a single Java class that at startup opens gRPC channels to each of the four backend microservices and keeps a stub for each one in memory. When a call arrives at the gateway, it inspects the type of request, selects the appropriate backend stub, forwards the call, waits for the response, and returns it to the caller. The backend microservices themselves remain completely unchanged from the previous exercise. The only addition is this orchestrating layer.

From the perspective of any external client, only the gateway exists. The client does not know and does not need to know that there are four different services running internally. If the team decides tomorrow to split the gym service into two separate services for cardio and weights, or to move any service to a different port, only the gateway configuration changes. The client application continues working without modification.

### Architectural Trade-Off

The gateway solves the coordination problem completely but introduces a single point of failure. If the gateway process crashes, all four microservices become unreachable to the outside world even though they are still running correctly. In production systems this is addressed by deploying multiple instances of the gateway behind a load balancer.

---

## Exercise 7 - ECICIENCIA Platform Design

### What Was Asked

The final integrative exercise asked for an architectural design proposal for a platform to manage a university academic event. No full implementation was required. The deliverables were a visual architecture diagram, a reasoned justification for choosing microservices over a monolith, a description of each proposed service and its responsibilities, and a proto file demonstrating how the contracts would look. This exercise tested whether the student could apply all the architectural reasoning accumulated throughout the workshop to a fresh, realistic problem.

### What Was Built

The design proposes three microservices coordinated by an API Gateway. The AssistantService handles the registration and management of event attendees. The AgendaService manages the full schedule of talks, workshops, and activities, and is expected to handle the highest volume of read traffic during the event as thousands of attendees consult it simultaneously. The WorkshopService manages the reservation of seats for interactive workshops and requires strong concurrency controls to prevent multiple people from claiming the last available seat at the same time.

The justification documents argue that separating these three concerns allows each service to scale independently based on its actual traffic pattern, makes the system resilient to isolated failures since a crash in the workshop reservation module does not prevent attendees from reading the agenda, and allows separate development teams to work on each service in parallel without interfering with each other. A proto file demonstrates what the contracts for these three services would look like in practice.

---

## How to Run Each Exercise

Each exercise folder contains its own README with detailed compilation and execution instructions.

Exercises 1, 2, and 3 use only the Java compiler. Navigate into the source folder, compile the package with javac, and run the Server class first followed by the Client class in a separate terminal.

Exercises 4, 5, and 6 use Maven. Navigate to the exercise folder, run the Maven compile command to generate the gRPC classes from the proto files, then start each server or the gateway using the Maven exec plugin targeting the appropriate main class. Exercise 6 requires starting all four backend microservices before starting the gateway.

Exercise 7 contains only design documents and requires no execution.
