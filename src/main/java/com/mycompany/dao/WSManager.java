/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.mycompany.domain.Calle;
import com.mycompany.domain.Camion;
import com.mycompany.domain.Colonia;
import com.mycompany.domain.EstadoPedido;
import com.mycompany.domain.Operador;
import com.mycompany.domain.Pedido;
import com.mycompany.domain.Repartidor;
import com.mycompany.domain.Rol;
import com.mycompany.domain.Usuario;
import com.mycompany.domain.Zona;

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
            //jsonUser.put("activo", usuario.isActivo());

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

    // Metodo para guardar un operador
    public static int guardarOperador(Operador operador) {
        String request_url = WSManager.WS_URL + "operadores.php";

        try {
            // Guardamos el usuario primero
            Usuario usuario = new Usuario(operador.getNombre(), operador.getCorreo(), operador.getContrasena(),
                    operador.getRol());
            int idUsuario = guardarUsuario(usuario);

            if (idUsuario == -1) {
                throw new Exception("Error al guardar el usuario");
            }

            // Creamos el objeto JSON a enviar
            JSONObject jsonOperador = new JSONObject();
            jsonOperador.put("nombre_completo", operador.getNombre_completo());
            jsonOperador.put("horario", operador.getHorario());
            jsonOperador.put("idUsuario", idUsuario);

            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonOperador.toJSONString(), ContentType.APPLICATION_JSON)
                    .execute().returnContent().asString();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(result);
            
            System.out.println("JSON RESPONSE:" + jsonResponse.toString());

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
            System.out.println("Error al guardar el operador" + ex.getMessage());
        }
        return -1;
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
                    repartidor.getRol());
            
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
            //jsonRepartidor.put("idCamion", repartidor.getIdCamion());


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

    ///////////////////////////////////////////////////////////////////////////
    /// Metodos para manejar los camiones
    // Metodo para obtener los camiones
    public static List<Camion> getCamiones() {
        String request_url = WSManager.WS_URL + "camiones.php";

        try {
            List<Camion> camiones = new ArrayList<>();

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
                JSONObject jsonCamion = (JSONObject) object;

                int idCamion = Integer.parseInt((String) jsonCamion.get("idCamion"));
                String placas = (String) jsonCamion.get("placas");
                String marca = (String) jsonCamion.get("marca");
                String modelo = (String) jsonCamion.get("modelo");
                int capacidad = Integer.parseInt((String) jsonCamion.get("capacidad"));

                // Creeamos el camion para agreaar a la lista
                Camion camion = new Camion(idCamion, capacidad, placas, modelo, marca);

                // Lo agregamos a la lista
                camiones.add(camion);
            }

            return camiones;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a los camiones" + ex.getMessage());
        }
        return null;
    }

    // Metodo para guardar un camion
    public static int guardarCamion(Camion camion) {
        String request_url = WSManager.WS_URL + "camiones.php";

        try {
            // Creamos el objeto JSON a enviar
            JSONObject jsonCamion = new JSONObject();
            jsonCamion.put("capacidad", camion.getCapacidad());
            jsonCamion.put("placas", camion.getPlacas());
            jsonCamion.put("marca", camion.getMarca());
            jsonCamion.put("modelo", camion.getModelo());

            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonCamion.toJSONString(), ContentType.APPLICATION_JSON)
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
            System.out.println("Error al guardar el camion" + ex.getMessage());
        }
        return -1;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    
    // Metodo para obtener las zonas
    public static List<Zona> getZonas() {
        String request_url = WSManager.WS_URL + "zonas.php";

        try {
            List<Zona> zonas = new ArrayList<>();

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
                JSONObject jsonZona = (JSONObject) object;

                int idZona = Integer.parseInt((String) jsonZona.get("idZona"));
                String nombre = (String) jsonZona.get("nombre");
                String coordenadas_x = (String) jsonZona.get("coordenadas_x");
                String coordenadas_y = (String) jsonZona.get("coordenadas_y");

                // Creeamos la zona para agreaar a la lista
                Zona zona = new Zona(idZona, nombre, coordenadas_x, coordenadas_y);

                // Lo agregamos a la lista
                zonas.add(zona);
            }

            return zonas;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a las zonas" + ex.getMessage());
        }
        return null;
    }

    // Metodo para guardar una zona
    public static int guardarZona(Zona zona) {
        String request_url = WSManager.WS_URL + "zonas.php";

        try {
            // Creamos el objeto JSON a enviar
            JSONObject jsonZona = new JSONObject();
            jsonZona.put("nombre", zona.getNombre());
            jsonZona.put("coordenadas_x", zona.getCoordenadas_x());
            jsonZona.put("coordenadas_y", zona.getCoordenadas_y());

            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonZona.toJSONString(), ContentType.APPLICATION_JSON)
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
            System.out.println("Error al guardar la zona" + ex.getMessage());
        }
        return -1;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /// Metodos para manejar las calles

    // Metodo para obtener las calles
    public static List<Calle> getCalles() {
        String request_url = WSManager.WS_URL + "calles.php";

        try {
            List<Calle> calles = new ArrayList<>();

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
                JSONObject jsonCalle = (JSONObject) object;

                int idCalle = Integer.parseInt((String) jsonCalle.get("idCalle"));
                int idColonia = Integer.parseInt((String) jsonCalle.get("idColonia"));
                String nombre = (String) jsonCalle.get("nombre");
                String descripcion = (String) jsonCalle.get("descripcion");

                // Creeamos la calle para agreaar a la lista
                Calle calle = new Calle(idCalle, idColonia, nombre, descripcion);

                // Lo agregamos a la lista
                calles.add(calle);
            }

            return calles;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a las calles" + ex.getMessage());
        }
        return null;
    }

    // Metodo para obtener las calles de una colonia
    public static List<Calle> getCallesByColonia(int idColonia) {
        String request_url = WSManager.WS_URL + "calles.php?idColonia=" + idColonia;

        try {
            List<Calle> calles = new ArrayList<>();

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
                JSONObject jsonCalle = (JSONObject) object;

                int idCalle = Integer.parseInt((String) jsonCalle.get("idCalle"));
                String nombre = (String) jsonCalle.get("nombre");
                String descripcion = (String) jsonCalle.get("descripcion");

                // Creeamos la calle para agreaar a la lista
                Calle calle = new Calle(idCalle, idColonia, nombre, descripcion);

                // Lo agregamos a la lista
                calles.add(calle);
            }

            return calles;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a las calles" + ex.getMessage());
        }
        return null;
    }

    // Metodo para guardar una calle
    public static int guardarCalle(Calle calle) {
        String request_url = WSManager.WS_URL + "calles.php";

        try {
            // Creamos el objeto JSON a enviar
            JSONObject jsonCalle = new JSONObject();
            jsonCalle.put("idColonia", calle.getIdColonia());
            jsonCalle.put("nombre", calle.getNombre());
            jsonCalle.put("descripcion", calle.getDescripcion());

            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonCalle.toJSONString(), ContentType.APPLICATION_JSON)
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
            System.out.println("Error al guardar la calle" + ex.getMessage());
        }
        return -1;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /// Metodos para manejar las colonias
    
    // Metodo para obtener las colonias
    public static List<Colonia> getColonias() {
        String request_url = WSManager.WS_URL + "colonias.php";

        try {
            List<Colonia> colonias = new ArrayList<>();


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
                JSONObject jsonColonia = (JSONObject) object;

                int idColonia = Integer.parseInt((String) jsonColonia.get("idColonia"));
                String nombre = (String) jsonColonia.get("nombre");
                int idZona = Integer.parseInt((String) jsonColonia.get("idZona"));

                // Obtenemos las calles de la colonia
                ArrayList<Calle> calles = (ArrayList<Calle>) getCallesByColonia(idColonia);

                // Creeamos la colon para agreaar a la lista
                Colonia colonia = new Colonia(idColonia, nombre, calles, idZona);

                // Lo agregamos a la lista
                colonias.add(colonia);
            }

            return colonias;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a las colonias" + ex.getMessage());
        }
        return null;
    }

    // Metodo para obtener las colonias de una zona
    public static List<Colonia> getColoniasByZona(int idZona) {
        String request_url = WSManager.WS_URL + "colonias.php?idZona=" + idZona;

        try {
            List<Colonia> colonias = new ArrayList<>();


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
                JSONObject jsonColonia = (JSONObject) object;

                int idColonia = Integer.parseInt((String) jsonColonia.get("idColonia"));
                String nombre = (String) jsonColonia.get("nombre");

                // Obtenemos las calles de la colonia
                ArrayList<Calle> calles = (ArrayList<Calle>) getCallesByColonia(idColonia);

                // Creeamos la colon para agreaar a la lista
                Colonia colonia = new Colonia(idColonia, nombre, calles, idZona);

                // Lo agregamos a la lista
                colonias.add(colonia);
            }

            return colonias;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a las colonias" + ex.getMessage());
        }
        return null;
    }

    // Metodo para guardar una colonia
    public static int guardarColonia(Colonia colonia) {
        String request_url = WSManager.WS_URL + "colonias.php";

        try {
            // Creamos el objeto JSON a enviar
            JSONObject jsonColonia = new JSONObject();
            jsonColonia.put("nombre", colonia.getNombre());
            jsonColonia.put("idZona", colonia.getIdZona());

            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonColonia.toJSONString(), ContentType.APPLICATION_JSON)
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
            System.out.println("Error al guardar la colonia" + ex.getMessage());
        }
        return -1;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Metodos para manejar los pedidos
    
    // Metodo para obtener los pedidos
    public static List<Pedido> getPedidos() {
        String request_url = WSManager.WS_URL + "pedidos.php";

        try {
            List<Pedido> pedidos = new ArrayList<>();

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
                JSONObject jsonPedido = (JSONObject) object;

                int idPedido = Integer.parseInt((String) jsonPedido.get("idPedido"));
                int idZona = Integer.parseInt((String) jsonPedido.get("idZona"));
                int cantidadGarrafones = Integer.parseInt((String) jsonPedido.get("cantidad_garrafones"));
                String fecha = (String) jsonPedido.get("fecha");
                String fechaEntregaString = (String) jsonPedido.get("fecha_entrega");
                // Convertimos la fecha a un objeto Date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                LocalDate localDate = LocalDate.parse(fecha, formatter);
                LocalDate localDateEntrega = LocalDate.parse(fechaEntregaString, formatter);
                
                Date date = Date.valueOf(localDate);
                Date dateEntrega = Date.valueOf(localDateEntrega);

                String estado = (String) jsonPedido.get("estado"); // ENUM en la BD

                EstadoPedido estadoPedido;
                try {
                    estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("El estado recibido no coindcide con el enum");
                }

                // Prioridad del pedido (boolean)
                boolean prioridad = ((String) jsonPedido.get("prioridad")).equals("1");
                int idRepartidor = Integer.parseInt((String) jsonPedido.get("idRepartidor"));
                int idOperador = Integer.parseInt((String) jsonPedido.get("idOperador"));

                // Creeamos el pedido para agreaar a la lista
                Pedido pedido = new Pedido(idPedido, idZona, cantidadGarrafones, date, estadoPedido, prioridad,
                        idRepartidor, idOperador, dateEntrega);

                // Lo agregamos a la lista
                pedidos.add(pedido);
            }

            return pedidos;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a los pedidos" + ex.getMessage());
        }
        return null;
    }

    // Metodo para obtener los pedidos de un repartidor
    public static List<Pedido> getPedidosByRepartidor(int idRepartidor) {
        String request_url = WSManager.WS_URL + "pedidos.php?idRepartidor=" + idRepartidor;

        try {
            List<Pedido> pedidos = new ArrayList<>();

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
                JSONObject jsonPedido = (JSONObject) object;

                int idPedido = Integer.parseInt((String) jsonPedido.get("idPedido"));
                int idZona = Integer.parseInt((String) jsonPedido.get("idZona"));
                int cantidadGarrafones = Integer.parseInt((String) jsonPedido.get("cantidad_garrafones"));
                String fecha = (String) jsonPedido.get("fecha");
                String fechaEntrega = (String) jsonPedido.get("fecha_entrega");
                // Convertimos la fecha a un objeto Date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                LocalDate localDate = LocalDate.parse(fecha, formatter);
                LocalDate localDateEntrega = LocalDate.parse(fechaEntrega, formatter);
                
                Date date = Date.valueOf(localDate);
                Date dateEntrega = Date.valueOf(localDateEntrega);

                String estado = (String) jsonPedido.get("estado"); // ENUM en la BD

                EstadoPedido estadoPedido;
                try {
                    estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("El estado recibido no coindcide con el enum");
                }

                // Prioridad del pedido (boolean)
                boolean prioridad = ((String) jsonPedido.get("prioridad")).equals("1");
                int idOperador = Integer.parseInt((String) jsonPedido.get("idOperador"));

                // Creeamos el pedido para agreaar a la lista
                Pedido pedido = new Pedido(idPedido, idZona, cantidadGarrafones, date, estadoPedido, prioridad,
                        idRepartidor, idOperador, dateEntrega);

                // Lo agregamos a la lista
                pedidos.add(pedido);
            }

            return pedidos;

        } catch (IOException e) {
            System.out.println("IO ERROR en la solicitud: " + e.getMessage());
        } catch (ParseException ex) {
            System.out.println("Error al parsear el json response:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al obtener a los pedidos" + ex.getMessage());
        }
        return null;
    }

    // Metodo para guardar un pedido
    public static int guardarPedido(Pedido pedido) {
        String request_url = WSManager.WS_URL + "pedidos.php";

        try {
            // Creamos el objeto JSON a enviar
            JSONObject jsonPedido = new JSONObject();
            // Campos obligatorios para crear el pedido
            jsonPedido.put("idZona", pedido.getIdZona());
            jsonPedido.put("cantidad_garrafones", pedido.getCantidadGarrafones());
            jsonPedido.put("idOperador", pedido.getIdOperador());

            // Campos opcionales en caso de que sean nulos o vacios no se envian
            if (pedido.getFecha() != null) {
                jsonPedido.put("fecha", pedido.getFecha().toString());
            }
            if (pedido.getEstado() != null) {
                jsonPedido.put("estado", pedido.getEstado().toString());
            }
            if (pedido.isPrioridad()) {
                jsonPedido.put("prioridad", "1");
            }
            if (pedido.getIdRepartidor() != 0) {
                jsonPedido.put("idRepartidor", pedido.getIdRepartidor());
            }
            if (pedido.getFecha_entrega() != null) {
                jsonPedido.put("fecha_entrega", pedido.getFecha_entrega().toString());
            }


            // Creamos el request
            String result = Request.Post(request_url)
                    .bodyString(jsonPedido.toJSONString(), ContentType.APPLICATION_JSON)
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
            System.out.println("Error al guardar el pedido" + ex.getMessage());
        }
        return -1;
    }

    // Metodo para actualizar un pedido
    public static int actualizarPedido(Pedido pedido) {
        String request_url = WSManager.WS_URL + "pedidos.php";

        try {
            // Creamos el objeto JSON a enviar
            JSONObject jsonPedido = new JSONObject();
            // Campos obligatorios para crear el pedido
            jsonPedido.put("idPedido", pedido.getIdPedido());
            jsonPedido.put("idZona", pedido.getIdZona());
            jsonPedido.put("cantidad_garrafones", pedido.getCantidadGarrafones());
            jsonPedido.put("idOperador", pedido.getIdOperador());

            // Campos opcionales en caso de que sean nulos o vacios no se envian
            if (pedido.getFecha() != null) {
                jsonPedido.put("fecha", pedido.getFecha().toString());
            }
            if (pedido.getEstado() != null) {
                jsonPedido.put("estado", pedido.getEstado().toString());
            }
            if (pedido.isPrioridad()) {
                jsonPedido.put("prioridad", "1");
            }
            if (pedido.getIdRepartidor() != 0) {
                jsonPedido.put("idRepartidor", pedido.getIdRepartidor());
            }
            if (pedido.getFecha_entrega() != null) {
                jsonPedido.put("fecha_entrega", pedido.getFecha_entrega().toString());
            }


            // Creamos el request
            String result = Request.Put(request_url)
                    .bodyString(jsonPedido.toJSONString(), ContentType.APPLICATION_JSON)
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
            System.out.println("Error al actualizar el pedido" + ex.getMessage());
        }
        return -1;
    }       

    //////////////////////////////////////////////////////////////////////////////////////////////


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
