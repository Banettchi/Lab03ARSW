package edu.eci.arsw.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabInventoryServiceImpl extends UnicastRemoteObject implements LabInventoryService {
    private Map<String, Equipment> equipments = new HashMap<>();

    public LabInventoryServiceImpl() throws RemoteException {
        equipments.put("EQ001", new Equipment("EQ001", "Osciloscopio", "Lab Electronica"));
        equipments.put("EQ002", new Equipment("EQ002", "Multimetro", "Lab Electronica"));
        equipments.put("EQ003", new Equipment("EQ003", "Microscopio", "Lab Biologia"));
        equipments.put("EQ004", new Equipment("EQ004", "Espectrometro", "Lab Quimica"));
    }

    @Override
    public List<String> consultarEquipos() throws RemoteException {
        List<String> list = new ArrayList<>();
        for (Equipment eq : equipments.values()) {
            list.add(eq.toString());
        }
        return list;
    }

    @Override
    public String consultarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipments.get(codigo);
        if (eq == null) {
            return "Equipo no encontrado";
        }
        return eq.toString();
    }

    @Override
    public synchronized boolean reservarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipments.get(codigo);
        if (eq != null && eq.isAvailable()) {
            eq.setAvailable(false);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean liberarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipments.get(codigo);
        if (eq != null && !eq.isAvailable()) {
            eq.setAvailable(true);
            return true;
        }
        return false;
    }
}

