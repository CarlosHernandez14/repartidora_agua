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
                } else if (isset($_GET['idColonia'])) {
                    $idColonia = $_GET['idColonia'];
                    $calles = $db->getCallesByColonia($idColonia);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Calles obtenidas correctamente',
                        'data' => $calles
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

                if (!isset($data['nombre']) || !isset($data['idColonia'])) {
                    throw new Exception("Faltan datos");
                }

                $nombre = $data['nombre'];
                $descripcion = $data['descripcion'] ?? null;
                $idColonia = $data['idColonia'];

                $calle = $db->createCalle($nombre, $descripcion, $idColonia);

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
                $descripcion = $data['descripcion'] ?? null;

                $calle = $db->updateCalle($idCalle, $nombre, $descripcion);
                
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