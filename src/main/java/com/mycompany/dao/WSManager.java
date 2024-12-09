/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.mycompany.domain.Operador;
import com.mycompany.domain.Repartidor;
import com.mycompany.domain.Rol;
import com.mycompany.domain.Usuario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WSManager {

    public static final String WS_URL = "http://localhost/WS_RepartidoraAgua/";

    // Metodo paa iniciar sesion
    public static Usuario iniciarSesion(String correo, String contrasena) {
        // Obtenemos los usuarios
        List<Usuario> usuarios = getUsuarios();
        // Obtenemos la contrasena hasheada
        String contrasenaHash = hashSHA1(contrasena);

        // Buscamos el usuario con el correo y contrasena
        Usuario usuario = usuarios.stream()
                .filter(user -> user.getCorreo().equals(correo) && user.getContrasena().equals(contrasenaHash))
                .findFirst()
                .orElse(null);

        // Si no se encontro el usuario, retornamos null
        if (usuario == null) {
            return null;
        }

        // Verificamos si el usuario es un operador o repartidor
        if (usuario.getRol() == Rol.OPERADOR) {
            // Obtenemos el operador
            Operador operador = getOperadores().stream()
                    .filter(op -> op.getId() == usuario.getId())
                    .findFirst()
                    .orElse(null);

            // Retornamos el operador
            return operador;
        } else if (usuario.getRol() == Rol.REPARTIDOR) {
            //Obtenemos el repartidor
            Repartidor repartidor = getRepartidores().stream()
                    .filter(rep -> rep.getId() == usuario.getId())
                    .findFirst()
                    .orElse(null);

            //Retornamos el repartidor
            return repartidor;
        }

        // Si no es ni operador ni repartidor, retornamos el usuario
        return usuario;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Metodo para obtener los usuarios
    public static List<Usuario> getUsuarios() {
        String request_url = WSManager.WS_URL + "usuarios.php";

        try {
            List<Usuario> usuarios = new ArrayList<>();

            String result = Request.Get(request_url)
                    .execute().returnContent().asString();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(result);

            if (!Boolean.parseBoolean(jsonResponse.get("OK").toString())) {
                String error = (String) jsonResponse.get("error");
                throw new Exception("Ocurrio un error en la peticion:" + error);

            }

            // Obtenemos la data en caso de que la peticion haya ido bien
            JSONArray jsonData = (JSONArray) jsonResponse.get("data");

            // Agregamos el resultado a la list
            for (Object object : jsonData) {
                JSONObject jsonUser = (JSONObject) object;

                int idUsuario = Integer.parseInt((String) jsonUser.get("idUsuario"));
                String nombre = (String) jsonUser.get("nombre");
                String correo = (String) jsonUser.get("correo");
                String contrasenaHash = (String) jsonUser.get("contrasena");
                String rolString = (String) jsonUser.get("rol");
                boolean activo = ((String) jsonUser.get("activo")).equals("1");

                // Conversion del rol al enum
                Rol userRole;
                try {
                    userRole = Rol.valueOf(rolString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("El rol recibido no coindcide con el enum");
                }

                // Creeamos el usuario para agreaar a la lista
                Usuario usuario = new Usuario(idUsuario, nombre, correo, contrasenaHash, userRole, activo);

                // Lo agregamos a la lista
                usuarios.add(usuario);
            }

            return usuarios;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a los usuarios" + ex.getMessage());
        }
        return null;
    }

    // Metodo para obtener un usuario por su id
    public static Usuario getUsuarioById(int idUsuario) {
        String request_url = WSManager.WS_URL + "usuarios.php?idUsuario=" + idUsuario;

        try {
            String result = Request.Get(request_url)
                    .execute().returnContent().asString();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(result);

            if (!Boolean.parseBoolean(jsonResponse.get("OK").toString())) {
                String error = (String) jsonResponse.get("error");
                throw new Exception("Ocurrio un error en la peticion:" + error);

            }

            // Obtenemos la data en caso de que la peticion haya ido bien
            JSONObject jsonData = (JSONObject) jsonResponse.get("data");

            int userId = Integer.parseInt((String) jsonData.get("idUsuario"));
            String nombre = (String) jsonData.get("nombre");
            String correo = (String) jsonData.get("correo");
            String contrasenaHash = (String) jsonData.get("contrasena");
            String rolString = (String) jsonData.get("rol");
            boolean activo = ((String) jsonData.get("activo")).equals("1");

            // Conversion del rol al enum
            Rol userRole;
            try {
                userRole = Rol.valueOf(rolString.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("El rol recibido no coindcide con el enum");
            }

            // Creeamos el usuario para agreaar a la lista
            Usuario usuario = new Usuario(userId, nombre, correo, contrasenaHash, userRole, activo);

            return usuario;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a los usuarios" + ex.getMessage());
        }
        return null;
    }

    // Metodo para guardar un usuario
    // Retorna el id del usuario guardado
    public static int guardarUsuario(Usuario usuario) {
        String request_url = WSManager.WS_URL + "usuarios.php";

        try {
            // Creamos el objeto JSON a enviar
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("nombre", usuario.getNombre());
            jsonUser.put("correo", usuario.getCorreo());
            jsonUser.put("contrasena", usuario.getContrasena());
            jsonUser.put("rol", usuario.getRol().toString());
            jsonUser.put("activo", usuario.isActivo());

            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonUser.toJSONString(), ContentType.APPLICATION_JSON)
                    .execute().returnContent().asString();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(result);

            if (!Boolean.parseBoolean(jsonResponse.get("OK").toString())) {
                String error = (String) jsonResponse.get("error");
                throw new Exception("Ocurrio un error en la peticion:" + error);
            }

            return Integer.parseInt((String) jsonResponse.get("data"));

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al guardar el usuario" + ex.getMessage());
        }
        return -1;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /// Metodos para manejar los operadores
    // Metodo para obtener los operadores
    public static List<Operador> getOperadores() {
        String request_url = WSManager.WS_URL + "operadores.php";

        try {
            List<Operador> operadores = new ArrayList<>();

            String result = Request.Get(request_url)
                    .execute().returnContent().asString();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(result);

            if (!Boolean.parseBoolean(jsonResponse.get("OK").toString())) {
                String error = (String) jsonResponse.get("error");
                throw new Exception("Ocurrio un error en la peticion:" + error);

            }

            // Obtenemos la data en caso de que la peticion haya ido bien
            JSONArray jsonData = (JSONArray) jsonResponse.get("data");

            // Agregamos el resultado a la list
            for (Object object : jsonData) {
                JSONObject jsonOperador = (JSONObject) object;

                int idOperador = Integer.parseInt((String) jsonOperador.get("idOperador"));
                String nombre_completo = (String) jsonOperador.get("nombre_completo");
                String horario = (String) jsonOperador.get("horario");
                int idUsuario = Integer.parseInt((String) jsonOperador.get("idUsuario"));

                // Obtenemos el usuario relacionado al operador
                Usuario usuario = getUsuarioById(idUsuario);

                // Creeamos el operador para agreaar a la lista
                Operador operador = new Operador(idOperador, idUsuario, horario, usuario.getId(), usuario.getNombre(),
                        usuario.getCorreo(), usuario.getContrasena(), usuario.getRol(), usuario.isActivo(),
                        nombre_completo);

                // Lo agregamos a la lista
                operadores.add(operador);
            }

            return operadores;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a los operadores" + ex.getMessage());
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    /// Metodos para manejar los repartidores
    // Metodo para obtener los repartidores
    public static List<Repartidor> getRepartidores() {
        String request_url = WSManager.WS_URL + "repartidores.php";

        try {
            List<Repartidor> repartidores = new ArrayList<>();

            String result = Request.Get(request_url)
                    .execute().returnContent().asString();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(result);

            if (!Boolean.parseBoolean(jsonResponse.get("OK").toString())) {
                String error = (String) jsonResponse.get("error");
                throw new Exception("Ocurrio un error en la peticion:" + error);

            }

            // Obtenemos la data en caso de que la peticion haya ido bien
            JSONArray jsonData = (JSONArray) jsonResponse.get("data");

            // Agregamos el resultado a la list
            for (Object object : jsonData) {
                JSONObject jsonRepartidor = (JSONObject) object;

                int idRepartidor = Integer.parseInt((String) jsonRepartidor.get("idRepartidor"));
                String nombre_completo = (String) jsonRepartidor.get("nombre_completo");
                String telefono = (String) jsonRepartidor.get("telefono");
                int idUsuario = Integer.parseInt((String) jsonRepartidor.get("idUsuario"));
                
                int idCamion = Integer.parseInt(
                        (String) jsonRepartidor.get("idCamion") == null ? "0" : (String) jsonRepartidor.get("idCamion")
                );

                // Obtenemos el usuario relacionado al operador
                Usuario usuario = getUsuarioById(idUsuario);

                // Creeamos el repartidor par agregarlo a la lista
                Repartidor repartidor = new Repartidor(idRepartidor, telefono, idCamion, usuario.getId(),
                        usuario.getNombre(), usuario.getCorreo(), usuario.getContrasena(), usuario.getRol(),
                        usuario.isActivo(), nombre_completo);

                // Lo agregamos a la lista
                repartidores.add(repartidor);
            }

            return repartidores;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a los repartidores" + ex.getMessage());
        }
        return null;
    }

    // Metodo para guardar un repartidor
    public static int guardarRepartidor(Repartidor repartidor) {
        String request_url = WSManager.WS_URL + "repartidores.php";

        try {
            // Guardamos el usuario primero
            Usuario usuario = new Usuario(repartidor.getNombre(), repartidor.getCorreo(), repartidor.getContrasena(),
                    repartidor.getRol(), repartidor.isActivo());
            
            // Guardamos el usuario
            int idUsuario = guardarUsuario(usuario);
            
            if (idUsuario == -1) {
                throw new Exception("Error al guardar el usuario");
            }

            // Creamos el objeto JSON a enviar
            JSONObject jsonRepartidor = new JSONObject();
            jsonRepartidor.put("nombre_completo", repartidor.getNombre_completo());
            jsonRepartidor.put("telefono", repartidor.getTelefono());
            jsonRepartidor.put("idUsuario", idUsuario);
            jsonRepartidor.put("idCamion", repartidor.getIdCamion());


            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonRepartidor.toJSONString(), ContentType.APPLICATION_JSON)
                    .execute().returnContent().asString();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(result);

            if (!Boolean.parseBoolean(jsonResponse.get("OK").toString())) {
                String error = (String) jsonResponse.get("error");
                throw new Exception("Ocurrio un error en la peticion:" + error);
            }

            return Integer.parseInt((String) jsonResponse.get("data"));

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al guardar el repartidor" + ex.getMessage());
        }
        return -1;
    }

    private static String hashSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            // Convertir el hash a un string hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo generar el hash SHA1", e);
        }
    }

}
