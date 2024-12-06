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
                return $user['idUsuario'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar el usuario");
            }

        }

        // Funcion para actualizar un usuario
        public function updateUser($id, $nombre = null, $correo = null, $contrasena = null, $rol = null){
            $link = $this->open();

            // Verificamos que el usuario exista primero
            $query = "SELECT * FROM usuario WHERE idUsuario = $id";
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
            

            $query = "UPDATE usuario SET nombre = '$nombre', correo = '$correo', contrasena = '$contrasena', rol = '$rol' WHERE idUsuario = $id";
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

        // FUNCIONES PARA MANEJO DE LA TABLA OPERADOR
        // Funcion para obtener todos los operadores
        public function getOperadores(){
            $link = $this->open();
            $query = "SELECT * FROM operador";
            $result = mysqli_query($link, $query);
            
            if($result){
                $operadores = [];
                while($row = mysqli_fetch_assoc($result)){
                    $operadores[] = $row;
                }
                $this->close($link);
                
                return $operadores;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener los operadores");
            }

        }

        // Funcion para obtener un operador por su id
        public function getOperadorById($id){
            $link = $this->open();
            $query = "SELECT * FROM operador WHERE idOperador = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $operador = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $operador;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener el operador");
            }

        }

        // Funcion para insertar un operador
        public function createOperador($idUsuario, $horario) {
            $link = $this->open();
            $query = "INSERT INTO operador (idUsuario, horario) VALUES ($idUsuario, '$horario')";
            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos el operador para obtener su id
                $query = "SELECT * FROM operador WHERE idUsuario = $idUsuario";
                $result = mysqli_query($link, $query);
                $operador = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id del operador
                return $operador['idOperador'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar el operador");
            }

        }

        // Funcion para actualizar un operador
        public function updateOperador($id, $horario = null){
            $link = $this->open();

            // Verificamos que el operador exista primero
            $query = "SELECT * FROM operador WHERE idOperador = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("El operador no existe");
            }

            $operador = mysqli_fetch_assoc($result);

            if($horario == null){
                $horario = $operador['horario'];
            }

            $query = "UPDATE operador SET horario = '$horario' WHERE idOperador = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id del operador actualizado
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar el operador");
            }

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////

        // FUNCIONES PARA MANEJO DE LA TABLA REPARTIDOR
        // Funcion para obtener todos los repartidores
        public function getRepartidores(){
            $link = $this->open();
            $query = "SELECT * FROM repartidor";
            $result = mysqli_query($link, $query);
            
            if($result){
                $repartidores = [];
                while($row = mysqli_fetch_assoc($result)){
                    $repartidores[] = $row;
                }
                $this->close($link);
                
                return $repartidores;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener los repartidores");
            }

        }

        // Funcion para obtener un repartidor por su id
        public function getRepartidorById($id){
            $link = $this->open();
            $query = "SELECT * FROM repartidor WHERE idRepartidor = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $repartidor = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $repartidor;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener el repartidor");
            }

        }

        // Funcion para obtener un repartidor por id de camion
        public function getRepartidorByCamion($idCamion){
            $link = $this->open();
            $query = "SELECT * FROM repartidor WHERE idCamion = $idCamion";
            $result = mysqli_query($link, $query);
            
            if($result){
                $repartidor = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $repartidor;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener el repartidor");
            }

        }

        // Funcion para obtener repartidor por id de usuario
        public function getRepartidorByUsuario($idUsuario){
            $link = $this->open();
            $query = "SELECT * FROM repartidor WHERE idUsuario = $idUsuario";
            $result = mysqli_query($link, $query);
            
            if($result){
                $repartidor = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $repartidor;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener el repartidor");
            }

        }

        // Funcion para insertar un repartidor
        public function createRepartidor($nombre_completo, $telefono, $idCamion = null, $idUsuario) {
            $link = $this->open();

            // Manejo de NULL en la consulta
            $idCamion = is_null($idCamion) ? 'NULL' : $idCamion;

            $query = "INSERT INTO repartidor (nombre_completo, telefono, idCamion, idUsuario) VALUES ('$nombre_completo', '$telefono', $idCamion, $idUsuario)";
            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos el repartidor para obtener su id
                $query = "SELECT * FROM repartidor WHERE idUsuario = $idUsuario";
                $result = mysqli_query($link, $query);
                $repartidor = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id del repartidor
                return $repartidor['idRepartidor'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar el repartidor");
            }
        }

        // Funcion para actualizar un repartidor
        public function updateRepartidor($id, $nombre_completo = null, $telefono = null, $idCamion = null){
            $link = $this->open();

            // Verificamos que el repartidor exista primero
            $query = "SELECT * FROM repartidor WHERE idRepartidor = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("El repartidor no existe");
            }

            $repartidor = mysqli_fetch_assoc($result);

            if($nombre_completo == null){
                $nombre_completo = $repartidor['nombre_completo'];
            }

            if($telefono == null){
                $telefono = $repartidor['telefono'];
            }

            if($idCamion == null){
                $idCamion = $repartidor['idCamion'] ?? 'NULL';
            }

            $query = "UPDATE repartidor SET nombre_completo = '$nombre_completo', telefono = '$telefono', idCamion = $idCamion WHERE idRepartidor = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id del repartidor actualizado
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar el repartidor");
            }

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////

        // FUNCIONES PARA MANEJO DE LA TABLA CAMION

        // Funcion para obtener todos los camiones
        public function getCamiones(){
            $link = $this->open();
            $query = "SELECT * FROM camion";
            $result = mysqli_query($link, $query);
            
            if($result){
                $camiones = [];
                while($row = mysqli_fetch_assoc($result)){
                    $camiones[] = $row;
                }
                $this->close($link);
                
                return $camiones;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener los camiones");
            }

        }

        // Funcion para obtener un camion por su id
        public function getCamionById($id){
            $link = $this->open();
            $query = "SELECT * FROM camion WHERE idCamion = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $camion = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $camion;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener el camion");
            }

        }


        // Funcion para insertar un camion
        public function createCamion($capacidad_maxima, $placas, $modelo, $marca) {
            $link = $this->open();
            $query = "INSERT INTO camion (capacidad_maxima, placas, modelo, marca) VALUES ($capacidad_maxima, '$placas', '$modelo', '$marca')";
            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos el camion para obtener su id
                $query = "SELECT * FROM camion WHERE placas = '$placas'";
                $result = mysqli_query($link, $query);
                $camion = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id del camion
                return $camion['idCamion'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar el camion");
            }
        }

        // Funcion para actualizar un camion
        public function updateCamion($id, $placas = null, $modelo = null, $marca = null) {
            $link = $this->open();

            // Verificamos que el camion exista primero
            $query = "SELECT * FROM camion WHERE idCamion = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("El camion no existe");
            }

            $camion = mysqli_fetch_assoc($result);

            if($placas == null){
                $placas = $camion['placas'];
            }

            if($modelo == null){
                $modelo = $camion['modelo'];
            }

            if($marca == null){
                $marca = $camion['marca'];
            }

            $query = "UPDATE camion SET placas = '$placas', modelo = '$modelo', marca = '$marca' WHERE idCamion = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id del camion actualizado
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar el camion");
            }

        } 

        // Funcion para eliminar un camion
        public function deleteCamion($id){
            $link = $this->open();

            // Verificamos que el camion exista primero
            $query = "SELECT * FROM camion WHERE idCamion = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("El camion no existe");
            }

            $query = "DELETE FROM camion WHERE idCamion = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se elimine correctamente, retornamos el id del camion eliminado
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al eliminar el camion");
            }

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////




    }


?>