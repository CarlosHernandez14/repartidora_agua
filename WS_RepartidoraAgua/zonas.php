<?php

    require_once "DBManager.php";

    $db = new DBManager();

    header('Content-Type: application/json');


    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['idZona'])){
                    $id = $_GET['idZona'];
                    $zona = $db->getZonaById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Zona obtenida correctamente',
                        'data' => $zona
                    ]);
                } else {
                    $zonas = $db->getZonas();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Zonas obtenidas correctamente',
                        'data' => $zonas
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['nombre']) || !isset($data['coordenadas_x']) || !isset($data['coordenadas_y'])) {
                    throw new Exception("Faltan datos");
                }

                $nombre = $data['nombre'];
                $coordenadas_x = $data['coordenadas_x'];
                $coordenadas_y = $data['coordenadas_y'];

                $zona = $db->createZona($nombre, $coordenadas_x, $coordenadas_y);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Zona creada correctamente',
                    'data' => strval($zona)
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idZona'])) {
                    throw new Exception("Faltan el id de la zona a actualizar");
                }

                $idZona = $data['idZona'];
                $nombre = $data['nombre'] ?? null;
                $coordenadas_x = $data['coordenadas_x'] ?? null;
                $coordenadas_y = $data['coordenadas_y'] ?? null;

                $zona = $db->updateZona($idZona, $nombre, $coordenadas_x, $coordenadas_y);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Zona actualizada correctamente',
                    'data' => strval($zona)
                ]);
                break;
            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idZona'])) {
                    throw new Exception("Faltan el id de la zona a eliminar");
                }

                $idZona = $data['idZona'];

                //$zona = $db->deleteZona($idZona);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Zona eliminada correctamente',
                    'data' => strval($zona)
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