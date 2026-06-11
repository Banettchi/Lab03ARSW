package edu.eci.arsw.wellness.client;

import edu.eci.arsw.wellness.appointment.*;
import edu.eci.arsw.wellness.gym.*;
import edu.eci.arsw.wellness.medical.*;
import edu.eci.arsw.wellness.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MultiClient {
    public static void main(String[] args) {
        // En esta version de microservicios, el cliente conoce TODOS los puertos
        ManagedChannel appointmentChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel medicalChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        ManagedChannel gymChannel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();
        ManagedChannel recreationChannel = ManagedChannelBuilder.forAddress("localhost", 50054).usePlaintext().build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub = AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub = MedicalServiceGrpc.newBlockingStub(medicalChannel);
        GymServiceGrpc.GymServiceBlockingStub gymStub = GymServiceGrpc.newBlockingStub(gymChannel);
        RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub = RecreationServiceGrpc.newBlockingStub(recreationChannel);

        System.out.println("--- Cliente Fat (Conoce multiples servicios) ---");
        
        System.out.println("1. Hablando con AppointmentService en el puerto 50051");
        AppointmentRequest req = AppointmentRequest.newBuilder().setStudentId(222).setServiceType("DENTISTRY").setDate("2026-06-20").build();
        System.out.println("Cita status: " + appointmentStub.requestAppointment(req).getStatus());

        System.out.println("2. Hablando con MedicalService en el puerto 50052");
        EmptyMedicalRequest medReq = EmptyMedicalRequest.newBuilder().build();
        System.out.println("Especialidades disponibles: " + medicalStub.getSpecialties(medReq).getSpecialtiesCount());

        System.out.println("3. Hablando con GymService en el puerto 50053");
        GymReserveRequest gymReq = GymReserveRequest.newBuilder().setStudentId(222).setTimeSlot("16:00-17:00").build();
        System.out.println("Gimnasio status: " + gymStub.reserveGymSession(gymReq).getMessage());

        System.out.println("4. Hablando con RecreationService en el puerto 50054");
        ResourceRequest recReq = ResourceRequest.newBuilder().setStudentId(222).setResourceId("Mesa_PingPong").build();
        System.out.println("Recreacion status: " + recreationStub.reserveResource(recReq).getMessage());

        appointmentChannel.shutdown();
        medicalChannel.shutdown();
        gymChannel.shutdown();
        recreationChannel.shutdown();
    }
}
