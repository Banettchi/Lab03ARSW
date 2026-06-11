package edu.eci.arsw.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {
    public static void main(String[] args) throws Exception {
        LabInventoryService service = new LabInventoryServiceImpl();
        Registry registry = LocateRegistry.createRegistry(23000);
        registry.rebind("labInventoryService", service);
        System.out.println("LabInventoryService RMI publicado en puerto 23000...");
    }
}

