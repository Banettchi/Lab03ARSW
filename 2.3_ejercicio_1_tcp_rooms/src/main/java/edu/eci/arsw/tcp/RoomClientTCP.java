package edu.eci.arsw.tcp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RoomClientTCP {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cliente de Salones TCP ===");
        System.out.println("Comandos disponibles:");
        System.out.println("  CONSULTAR_SALON,E301");
        System.out.println("  RESERVAR_SALON,E302");
        System.out.println("  LIBERAR_SALON,E302");
        System.out.println("Escribe 'salir' para terminar.");
        System.out.println("==============================");

        while (true) {
            System.out.print("\nIngrese comando: ");
            String command = scanner.nextLine().trim().toUpperCase();

            if (command.equals("SALIR")) {
                System.out.println("Cerrando cliente.");
                break;
            }

            if (command.isEmpty()) {
                continue;
            }

            try {
                Socket socket = new Socket("127.0.0.1", 35000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(command);
                String response = in.readLine();
                System.out.println("Respuesta del servidor: " + response);

                in.close();
                out.close();
                socket.close();
            } catch (ConnectException e) {
                System.out.println("ERROR: No se pudo conectar al servidor. Asegurate de que el servidor este corriendo.");
            }
        }

        scanner.close();
    }
}
