<?php

    require_once("DBManager.php");

    $db = new DBManager();

    header('Content-Type: application/json');  

    // Swithc para cada uno de los metodos

    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['id'])){
                    $id = $_GET['id'];
                    $user = $db->getUserById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Usuario obtenido correctamente',
                        'data' => $user
                    ]);
                } else {
                    $users = $db->getUsers();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Usuarios obtenidos correctamente',
                        'data' => $users
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['nombre']) || !isset($data['correo']) || !isset($data['contrasena']) || !isset($data['rol'])) {
                    throw new Exception("Faltan datos");
                }

                $nombre = $data['nombre'];
                $correo = $data['correo'];
                $contrasena = $data['contrasena'];
                $rol = $data['rol'];

                $user = $db->createUser($nombre, $correo, $contrasena, $rol);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Usuario creado correctamente',
                    'data' => $user
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idUsuario'])) {
                    throw new Exception("Faltan el id del usuario a actualizar");
                }

                $idUsuario = $data['idUsuario'];
                $nombre = $data['nombre'] ?? null;
                $correo = $data['correo'] ?? null;
                $contrasena = $data['contrasena'] ?? null;
                $rol = $data['rol'] ?? null;

                $user = $db->updateUser($idUsuario, $nombre, $correo, $contrasena, $rol);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Usuario actualizado correctamente',
                    'data' => $user
                ]);
                
                break;
            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);
                //$user = $db->deleteUser($data);
                echo json_encode($user);
                break;
        }
    } catch (Exception $e) {
        // Retornamos respuesta con formato JSON
        echo json_encode([
            'OK' => false,
            'error' => $e->getMessage()
        ]);
    }

?>