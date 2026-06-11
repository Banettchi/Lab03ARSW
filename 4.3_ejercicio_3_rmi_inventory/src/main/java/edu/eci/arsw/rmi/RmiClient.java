package edu.eci.arsw.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23000);
        LabInventoryService service = (LabInventoryService) registry.lookup("labInventoryService");
        
        System.out.println("--- Lista de equipos ---");
        List<String> equipos = service.consultarEquipos();
        for (String eq : equipos) {
            System.out.println(eq);
        }
        
        System.out.println("\nConsultando EQ001: " + service.consultarEquipo("EQ001"));
        
        System.out.println("Reservando EQ001: " + (service.reservarEquipo("EQ001") ? "Exito" : "Fallo"));
        System.out.println("Consultando EQ001 despues de reservar: " + service.consultarEquipo("EQ001"));
        
        System.out.println("Intentando reservar EQ001 nuevamente: " + (service.reservarEquipo("EQ001") ? "Exito" : "Fallo"));
        
        System.out.println("Liberando EQ001: " + (service.liberarEquipo("EQ001") ? "Exito" : "Fallo"));
        System.out.println("Consultando EQ001 despues de liberar: " + service.consultarEquipo("EQ001"));
    }
}

