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
            $query = "SELECT * FROM usuario WHERE idUsuario = $id";
            $result = mysqli_query($link, $query);

            if(!(mysqli_num_rows($result) > 0)) {
                $this->close($link);
                throw new Exception("El usuario no existe");
            }
            
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
        public function createUser($nombre, $correo, $contrasena, $rol, $activo = null) {
            $link = $this->open();
        
            // Construcción de la consulta con o sin el campo "activo"
            if ($activo !== null) { // Si el cliente especifica un valor para "activo"
                $activo = $activo ? 1 : 0; // Aseguramos que sea 1 o 0
                $query = "INSERT INTO usuario (nombre, correo, contrasena, rol, activo) 
                          VALUES ('$nombre', '$correo', SHA1('$contrasena'), '$rol', $activo)";
            } else {
                $query = "INSERT INTO usuario (nombre, correo, contrasena, rol) 
                          VALUES ('$nombre', '$correo', SHA1('$contrasena'), '$rol')";
            }
        
            // Ejecución de la consulta
            $result = mysqli_query($link, $query);
            if ($result) {
                // Obtener el usuario recién creado
                $query = "SELECT * FROM usuario WHERE correo = '$correo' AND contrasena = SHA1('$contrasena')";
                $result = mysqli_query($link, $query);
                $user = mysqli_fetch_assoc($result);
                $this->close($link);
                return $user['idUsuario'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar el usuario");
            }
        }

        // Funcion para actualizar un usuario
        public function updateUser($id, $nombre = null, $correo = null, $contrasena = null, $rol = null, $activo = null){
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

            if($activo === null){
                $activo = $user['activo'];
            } else {
                $activo = $activo ? 1 : 0;
            }
            

            $query = "UPDATE usuario SET nombre = '$nombre', correo = '$correo', contrasena = '$contrasena', rol = '$rol', activo = $activo WHERE idUsuario = $id";

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
        public function createOperador($idUsuario, $horario, $nombre_completo) {
            $link = $this->open();
            $query = "INSERT INTO operador (idUsuario, horario, nombre_completo) VALUES ($idUsuario, '$horario', '$nombre_completo')";
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
        public function updateOperador($id, $horario = null, $nombre_completo = null){
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

            $query = "UPDATE operador SET horario = '$horario', nombre_completo = '$nombre_completo' WHERE idOperador = $id";
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
            
            if(!(mysqli_num_rows($result) > 0)) {
                $this->close($link);
                throw new Exception("El camion no existe");
                return;
            }

            // Si hay un resultado o el numero de camiones es mayor a 0
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
        public function createCamion($capacidad_maxima = null, $placas, $modelo, $marca) {
            $link = $this->open();
            $query = "INSERT INTO camion (capacidad_maxima, placas, modelo, marca) VALUES ($capacidad_maxima, '$placas', '$modelo', '$marca')";

            if(is_null($capacidad_maxima)){
                $query = "INSERT INTO camion (placas, modelo, marca) VALUES ('$placas', '$modelo', '$marca')";
            }
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

        // FUNCIONES PARA MANEJO DE LA TABLA CALLE
        // Funcion para obtener todas las calles
        public function getCalles(){
            $link = $this->open();
            $query = "SELECT * FROM calle";
            $result = mysqli_query($link, $query);
            
            if($result){
                $calles = [];
                while($row = mysqli_fetch_assoc($result)){
                    $calles[] = $row;
                }
                $this->close($link);
                
                return $calles;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener las calles");
            }

        }

        // Funcion para obtener una calle por su id
        public function getCalleById($id){
            $link = $this->open();
            $query = "SELECT * FROM calle WHERE idCalle = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $calle = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $calle;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener la calle");
            }

        }

        // Funcion para insertar una calle 
        public function createCalle($nombre, $descripcion = null, $idColonia) {
            $link = $this->open();
            $query = "INSERT INTO calle (nombre, descripcion, idColonia) VALUES ('$nombre', '$descripcion', $idColonia)";

            if(is_null($descripcion)){
                $query = "INSERT INTO calle (nombre, idColonia) VALUES ('$nombre', $idColonia)";
            }

            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos la calle para obtener su id
                $query = "SELECT * FROM calle WHERE nombre = '$nombre'";
                $result = mysqli_query($link, $query);
                $calle = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id de la calle
                return $calle['idCalle'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar la calle");
            }
        }

        // Funcion para actualizar una calle
        public function updateCalle($id, $nombre = null, $descripcion = null) {
            $link = $this->open();

            // Verificamos que la calle exista primero
            $query = "SELECT * FROM calle WHERE idCalle = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("La calle no existe");
            }

            $calle = mysqli_fetch_assoc($result);

            if($nombre == null){
                $nombre = $calle['nombre'];
            }

            if($descripcion == null){
                $descripcion = $calle['descripcion'];
            }

            $query = "UPDATE calle SET nombre = '$nombre', descripcion = '$descripcion' WHERE idCalle = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id de la calle actualizada
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar la calle");
            }

        }


        ////////////////////////////////////////////////////////////////////////////////////////////////

        // FUNCIONES PARA MANEJO DE LA TABLA COLONIA

        // Funcion para obtener todas las colonias
        public function getColonias(){
            $link = $this->open();
            $query = "SELECT * FROM colonia";
            $result = mysqli_query($link, $query);
            
            if($result){
                $colonias = [];
                while($row = mysqli_fetch_assoc($result)){
                    $colonias[] = $row;
                }
                $this->close($link);
                
                return $colonias;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener las colonias");
            }

        }

        // Funcion para obtener una colonia por su id
        public function getColoniaById($id){
            $link = $this->open();
            $query = "SELECT * FROM colonia WHERE idColonia = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $colonia = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $colonia;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener la colonia");
            }

        }

        // Funcion para obtener las colonias por el idZona
        public function getColoniasByZona($idZona){
            $link = $this->open();
            $query = "SELECT * FROM colonia WHERE idZona = $idZona";
            $result = mysqli_query($link, $query);
            
            if($result){
                $colonias = [];
                while($row = mysqli_fetch_assoc($result)){
                    $colonias[] = $row;
                }
                $this->close($link);
                
                return $colonias;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener las colonias");
            }

        }


        // Funcion para insertar una colonia
        public function createColonia($nombre, $idZona) {
            $link = $this->open();
            $query = "INSERT INTO colonia (nombre, idZona) VALUES ('$nombre', $idZona)";
            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos la colonia para obtener su id
                $query = "SELECT * FROM colonia WHERE nombre = '$nombre'";
                $result = mysqli_query($link, $query);
                $colonia = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id de la colonia
                return $colonia['idColonia'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar la colonia");
            }
        }

        // Funcion para actualizar una colonia
        public function updateColonia($id, $nombre = null) {
            $link = $this->open();

            // Verificamos que la colonia exista primero
            $query = "SELECT * FROM colonia WHERE idColonia = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("La colonia no existe");
            }

            $colonia = mysqli_fetch_assoc($result);

            if($nombre == null){
                $nombre = $colonia['nombre'];
            }

            $query = "UPDATE colonia SET nombre = '$nombre' WHERE idColonia = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id de la colonia actualizada
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar la colonia");
            }

        }

        // Funcion para eliminar una colonia
        public function deleteColonia($id){
            $link = $this->open();

            // Verificamos que la colonia exista primero
            $query = "SELECT * FROM colonia WHERE idColonia = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("La colonia no existe");
            }

            $query = "DELETE FROM colonia WHERE idColonia = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se elimine correctamente, retornamos el id de la colonia eliminada
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al eliminar la colonia");
            }

        }


        ////////////////////////////////////////////////////////////////////////////////////////////////

        // FUNCIONES PARA MANEJO DE LA TABLA ZONA

        // Funcion para obtener todas las zonas
        public function getZonas(){
            $link = $this->open();
            $query = "SELECT * FROM zona";
            $result = mysqli_query($link, $query);
            
            if($result){
                $zonas = [];
                while($row = mysqli_fetch_assoc($result)){
                    $zonas[] = $row;
                }
                $this->close($link);
                
                return $zonas;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener las zonas");
            }

        }

        // Funcion para obtener una zona por su id
        public function getZonaById($id){
            $link = $this->open();
            $query = "SELECT * FROM zona WHERE idZona = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $zona = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $zona;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener la zona");
            }

        }

        // Funcion para insertar una zona
        public function createZona($nombre, $coordenadas_x, $coordenadas_y) {
            $link = $this->open();
            $query = "INSERT INTO zona (nombre, coordenadas_x, coordenadas_y) VALUES ('$nombre', '$coordenadas_x', '$coordenadas_y')";
            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos la zona para obtener su id
                $query = "SELECT * FROM zona WHERE nombre = '$nombre'";
                $result = mysqli_query($link, $query);
                $zona = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id de la zona
                return $zona['idZona'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar la zona");
            }
        }

        // Funcion para actualizar una zona
        public function updateZona($id, $nombre = null, $coordenadas_x = null, $coordenadas_y = null) {
            $link = $this->open();

            // Verificamos que la zona exista primero
            $query = "SELECT * FROM zona WHERE idZona = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("La zona no existe");
            }

            $zona = mysqli_fetch_assoc($result);

            if($nombre == null){
                $nombre = $zona['nombre'];
            }

            if($coordenadas_x == null){
                $coordenadas_x = $zona['coordenadas_x'];
            }

            if($coordenadas_y == null){
                $coordenadas_y = $zona['coordenadas_y'];
            }

            $query = "UPDATE zona SET nombre = '$nombre', coordenadas_x = '$coordenadas_x', coordenadas_y = '$coordenadas_y' WHERE idZona = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id de la zona actualizada
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar la zona");
            }

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////

        // FUNCIONES PARA MANEJO DE LA TABLA PEDIDO
        // Funcion para obtener todos los pedidos

        public function getPedidos(){
            $link = $this->open();
            $query = "SELECT * FROM pedido";
            $result = mysqli_query($link, $query);
            
            if($result){
                $pedidos = [];
                while($row = mysqli_fetch_assoc($result)){
                    $pedidos[] = $row;
                }
                $this->close($link);
                
                return $pedidos;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener los pedidos");
            }

        }

        // Funcion para obtener un pedido por su id
        public function getPedidoById($id){
            $link = $this->open();
            $query = "SELECT * FROM pedido WHERE idPedido = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $pedido = mysqli_fetch_assoc($result);
                $this->close($link);
                
                return $pedido;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener el pedido");
            }

        }

        // Funcion para obtener los pedidos por id de la Zona

        public function getPedidosByZona($idZona){
            $link = $this->open();
            $query = "SELECT * FROM pedido WHERE idZona = $idZona";
            $result = mysqli_query($link, $query);
            
            if($result){
                $pedidos = [];
                while($row = mysqli_fetch_assoc($result)){
                    $pedidos[] = $row;
                }
                $this->close($link);
                
                return $pedidos;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener los pedidos");
            }

        }

        // Funcion para obtener pedidos por id del Repartidor

        public function getPedidosByRepartidor($idRepartidor){
            $link = $this->open();
            $query = "SELECT * FROM pedido WHERE idRepartidor = $idRepartidor";
            $result = mysqli_query($link, $query);
            
            if($result){
                $pedidos = [];
                while($row = mysqli_fetch_assoc($result)){
                    $pedidos[] = $row;
                }
                $this->close($link);
                
                return $pedidos;
            } else {
                $this->close($link);
                throw new Exception("Error al obtener los pedidos");
            }

        }

        // Funcion para insertar un pedido
        public function createPedido($idZona, $cantidad_garrafones, $fecha = null, $estado, $prioridad, $idRepartidor = null, $idOperador = null) {
            $link = $this->open();

            $idRepartidor = is_null($idRepartidor) ? 'NULL' : $idRepartidor;
            $idOperador = is_null($idOperador) ? 'NULL' : $idOperador;

            $query = "INSERT INTO pedido (idZona, cantidad_garrafones, fecha, estado, prioridad, idRepartidor, idOperador) VALUES ($idZona, $cantidad_garrafones, '$fecha', '$estado', $prioridad, $idRepartidor, $idOperador)";
            
            
            // Manejo de NULLS
            if ($fecha == null) {
                $query = "INSERT INTO pedido (idZona, cantidad_garrafones, estado, prioridad, idRepartidor, idOperador) VALUES ($idZona, $cantidad_garrafones, '$estado', $prioridad, $idRepartidor, $idOperador)";  
            }

            $result = mysqli_query($link, $query);
            
            if($result){
                // En caso de que lo haya creado, buscamos el pedido para obtener su id
                $query = "SELECT * FROM pedido WHERE idZona = $idZona AND cantidad_garrafones = $cantidad_garrafones AND fecha = '$fecha' AND estado = '$estado' AND prioridad = $prioridad AND idRepartidor = $idRepartidor AND idOperador = $idOperador";
                $result = mysqli_query($link, $query);
                $pedido = mysqli_fetch_assoc($result);
                $this->close($link);

                // Retornamos el id del pedido
                return $pedido['idPedido'];
            } else {
                $this->close($link);
                throw new Exception("Error al insertar el pedido");
            }

        }

        // Funcion para actualizar un pedido
        public function updatePedido($id, $idZona = null, $cantidad_garrafones = null, $fecha = null, $estado = null, $prioridad = null, $idRepartidor = null) {
            $link = $this->open();

            // Verificamos que el pedido exista primero
            $query = "SELECT * FROM pedido WHERE idPedido = $id";
            $result = mysqli_query($link, $query);

            if(mysqli_num_rows($result) == 0){
                $this->close($link);
                throw new Exception("El pedido no existe");
            }

            $pedido = mysqli_fetch_assoc($result);

            if($idZona == null){
                $idZona = $pedido['idZona'];
            }

            if($cantidad_garrafones == null){
                $cantidad_garrafones = $pedido['cantidad_garrafones'];
            }

            if($fecha == null){
                $fecha = $pedido['fecha'];
            }

            if($estado == null){
                $estado = $pedido['estado'];
            }

            if($prioridad == null){
                $prioridad = $pedido['prioridad'];
            }

            if($idRepartidor == null){
                $idRepartidor = $pedido['idRepartidor'];
            }

            // if($idOperador == null){
            //     $idOperador = $pedido['idOperador'];
            // }

            $query = "UPDATE pedido SET idZona = $idZona, cantidad_garrafones = $cantidad_garrafones, fecha = '$fecha', estado = '$estado', prioridad = $prioridad, idRepartidor = $idRepartidor WHERE idPedido = $id";
            $result = mysqli_query($link, $query);
            
            if($result){
                $this->close($link);
                
                // En caso de que se actualice correctamente, retornamos el id del pedido actualizado
                return $id;
            } else {
                $this->close($link);
                throw new Exception("Error al actualizar el pedido");
            }

        }


    }


?>