<?php

    require_once 'DBManager.php';

    $db = new DBManager();

    header('Content-Type: application/json');

    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['idRepartidor'])){
                    $id = $_GET['idRepartidor'];
                    $repartidor = $db->getRepartidorById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Repartidor obtenido correctamente',
                        'data' => $repartidor
                    ]);
                } else if (isset($_GET['idCamion'])) {
                    $idCamion = $_GET['idCamion'];
                    $repartidor = $db->getRepartidorByCamion($idCamion);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Repartidor obtenido correctamente',
                        'data' => $repartidor
                    ]);
                } else if (isset($_GET['idUsuario'])) {
                    $idUsuario = $_GET['idUsuario'];
                    $repartidor = $db->getRepartidorByUsuario($idUsuario);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Repartidor obtenido correctamente',
                        'data' => $repartidor
                    ]);
                } else {
                    $repartidores = $db->getRepartidores();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Repartidores obtenidos correctamente',
                        'data' => $repartidores
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['nombre_completo']) || !isset($data['telefono']) || !isset($data['idUsuario'])) {
                    throw new Exception("Faltan datos");
                }

                $nombre_completo = $data['nombre_completo'];
                $telefono = $data['telefono'];
                $idUsuario = $data['idUsuario'];
                $idCamion = $data['idCamion'] ?? null;

                $repartidor = $db->createRepartidor($nombre_completo, $telefono, $idCamion, $idUsuario);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Repartidor creado correctamente',
                    'data' => strval($repartidor)
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idRepartidor'])) {
                    throw new Exception("Faltan el id del repartidor a actualizar");
                }

                $idRepartidor = $data['idRepartidor'];
                $nombre_completo = $data['nombre_completo'] ?? null;
                $telefono = $data['telefono'] ?? null;
                $idCamion = $data['idCamion'] ?? null;

                $repartidor = $db->updateRepartidor($idRepartidor, $nombre_completo, $telefono, $idCamion);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Repartidor actualizado correctamente',
                    'data' => strval($repartidor)
                ]);
                break;
            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idRepartidor'])) {
                    throw new Exception("Faltan el id del repartidor a eliminar");
                }

                $idRepartidor = $data['idRepartidor'];

                //$repartidor = $db->deleteRepartidor($idRepartidor);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Repartidor eliminado correctamente',
                    'data' => $repartidor
                ]);
                break;
        }
    } catch (Exception $e) {
        echo json_encode([
            'OK' => false,
            'error' => $e->getMessage()
        ]);
    }

?>