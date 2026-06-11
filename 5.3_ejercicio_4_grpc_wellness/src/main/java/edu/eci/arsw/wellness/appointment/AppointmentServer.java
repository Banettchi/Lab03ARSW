package edu.eci.arsw.wellness.appointment;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class AppointmentServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new AppointmentServiceImpl())
                .build();
        server.start();
        System.out.println("AppointmentServer iniciado en puerto 50051");
        server.awaitTermination();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private Map<Integer, Appointment> appointments = new HashMap<>();
        private int currentId = 1;

        @Override
        public void requestAppointment(AppointmentRequest request, StreamObserver<AppointmentResponse> responseObserver) {
            Appointment app = Appointment.newBuilder()
                    .setId(currentId)
                    .setStudentId(request.getStudentId())
                    .setServiceType(request.getServiceType())
                    .setDate(request.getDate())
                    .setStatus("REQUESTED")
                    .build();
            appointments.put(currentId, app);

            AppointmentResponse response = AppointmentResponse.newBuilder()
                    .setId(currentId)
                    .setStatus("REQUESTED")
                    .build();
            currentId++;
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void cancelAppointment(CancelRequest request, StreamObserver<CancelResponse> responseObserver) {
            int id = request.getAppointmentId();
            boolean success = false;
            if (appointments.containsKey(id)) {
                Appointment oldApp = appointments.get(id);
                appointments.put(id, oldApp.toBuilder().setStatus("CANCELLED").build());
                success = true;
            }
            responseObserver.onNext(CancelResponse.newBuilder().setSuccess(success).build());
            responseObserver.onCompleted();
        }

        @Override
        public void getAppointments(StudentRequest request, StreamObserver<AppointmentList> responseObserver) {
            int studentId = request.getStudentId();
            AppointmentList.Builder listBuilder = AppointmentList.newBuilder();
            
            for (Appointment app : appointments.values()) {
                if (app.getStudentId() == studentId && !app.getStatus().equals("CANCELLED")) {
                    listBuilder.addAppointments(app);
                }
            }
            
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
