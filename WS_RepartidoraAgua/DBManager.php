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


    }


?>