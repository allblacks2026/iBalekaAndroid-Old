<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$name = $_POST['Name'];
$surname = $_POST['Surname'];
$emailAddress = $_POST['EmailAddress'];
$password = $_POST['Password'];
$weight = $_POST['Weight'];
$height = $_POST['Height'];
$licenseNo = $_POST['LicenseNo'];
$gender = $_POST['Gender'];

if (isset($emailAddress, $name, $surname, $password)) {

        $updateAthlete = $connectionObject->prepare("CALL UpdateAthlete(?,?,?,?,?,?,?,?)");
        $updateAthlete->bindParam(1, $name, PDO::PARAM_STR);
        $updateAthlete->bindParam(2, $surname, PDO::PARAM_STR);
        $updateAthlete->bindParam(3, $password, PDO::PARAM_STR);
        $updateAthlete->bindParam(4, $weight, PDO::PARAM_STR);
        $updateAthlete->bindParam(5, $height, PDO::PARAM_STR);
        $updateAthlete->bindParam(6, $licenseNo, PDO::PARAM_STR);
        $updateAthlete->bindParam(7, $gender, PDO::PARAM_STR);
        $updateAthlete->bindParam(8, $emailAddress, PDO::PARAM_STR);

        $updateAthlete->execute();
        if($updateAthlete->rowCount() > 0)
        {
            $updateAthlete->closeCursor();
            echo "Success";
        } else {
            $updateAthlete->closeCursor();
            echo "Error500";
        }

} else {
    echo "Error200";
}