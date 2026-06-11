# Architectural Design - ECICIENCIA Platform

Escuela Colombiana de Ingeniería Julio Garavito

Arquitecturas de Software - ARSW

---

## Exercise Description

This is the final integrative exercise of the workshop. Unlike all the previous ones, it's not about coding a functional system, but demonstrating architectural judgment by applying everything learned during the journey from TCP to the API Gateway. The deliverable product is a well-founded, justified design expressed through formal architecture artifacts.

---

## What Was Asked

The university required a distributed platform to manage the activities of the ECICIENCIA event, which includes talks, interactive workshops, and attendee registration. The exercise asked to propose the complete architecture of the system, explaining what services would exist, what responsibilities each would have, why a microservices architecture was chosen instead of a monolith, how the services would communicate with each other, and how users would access the platform. The design also had to be materialized in an architecture diagram and an example `.proto` contract file.

---

## Project Structure

```text
8.0_ejercicio_final_eciciencia/
├── arquitectura.md
├── eciciencia.proto
└── README.md
```

---

## Proposed Design

The platform is structured into three independent microservices coordinated by a central API Gateway.

The `AssistantService` manages the registration and personal data of event attendees. Its workload is high at the beginning of the event but then decreases significantly.

The `AgendaService` manages the event's list of activities, time slots, assigned rooms, and speakers. This service is predominantly read-only and will suffer the highest volume of traffic during the event, as thousands of attendees will consult the agenda simultaneously.

The `WorkshopService` controls the reservation of specific spots for interactive workshops. It is the most critical service in terms of consistency, as it must ensure that spots are not oversold when many users attempt to reserve simultaneously.

The API Gateway is the single entry point for all client applications, both web and mobile. Clients do not know what services exist or what addresses they run on.

---

## Microservices Architecture Justification

If ECICIENCIA were built as a monolith, all services would run as a single process. The immediate problem is scaling: if the agenda receives ninety percent of the traffic, the entire monolith must be replicated to support that load, which implies also scaling the registration module even though it's practically inactive at that moment, wasting infrastructure resources.

The second problem is resilience. If the `WorkshopService` has a bug that consumes all available memory during a reservation peak, in a monolith that error would bring down the entire system. Users would no longer be able to consult the agenda or register. With microservices, the `WorkshopService` can crash in isolation while the rest of the system continues to function normally.

The third problem is development. With a monolith, all teams work on the same repository and the same code. A poorly made change in the registration module can break the workshops module. With microservices, each team has total autonomy over their service.

---

## Delivered Artifacts

### arquitectura.md

Contains the complete architecture diagram in Mermaid format, which can be rendered directly on GitHub or any compatible Markdown viewer. The diagram shows the flow from external clients to the Gateway and from the Gateway to each microservice and its isolated database.

### eciciencia.proto

Materializes the design into concrete code. It defines the three services, `AssistantService`, `AgendaService`, and `WorkshopService`, with their methods and the messages they exchange. It demonstrates how the contract establishes a common language between development teams and allows the Gateway team and each microservice's team to work in parallel using the same file as a source of truth.
