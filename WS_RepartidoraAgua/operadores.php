<?php

    require_once "DBManager.php";

    $db = new DBManager();

    header('Content-Type: application/json');

    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['idOperador'])){
                    $id = $_GET['idOperador'];
                    $operador = $db->getOperadorById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Operador obtenido correctamente',
                        'data' => $operador
                    ]);
                } else {
                    $operadores = $db->getOperadores();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Operadores obtenidos correctamente',
                        'data' => $operadores
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idUsuario']) || !isset($data['horario']) || !isset($data['nombre_completo'])) {
                    throw new Exception("Faltan datos");
                }

                $idUsuario = $data['idUsuario'];
                $horario = $data['horario'];
                $nombre_completo = $data['nombre_completo'];

                $operador = $db->createOperador($idUsuario, $horario, $nombre_completo);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Operador creado correctamente',
                    'data' => $operador
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idOperador'])) {
                    throw new Exception("Faltan el id del operador a actualizar");
                }

                $idOperador = $data['idOperador'];
                $horario = $data['horario'] ?? null;

                $user = $db->updateOperador($idOperador, $horario);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Operador actualizado correctamente',
                    'data' => $user
                ]);
                break;
            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idUsuario'])) {
                    throw new Exception("Faltan el id del usuario a eliminar");
                }

                $idUsuario = $data['idUsuario'];
                //$user = $db->deleteUser($idUsuario);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Operador eliminado correctamente',
                    'data' => $user
                ]);
                break;
        }
    } catch (Exception $e) {
        echo json_encode([
            'OK' => false,
            'message' => $e->getMessage()
        ]);
    }


?>