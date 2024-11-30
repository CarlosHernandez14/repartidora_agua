<?php

    class DBManager {
        private $db;
        private $host;
        private $user;
        private $pass;
        private $port;

        public function __construct($host, $user, $pass, $port) {
            $this->host = $host;
            $this->user = $user;
            $this->pass = $pass;
            $this->port = $port;
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