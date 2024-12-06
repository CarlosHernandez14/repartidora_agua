<?php

    require_once "DBManager.php";

    $db = new DBManager();

    header('Content-Type: application/json');

    
    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['idCamion'])){
                    $id = $_GET['idCamion'];
                    $camion = $db->getCamionById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Camion obtenido correctamente',
                        'data' => $camion
                    ]);
                } else {
                    $camiones = $db->getCamiones();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Camiones obtenidos correctamente',
                        'data' => $camiones
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['capacidad_maxima']) || !isset($data['modelo']) || !isset($data['marca']) || !isset($data['placas'])) {
                    throw new Exception("Faltan datos");
                }

                $placas = $data['placas'];
                $modelo = $data['modelo'];
                $marca = $data['marca'];
                $capacidad_maxima = $data['capacidad_maxima'];

                $camion = $db->createCamion($capacidad_maxima, $placas, $modelo, $marca);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Camion creado correctamente',
                    'data' => $camion
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idCamion'])) {
                    throw new Exception("Faltan el id del camion a actualizar");
                }

                $idCamion = $data['idCamion'];
                $placas = $data['placas'] ?? null;
                $modelo = $data['modelo'] ?? null;
                $marca = $data['marca'] ?? null;
                $anio = $data['anio'] ?? null;

                //$camion = $db->updateCamion($idCamion, $placas, $modelo, $marca, $anio);
                echo json_encode([
                    'OK' => true,
                    'message' => 'Camion actualizado correctamente',
                    'data' => $camion
                ]);
                break;
            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idCamion'])) {
                    throw new Exception("Faltan el id del camion a eliminar");
                }

                $idCamion = $data['idCamion'];

                //$camion = $db->deleteCamion($idCamion);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Camion eliminado correctamente',
                    'data' => $camion
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