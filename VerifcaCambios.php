<?php

    $NOMBRE_ARCHIVO = "changelog.txt";
    
    $versionApp = $_POST["versionApp"]; // Version de la App
    
    $file = fopen($NOMBRE_ARCHIVO, "r") or die("Ocurrio un error");
    
    $versionActual = rtrim(fgets($file));
    
    // SI LA VERSION RECIBIDA ES NULA O DIFERENTE AL ACTUAL
    if(($versionApp == "") || ($versionApp != $versionActual)) {
        echo $versionActual;
        
    // SI LA VERSION RECIBIDA ES IGUAL A LA ACTUAL
    } else if($versionApp == $versionActual) {
        echo $versionActual;
    }
    
?>
