<?php
	$page = isset($_POST['page']) ? intval($_POST['page']) : 1;
	$rows = isset($_POST['rows']) ? intval($_POST['rows']) : 50;

	$items = array();
	date_default_timezone_set('UTC');
	for($i=1; $i<=$rows; $i++){
		$index = $i+($page-1)*$rows;
		$amount = rand(50,100);
		$price = rand(10000,20000)/100;
		$items[] = array(
			'inv' => sprintf("INV%04d",$index),
			'date' => date('Y-m-d',time()+24*3600*$i),
			'name' => 'Name' . $index,
			'note' => 'Note' . $index,
			'amount' => $amount,
			'price' => sprintf('%01.2f',$price),
			'cost' => sprintf('%01.2f',$amount*$price)
		);
	}
	$result = array();
	$result['total'] = 8000;
	$result['rows'] = $items;
	echo json_encode($result);
?>