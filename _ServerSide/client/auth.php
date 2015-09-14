<?php
	header('Content-type: application/json');
    require_once 'includes/xen_api.php';
    include 'includes/database.php';
    include 'includes/config.php';

    $params = $_POST;
    $xenAPI = new XenAPI($apiUrl, $apiKey);
    try {
        $response = $xenAPI->authenticate($params['l'], $params['p']);
        die(json_encode(array('hash' => $response['hash']),128));
    } catch (Exception $e) {
        if ($e->getCode() == 400) {
            $error = json_decode($e->getMessage(), TRUE);
            $err = $error['error'];
            $errMsg = 'API call failed: API ERROR CODE=' . $error['error'] . ' & API ERROR MESSAGE=' . $error['message'];
        } else {
            $err = $e -> getCode();
            $errMsg = 'API call failed: HTTP RESPONSE=' . $e->getMessage() . ' & HTTP STATUS CODE=' . $e->getCode();
        }
    }
    die(json_encode(array('error' => $err, 'errorMsg' => $errMsg),128));
?>