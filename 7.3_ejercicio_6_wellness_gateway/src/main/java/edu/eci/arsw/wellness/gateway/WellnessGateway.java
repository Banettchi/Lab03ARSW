package edu.eci.arsw.wellness.gateway;

import edu.eci.arsw.wellness.appointment.*;
import edu.eci.arsw.wellness.gym.*;
import edu.eci.arsw.wellness.medical.*;
import edu.eci.arsw.wellness.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WellnessGateway {
    public static void main(String[] args) {
        // Configuracion de canales
        ManagedChannel appointmentChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel medicalChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        ManagedChannel gymChannel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();
        ManagedChannel recreationChannel = ManagedChannelBuilder.forAddress("localhost", 50054).usePlaintext().build();

        // Stubs
        AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub = AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub = MedicalServiceGrpc.newBlockingStub(medicalChannel);
        GymServiceGrpc.GymServiceBlockingStub gymStub = GymServiceGrpc.newBlockingStub(gymChannel);
        RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub = RecreationServiceGrpc.newBlockingStub(recreationChannel);

        System.out.println("--- WELLNESS API GATEWAY ---");

        // 1. requestAppointment(studentId, serviceType)
        int studentId = 12345;
        System.out.println("\n[Solicitando cita medica]");
        AppointmentRequest request = AppointmentRequest.newBuilder()
                .setStudentId(studentId)
                .setStudentName("Diego")
                .setInstitutionalEmail("diego@school.edu")
                .setServiceType("MEDICINE")
                .setDate("2026-07-01")
                .build();
        AppointmentResponse appResponse = appointmentStub.requestAppointment(request);
        System.out.println("Status: " + appResponse.getStatus() + ", ID: " + appResponse.getId());

        // 2. reserveGymSession(studentId, timeSlot)
        System.out.println("\n[Reservando gimnasio]");
        GymReserveRequest gymRequest = GymReserveRequest.newBuilder().setStudentId(studentId).setTimeSlot("10:00-11:00").build();
        GymReserveResponse gymResponse = gymStub.reserveGymSession(gymRequest);
        System.out.println("Gimnasio: " + gymResponse.getMessage());

        // 3. reserveRecreationResource(studentId, resourceId)
        System.out.println("\n[Reservando recurso recreativo]");
        ResourceRequest recRequest = ResourceRequest.newBuilder().setStudentId(studentId).setResourceId("PingPongTable").build();
        ResourceResponse recResponse = recreationStub.reserveResource(recRequest);
        System.out.println("Recreacion: " + recResponse.getMessage());

        // 4. getStudentWellnessSummary(studentId)
        System.out.println("\n[Resumen de Bienestar del Estudiante " + studentId + "]");
        AppointmentList appointments = appointmentStub.getAppointments(StudentRequest.newBuilder().setStudentId(studentId).build());
        System.out.println("Citas activas: " + appointments.getAppointmentsCount());
        for (Appointment a : appointments.getAppointmentsList()) {
            System.out.println(" - " + a.getServiceType() + " at " + a.getDate() + " [" + a.getStatus() + "]");
        }

        // 5. Consultar especialidades medicas
        System.out.println("\n[Consultando Especialidades Medicas]");
        EmptyMedicalRequest medReq = EmptyMedicalRequest.newBuilder().build();
        SpecialtyList specialties = medicalStub.getSpecialties(medReq);
        System.out.println("Especialidades disponibles: " + specialties.getSpecialtiesCount());
        for (Specialty s : specialties.getSpecialtiesList()) {
            System.out.println(" - " + s.getName() + ": " + s.getDescription());
        }

        // Cierre de canales
        appointmentChannel.shutdown();
        medicalChannel.shutdown();
        gymChannel.shutdown();
        recreationChannel.shutdown();
    }
}
