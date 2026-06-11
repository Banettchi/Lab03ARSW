package edu.eci.arsw.wellness.gym;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class GymServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50053)
                .addService(new GymServiceImpl())
                .build();
        server.start();
        System.out.println("GymServer iniciado en puerto 50053");
        server.awaitTermination();
    }

    static class GymServiceImpl extends GymServiceGrpc.GymServiceImplBase {
        @Override
        public void reserveGymSession(GymReserveRequest request, StreamObserver<GymReserveResponse> responseObserver) {
            System.out.println("Reserving gym for student " + request.getStudentId() + " at " + request.getTimeSlot());
            GymReserveResponse response = GymReserveResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Session reserved successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
