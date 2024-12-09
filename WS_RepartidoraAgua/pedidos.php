<?php

    require_once "DBManager.php";

    $db = new DBManager();

    header('Content-Type: application/json');


    try {
        switch($_SERVER['REQUEST_METHOD']){
            case 'GET':
                if(isset($_GET['idPedido'])){
                    $id = $_GET['idPedido'];
                    $pedido = $db->getPedidoById($id);
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Pedido obtenido correctamente',
                        'data' => $pedido
                    ]);
                } else {
                    $pedidos = $db->getPedidos();
                    echo json_encode([
                        'OK' => true,
                        'message' => 'Pedidos obtenidos correctamente',
                        'data' => $pedidos
                    ]);
                }
                break;
            case 'POST':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idCliente']) || !isset($data['idDireccion']) || !isset($data['idEstado'])) {
                    throw new Exception("Faltan datos");
                }

                $idCliente = $data['idCliente'];
                $idDireccion = $data['idDireccion'];
                $idEstado = $data['idEstado'];
                $fecha = $data['fecha'] ?? null;
                $total = $data['total'] ?? null;

                $pedido = $db->createPedido($idCliente, $idDireccion, $idEstado, $fecha, $total);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Pedido creado correctamente',
                    'data' => strval($pedido)
                ]);
                break;
            case 'PUT':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idPedido'])) {
                    throw new Exception("Faltan el id del pedido a actualizar");
                }

                $idPedido = $data['idPedido'];
                $idZona = $data['idZona'] ?? null;
                $cantidad_garrafones = $data['cantidad_garrafones'] ?? null;
                $fecha = $data['fecha'] ?? null;
                $estado = $data['estado'] ?? null;
                $prioridad = $data['prioridad'] ?? null;
                $idRepartidor = $data['idRepartidor'] ?? null;


                $pedido = $db->updatePedido($idPedido, $idZona, $cantidad_garrafones, $fecha, $estado, $prioridad, $idRepartidor);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Pedido actualizado correctamente',
                    'data' => strval($pedido)
                ]);

                break;

            case 'DELETE':
                $data = json_decode(file_get_contents('php://input'), true);

                if (!isset($data['idPedido']))
                    throw new Exception("Faltan el id del pedido a eliminar");

                $idPedido = $data['idPedido'];

                //$pedido = $db->deletePedido($idPedido);

                echo json_encode([
                    'OK' => true,
                    'message' => 'Pedido eliminado correctamente',
                    'data' => strval($pedido)
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