<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$emailAddress = $_POST['EmailAddress'];
if (isset($emailAddress)) {

    $getAthleteID = $connectionObject->prepare("CALL GetAthleteById(?)");
    $getAthleteID->bindParam(1, $emailAddress, PDO::PARAM_STR);
    $getAthleteID->execute();
    if ($getAthleteID->rowCount() > 0) {
        $resultAthlete = $getAthleteID->fetch(PDO::FETCH_ASSOC);
        $athleteID = $resultAthlete['UserID'];
        $getAthleteID->closeCursor();

        $getAthleteStats = $connectionObject->prepare("CALL GetAthleteRuns(?)");
        $getAthleteStats->bindParam(1, $athleteID, PDO::PARAM_INT);
        $getAthleteStats->execute();
        if ($getAthleteStats->rowCount() > 0) {
            $result = $getAthleteStats->fetch(PDO::FETCH_ASSOC);
            $getAthleteStats->closeCursor();
            echo json_encode($result);
        } else {
            $getAthleteStats->closeCursor();
            echo "Error300";
        }
    } else {
        $getAthleteID->closeCursor();
        echo "Error400";
    }
}