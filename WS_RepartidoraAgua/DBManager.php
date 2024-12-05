<?php

    class DBManager {
        private $db;
        private $host;
        private $user;
        private $pass;
        private $port;

        public function __construct() {
            $this->db = "repartidora_agua";
            $this->host = "localhost";
            $this->user = "root";
            $this->pass = null;
            $this->port = 3306;
        }

        public function open(){
            $link = mysqli_connect(
                $this->host, $this->user, $this->pass, $this->db, $this->port
            ) or die("Error al abrir la conexion a la DB");
            return $link;
        }

        public function close($link){
            mysqli_close($link);
        }

        // FUNCIONES PARA MANEJO DE LA TABLA USUARIOS
        // Funcion para obtener todos los usuarios
        public function getUsers(){
            $link = $this->open();
            $query = "SELECT * FROM usuario";
            $result = mysqli_query($link, $query);
            
            if($result){
                $users = [];
                while($row = mysqli_fetch_assoc($result)){
                    $users[] = $row;
                }
                $this->close($link);
                
                return $users;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener los usuarios");
            }

        }

        // Funcion para obtener un usuario por su id
        public function getUserById($id){
            $link = $this->open();
            $query = "SELECT * FROM usuario WHERE id = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $user = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $user;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener el usuario");
            }

        }

        // Funcion para insertar un usuario
        public function createUser($nombre, $correo, $contrasena, $rol){
            $link = $this->open();
            $query = "INSERT INTO usuario (nombre, correo, contrasena, rol) VALUES ('$nombre', '$correo', SHA1('$contrasena'), '$rol')";
            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos el usuario para obtener su id
                $query = "SELECT * FROM usuario WHERE correo = '$correo' AND contrasena = SHA1('$contrasena')";
                $result = mysqli_query($link, $query);
                $user = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id del usuario
                return $user['id'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar el usuario");
            }

        }

        // Funcion para actualizar un usuario
        public function updateUser($id, $nombre = null, $correo = null, $contrasena = null, $rol = null){
            $link = $this->open();

            // Verificamos que el usuario exista primero
            $query = "SELECT * FROM usuario WHERE id = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("El usuario no existe");
            }

            $user = mysqli_fetch_assoc($result);

            // Seteamos los datos null con el valor actual
            if($nombre == null){
                $nombre = $user['nombre'];
            }

            if($correo == null){
                $correo = $user['correo'];
            }

            if($contrasena == null){
                $contrasena = $user['contrasena'];
            } else {
                $contrasena = sha1($contrasena);
            }

            if($rol == null){
                $rol = $user['rol'];
            }
            

            $query = "UPDATE usuario SET nombre = '$nombre', correo = '$correo', contrasena = '$contrasena', rol = '$rol' WHERE id = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id del usuario actualizado
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar el usuario");
            }

        }
        


        ////////////////////////////////////////////////////////////////////////////////////////////////



    }


?>