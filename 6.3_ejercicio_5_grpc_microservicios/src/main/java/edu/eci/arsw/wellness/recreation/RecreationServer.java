package edu.eci.arsw.wellness.recreation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class RecreationServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50054)
                .addService(new RecreationServiceImpl())
                .build();
        server.start();
        System.out.println("RecreationServer iniciado en puerto 50054");
        server.awaitTermination();
    }

    static class RecreationServiceImpl extends RecreationServiceGrpc.RecreationServiceImplBase {
        @Override
        public void reserveResource(ResourceRequest request, StreamObserver<ResourceResponse> responseObserver) {
            System.out.println("Reserving resource " + request.getResourceId() + " for student " + request.getStudentId());
            ResourceResponse response = ResourceResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Resource reserved successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
