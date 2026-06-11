package edu.eci.arsw.tcp;

import java.io.*;
import java.net.*;

public class RoomServerTCP {
    public static void main(String[] args) throws Exception {
        RoomRepository repository = new RoomRepository();
        ServerSocket serverSocket = new ServerSocket(35000);
        System.out.println("RoomServer TCP escuchando en puerto 35000...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = in.readLine();
            String response = processRequest(request, repository);
            out.println(response);

            in.close();
            out.close();
            clientSocket.close();
        }
    }

    private static synchronized String processRequest(String request, RoomRepository repository) {
        if (request == null || request.trim().isEmpty()) return "ERROR_OPERACION_INVALIDA";

        String normalized = request.trim().toUpperCase();
        String[] parts = normalized.split(",");

        if (parts.length != 2) return "ERROR_FORMATO_INVALIDO: usa ACCION,ID_SALON (ej: CONSULTAR_SALON,E301)";

        String action = parts[0].trim();
        String id = parts[1].trim();

        Room room = repository.findById(id);
        if (room == null) return "ERROR_SALON_NO_EXISTE: " + id + " no existe. Salones validos: E301, E302, E303, E304";

        switch (action) {
            case "CONSULTAR_SALON":
                return room.isAvailable() ? "SALON_DISPONIBLE" : "SALON_RESERVADO";
            case "RESERVAR_SALON":
                if (room.isAvailable()) {
                    room.setAvailable(false);
                    return "RESERVA_EXITOSA";
                } else {
                    return "ERROR_SALON_YA_RESERVADO";
                }
            case "LIBERAR_SALON":
                if (!room.isAvailable()) {
                    room.setAvailable(true);
                    return "LIBERACION_EXITOSA";
                } else {
                    return "ERROR_SALON_YA_DISPONIBLE";
                }
            default:
                return "ERROR_ACCION_DESCONOCIDA: " + action + ". Acciones validas: CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON";
        }
    }
}
