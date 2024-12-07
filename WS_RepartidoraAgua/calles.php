<?php

    require_once "DBManager.php";

    $db = new DBManager();

    header('Content-Type: application/json');


    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['idCalle'])){
                    $id = $_GET['idCalle'];
                    $calle = $db->getCalleById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Calle obtenida correctamente',
                        'data' => $calle
                    ]);
                } else {
                    $calles = $db->getCalles();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Calles obtenidas correctamente',
                        'data' => $calles
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['nombre']) || !isset($data['longitud']) || !isset($data['latitud'])) {
                    throw new Exception("Faltan datos");
                }

                $nombre = $data['nombre'];
                $longitud = $data['longitud'];
                $latitud = $data['latitud'];
                $calle = $db->createCalle($nombre, $longitud, $latitud);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Calle creada correctamente',
                    'data' => strval($calle)
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idCalle'])) {
                    throw new Exception("Faltan el id de la calle a actualizar");
                }

                $idCalle = $data['idCalle'];
                $nombre = $data['nombre'] ?? null;
                $longitud = $data['longitud'] ?? null;
                $latitud = $data['latitud'] ?? null;

                $calle = $db->updateCalle($idCalle, $nombre, $longitud, $latitud);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Calle actualizada correctamente',
                    'data' => strval($calle)
                ]);
                break;
            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idCalle']))
                    throw new Exception("Faltan el id de la calle a eliminar");

                $idCalle = $data['idCalle'];

                //$calle = $db->deleteCalle($idCalle);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Calle eliminada correctamente',
                    'data' => strval($calle)
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