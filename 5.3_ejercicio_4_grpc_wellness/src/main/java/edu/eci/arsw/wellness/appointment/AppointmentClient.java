package edu.eci.arsw.wellness.appointment;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AppointmentClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub stub =
                AppointmentServiceGrpc.newBlockingStub(channel);

        System.out.println("--- Cliente de Bienestar (Unico Servicio) ---");
        
        System.out.println("Solicitando cita...");
        AppointmentRequest request = AppointmentRequest.newBuilder()
                .setStudentId(111)
                .setStudentName("Diego")
                .setInstitutionalEmail("diego@school.edu")
                .setServiceType("MEDICINE")
                .setDate("2026-06-15")
                .build();
                
        AppointmentResponse response = stub.requestAppointment(request);
        System.out.println("Cita solicitada. ID: " + response.getId() + ", Estado: " + response.getStatus());

        channel.shutdown();
    }
}
