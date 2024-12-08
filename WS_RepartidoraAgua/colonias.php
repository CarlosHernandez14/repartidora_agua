<?php

    require_once "DBManager.php";

    $db = new DBManager();

    header('Content-Type: application/json');


    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['idColonia'])){
                    $id = $_GET['idColonia'];
                    $colonia = $db->getColoniaById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Colonia obtenida correctamente',
                        'data' => $colonia
                    ]);
                } else if(isset($_GET['idZona'])){
                    $id = $_GET['idZona'];
                    $colonias = $db->getColoniasByZona($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Colonias obtenidas correctamente',
                        'data' => $colonias
                    ]);
                } else {
                    $colonias = $db->getColonias();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Colonias obtenidas correctamente',
                        'data' => $colonias
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['nombre']) || !isset($data['idZona'])) {
                    throw new Exception("Faltan datos");
                }

                $nombre = $data['nombre'];
                $idZona = $data['idZona'];

                $colonia = $db->createColonia($nombre, $idZona);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Colonia creada correctamente',
                    'data' => strval($colonia)
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idColonia'])) {
                    throw new Exception("Faltan el id de la colonia a actualizar");
                }

                $idColonia = $data['idColonia'];
                $nombre = $data['nombre'] ?? null;

                $colonia = $db->updateColonia($idColonia, $nombre);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Colonia actualizada correctamente',
                    'data' => strval($colonia)
                ]);
                break;
            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);

                // Si esta seteado el id de la colonia en el Get header
                if (!isset($_GET['idColonia'])) {
                    throw new Exception("Faltan el id de la colonia a eliminar");
                }

                $idColonia = $_GET['idColonia'];

                $colonia = $db->deleteColonia($idColonia);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Colonia eliminada correctamente',
                    'data' => strval($colonia)
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