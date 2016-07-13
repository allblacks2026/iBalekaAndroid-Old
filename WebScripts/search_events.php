<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$searchCriteria = $_POST['SearchCriteria'];
$sortByDate = $_POST['SortByDate'];

if (isset($searchCriteria)) {

    if ($sortByDate == 1) {
        $searchByDate = $connectionObject->prepare("CALL SearchEvents_Ordered(?)");
        $searchByDate->bindParam(1, $searchCriteria, PDO::PARAM_STR);
        $searchByDate->execute();

        if($searchByDate->rowCount() > 0) {
            $result = $searchByDate->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
        } else {
            $searchByDate->closeCursor();
            echo "Error300";
        }
    } else if ($sortByDate == 0) {
        $normalSearch = $connectionObject->prepare("CALL SearchEvents(?)");
        $normalSearch->bindParam(1, $searchCriteria, PDO::PARAM_STR);
        $normalSearch->execute();

        if ($normalSearch->rowCount() > 0) {
            $result = $normalSearch->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
        } else {
            $normalSearch->closeCursor();
            echo "Error300";
        }
    }
} else {
    echo "Error400";
}