package edu.eci.arsw.wellness.medical;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class MedicalServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50052)
                .addService(new MedicalServiceImpl())
                .build();
        server.start();
        System.out.println("MedicalServer iniciado en puerto 50052");
        server.awaitTermination();
    }

    static class MedicalServiceImpl extends MedicalServiceGrpc.MedicalServiceImplBase {
        @Override
        public void getSpecialties(EmptyMedicalRequest request, StreamObserver<SpecialtyList> responseObserver) {
            SpecialtyList list = SpecialtyList.newBuilder()
                    .addSpecialties(Specialty.newBuilder().setName("MEDICINE").setDescription("General medicine").build())
                    .addSpecialties(Specialty.newBuilder().setName("PSYCHOLOGY").setDescription("Mental health").build())
                    .addSpecialties(Specialty.newBuilder().setName("DENTISTRY").setDescription("Dental care").build())
                    .build();
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }
    }
}
