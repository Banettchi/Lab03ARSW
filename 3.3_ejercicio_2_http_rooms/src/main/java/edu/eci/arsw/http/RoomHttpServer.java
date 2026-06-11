package edu.eci.arsw.http;

import com.sun.net.httpserver.*;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class RoomHttpServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        RoomRepository repository = new RoomRepository();
        
        server.createContext("/rooms", new RoomHandler(repository));
        server.setExecutor(null);
        server.start();
        System.out.println("RoomHttpServer escuchando en http://localhost:8080/rooms");
    }

    static class RoomHandler implements HttpHandler {
        private RoomRepository repository;

        public RoomHandler(RoomRepository repository) {
            this.repository = repository;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                String query = exchange.getRequestURI().getQuery();
                
                String response = "";
                int statusCode = 200;

                if (method.equals("GET")) {
                    if (path.equals("/rooms")) {
                        if (query != null && query.startsWith("id=")) {
                            String id = query.substring(3);
                            Room room = repository.findById(id);
                            if (room != null) {
                                response = "Salon " + id + " esta " + (room.isAvailable() ? "DISPONIBLE" : "RESERVADO");
                            } else {
                                response = "ERROR_SALON_NO_EXISTE";
                                statusCode = 404;
                            }
                        } else {
                            // Listar todos
                            StringBuilder sb = new StringBuilder("Salones:\n");
                            for (Room room : repository.findAll().values()) {
                                sb.append(room.getId()).append(" - ").append(room.isAvailable() ? "DISPONIBLE" : "RESERVADO").append("\n");
                            }
                            response = sb.toString();
                        }
                    }
                } else if (method.equals("POST")) {
                    if (path.equals("/rooms/reserve") && query != null && query.startsWith("id=")) {
                        String id = query.substring(3);
                        Room room = repository.findById(id);
                        if (room != null) {
                            if (room.isAvailable()) {
                                room.setAvailable(false);
                                response = "RESERVA_EXITOSA";
                            } else {
                                response = "ERROR_OPERACION_INVALIDA (Ya reservado)";
                                statusCode = 400;
                            }
                        } else {
                            response = "ERROR_SALON_NO_EXISTE";
                            statusCode = 404;
                        }
                    } else if (path.equals("/rooms/release") && query != null && query.startsWith("id=")) {
                        String id = query.substring(3);
                        Room room = repository.findById(id);
                        if (room != null) {
                            if (!room.isAvailable()) {
                                room.setAvailable(true);
                                response = "LIBERACION_EXITOSA";
                            } else {
                                response = "ERROR_OPERACION_INVALIDA (Ya esta disponible)";
                                statusCode = 400;
                            }
                        } else {
                            response = "ERROR_SALON_NO_EXISTE";
                            statusCode = 404;
                        }
                    } else {
                        response = "Ruta no valida para POST";
                        statusCode = 404;
                    }
                } else {
                    response = "Metodo no soportado";
                    statusCode = 405;
                }

                exchange.sendResponseHeaders(statusCode, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

