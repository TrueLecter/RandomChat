<?php
	header('Content-type: text/html');
    require_once 'includes/xen_api.php';
    require_once 'includes/database.php';
    require_once 'includes/config.php';
    require_once 'includes/utils.php';

    $params = $_POST;
    $xenAPI = new XenAPI($apiUrl, $apiKey);
    $err = 0;
    $errMsg = "Success!";
    try {
        $login = $params['l'];
        $hash = $params['hash'];
        if (!checkAuthHash($login, $hash, $xenAPI)){
            $errMsg = 'This hash is not valid.';
            $err = 1228;
        }
    } catch (Exception $e) {
        if ($e->getCode() == 400) {
            $error = json_decode($e->getMessage(), TRUE);
            $errMsg = 'API call failed: API ERROR CODE=' . $error['error'] . ' & API ERROR MESSAGE=' . $error['message'];
            $err = $error['error'];
        } else {
            $errMsg = 'API call failed: HTTP RESPONSE=' . $e->getMessage() . ' & HTTP STATUS CODE=' . $e->getCode();
            $err = $e -> getCode();
        }
    }
    die(json_encode(array('error' => $err, 'errorMsg' => $errMsg),128));
?>