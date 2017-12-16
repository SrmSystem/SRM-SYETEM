--要货计划量产设定为O,便于要货计划行转列统计
update PURCHASE_GOODS_REQUEST set FLAG ='O' where FLAG is null;



-----设置视图---

--清理要货计划视图
CREATE VIEW VIEW_CLEAR_REQUEST AS

SELECT
	REQUEST_ID
FROM
	(
		SELECT
			REQUEST. ID AS REQUEST_ID
		FROM
			QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN
		LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM ORDER_ITEM ON ITEM_PLAN.ORDER_ITEM_ID = ORDER_ITEM. ID
		LEFT JOIN PURCHASE_GOODS_REQUEST REQUEST ON ITEM_PLAN.GOODS_REQUEST_ID = REQUEST. ID
		WHERE
			ITEM_PLAN.TO_DELIVERY_QTY = 0
		AND ITEM_PLAN.DELIVERY_QTY = 0
		AND ITEM_PLAN.ABOLISHED = 0
     AND REQUEST. ID is not null
       AND  ITEM_PLAN.SHIP_TYPE=1
		GROUP BY
			ORDER_ITEM. ID,
			REQUEST. ID
	)
GROUP BY
	REQUEST_ID
UNION
	SELECT
		REQUEST_ID
	FROM
		(
			SELECT
				REQUEST. ID AS REQUEST_ID
			FROM
				QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN
			LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM ORDER_ITEM ON ITEM_PLAN.ORDER_ITEM_ID = ORDER_ITEM. ID
			LEFT JOIN PURCHASE_GOODS_REQUEST REQUEST ON ITEM_PLAN.GOODS_REQUEST_ID = REQUEST. ID
			WHERE
				ITEM_PLAN.ORDER_QTY - ITEM_PLAN.DELIVERY_QTY - ITEM_PLAN.TO_DELIVERY_QTY > 0
			AND ITEM_PLAN.REQUEST_TIME <TO_DATE(TO_CHAR(Sysdate, 'YYYY-MM-DD'), 'YYYY-MM-DD')
			AND ITEM_PLAN.ABOLISHED = 0
      AND REQUEST. ID is not null
 AND  ITEM_PLAN.SHIP_TYPE=1
			GROUP BY
				ORDER_ITEM. ID,
				REQUEST. ID
		)
	GROUP BY
		REQUEST_ID;
		
		
		
		---VIEW_NO_DLV_TO_ORDER未发货回到订单视图
		create view VIEW_NO_DLV_TO_ORDER as
		SELECT
	NVL (SUM(ITEM_PLAN.ORDER_QTY), 0) AS sur_order_qty,
	ORDER_ITEM. ID AS ORDER_ITEM_ID,
	CASE
WHEN MAX (ORDER_ITEM.BPUMN) IS NOT NULL
AND MAX (ORDER_ITEM.BPUMZ) IS NOT NULL
AND MAX (ORDER_ITEM.BPUMN) > 0 THEN
	(
		NVL (SUM(ITEM_PLAN.ORDER_QTY), 0) * MAX (ORDER_ITEM.BPUMZ)
	) / MAX (ORDER_ITEM.BPUMN)
ELSE
	0
END AS sur_base_qty
FROM
	QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN
LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM ORDER_ITEM ON ITEM_PLAN.ORDER_ITEM_ID = ORDER_ITEM. ID
WHERE
	ITEM_PLAN.TO_DELIVERY_QTY = 0
AND ITEM_PLAN.DELIVERY_QTY = 0
AND ITEM_PLAN.ABOLISHED = 0
AND ITEM_PLAN.SHIP_TYPE=1
GROUP BY
	ORDER_ITEM. ID;
	
	
	--VIEW_NO_DLV_TO_REQUEST未发货回到要货计划视图
	create view  VIEW_NO_DLV_TO_REQUEST as 
	SELECT
	REQUEST_ID,
	NVL (SUM(SUR_QRY), 0) AS SUR_QRY
FROM
	(
		SELECT
			ORDER_ITEM. ID AS ORDER_ITEM_ID,
			REQUEST. ID AS REQUEST_ID,
			CASE
		WHEN MAX (ORDER_ITEM.BPUMN) IS NOT NULL
		AND MAX (ORDER_ITEM.BPUMZ) IS NOT NULL
		AND MAX (ORDER_ITEM.BPUMN) > 0 THEN
			(
				NVL (SUM(ITEM_PLAN.ORDER_QTY), 0) * MAX (ORDER_ITEM.BPUMZ)
			) / MAX (ORDER_ITEM.BPUMN)
		ELSE
			0
		END AS SUR_QRY
		FROM
			QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN
		LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM ORDER_ITEM ON ITEM_PLAN.ORDER_ITEM_ID = ORDER_ITEM. ID
		LEFT JOIN PURCHASE_GOODS_REQUEST REQUEST ON ITEM_PLAN.GOODS_REQUEST_ID = REQUEST. ID
		WHERE
			ITEM_PLAN.TO_DELIVERY_QTY = 0
		AND ITEM_PLAN.DELIVERY_QTY = 0
		AND ITEM_PLAN.ABOLISHED = 0
    AND ITEM_PLAN.SHIP_TYPE=1
		GROUP BY
			ORDER_ITEM. ID,
			REQUEST. ID
	)
GROUP BY
	REQUEST_ID;
	
	
	---VIEW_OVERDUE_DLV_TO_ORDER 过期清理
	create view VIEW_OVERDUE_DLV_TO_ORDER as 
	SELECT
	NVL (
		SUM (
			ITEM_PLAN.ORDER_QTY - ITEM_PLAN.DELIVERY_QTY - ITEM_PLAN.TO_DELIVERY_QTY
		),
		0
	) AS sur_order_qty,
	ORDER_ITEM. ID AS ORDER_ITEM_ID,
	CASE
WHEN MAX (ORDER_ITEM.BPUMN) IS NOT NULL
AND MAX (ORDER_ITEM.BPUMZ) IS NOT NULL
AND MAX (ORDER_ITEM.BPUMN) > 0 THEN
	(
		NVL (
			SUM (
				ITEM_PLAN.ORDER_QTY - ITEM_PLAN.DELIVERY_QTY - ITEM_PLAN.TO_DELIVERY_QTY
			),
			0
		) * MAX (ORDER_ITEM.BPUMZ)
	) / MAX (ORDER_ITEM.BPUMN)
ELSE
	0
END AS sur_base_qty
FROM
	QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN
LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM ORDER_ITEM ON ITEM_PLAN.ORDER_ITEM_ID = ORDER_ITEM. ID
WHERE
	ITEM_PLAN.ORDER_QTY - ITEM_PLAN.DELIVERY_QTY - ITEM_PLAN.TO_DELIVERY_QTY > 0
AND ITEM_PLAN.REQUEST_TIME < TO_DATE(TO_CHAR(Sysdate, 'YYYY-MM-DD'), 'YYYY-MM-DD')
AND ITEM_PLAN.ABOLISHED = 0
AND ITEM_PLAN.SHIP_TYPE=1
GROUP BY
	ORDER_ITEM. ID;
	
	---VIEW_OVERDUE_DLV_TO_REQUEST过期返回要货计划
	create view VIEW_OVERDUE_DLV_TO_REQUEST as 
	SELECT
	REQUEST_ID,
	NVL (SUM(SUR_QRY), 0) AS SUR_QRY
FROM
	(
		SELECT
			ORDER_ITEM. ID AS ORDER_ITEM_ID,
			REQUEST. ID AS REQUEST_ID,
			CASE
		WHEN MAX (ORDER_ITEM.BPUMN) IS NOT NULL
		AND MAX (ORDER_ITEM.BPUMZ) IS NOT NULL
		AND MAX (ORDER_ITEM.BPUMN) > 0 THEN
			(
				NVL (
					SUM (
						ITEM_PLAN.ORDER_QTY - ITEM_PLAN.DELIVERY_QTY - ITEM_PLAN.TO_DELIVERY_QTY
					),
					0
				) * MAX (ORDER_ITEM.BPUMZ)
			) / MAX (ORDER_ITEM.BPUMN)
		ELSE
			0
		END AS SUR_QRY
		FROM
			QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN
		LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM ORDER_ITEM ON ITEM_PLAN.ORDER_ITEM_ID = ORDER_ITEM. ID
		LEFT JOIN PURCHASE_GOODS_REQUEST REQUEST ON ITEM_PLAN.GOODS_REQUEST_ID = REQUEST. ID
		WHERE
			ITEM_PLAN.ORDER_QTY - ITEM_PLAN.DELIVERY_QTY - ITEM_PLAN.TO_DELIVERY_QTY > 0
		AND ITEM_PLAN.REQUEST_TIME < TO_DATE(TO_CHAR(Sysdate, 'YYYY-MM-DD'), 'YYYY-MM-DD')
		AND ITEM_PLAN.ABOLISHED = 0
    AND ITEM_PLAN.SHIP_TYPE=1
		GROUP BY
			ORDER_ITEM. ID,
			REQUEST. ID
	)
GROUP BY
	REQUEST_ID;
	
	
	
	
	---创建临时表
CREATE TABLE "PURCHASE_GOODS_REQUEST_CLEAR" (
"ID" NUMBER(11) NOT NULL ,
PRIMARY KEY ("ID")
);




--创建存储过程
CREATE OR REPLACE 
PROCEDURE "PRO_GOODS_REQUEST_CLEAR_EXEC"(v_flag out number) AS 
----------chao.gu 20170728 供货计划执行情况存储过程------------------
----------参数：开始时间、结束时间-----------------------------------
----------将实时情况写入表，先删除再插入-----------------------------
begin 

---将临时表进行清理start ,存的是这次发生变更的id
delete from PURCHASE_GOODS_REQUEST_CLEAR;
insert into PURCHASE_GOODS_REQUEST_CLEAR("ID") select REQUEST_ID FROM VIEW_CLEAR_REQUEST;
--将临时表进行清理end


-----------------------------所有无发货单的数据清理 start--------------------------------------
--返回订单sur_order_qty
UPDATE QEWEB_PURCHASE_ORDER_ITEM A
SET sur_order_qty = sur_order_qty + (
	SELECT
		sur_order_qty
	FROM
		VIEW_NO_DLV_TO_ORDER b
	WHERE
		A . ID = b. ORDER_ITEM_ID
)
WHERE
	A . ID IN (
		SELECT
			ORDER_ITEM_ID
		FROM
			VIEW_NO_DLV_TO_ORDER
	);

--返回订单
 UPDATE QEWEB_PURCHASE_ORDER_ITEM A
SET sur_base_qty = sur_base_qty + (
	SELECT
		sur_base_qty
	FROM
		VIEW_NO_DLV_TO_ORDER b
	WHERE
		A . ID = b. ORDER_ITEM_ID
)
WHERE
	A . ID IN (
		SELECT
			ORDER_ITEM_ID
		FROM
			VIEW_NO_DLV_TO_ORDER
	);

 --返回要货计划SUR_QRY
	UPDATE PURCHASE_GOODS_REQUEST A
SET SUR_QRY = SUR_QRY + (
	SELECT
		SUR_QRY
	FROM
		VIEW_NO_DLV_TO_REQUEST b
	WHERE
		A . ID = b. REQUEST_ID
)
WHERE
	A . ID IN (
		SELECT
			REQUEST_ID
		FROM
			VIEW_NO_DLV_TO_REQUEST
	);


---删除供货计划
update QEWEB_PURCHASE_ORDER_ITEM_PLAN  ITEM_PLAN set ITEM_PLAN.ABOLISHED=1 where ITEM_PLAN.TO_DELIVERY_QTY = 0
		AND ITEM_PLAN.DELIVERY_QTY = 0
		AND ITEM_PLAN.ABOLISHED = 0;

-----------------------------所有无发货单的数据清理 end--------------------------------------
-----------------------------删除已有发货单并且过期的数据start-------------------------------

--返回订单sur_order_qty
UPDATE QEWEB_PURCHASE_ORDER_ITEM A
SET sur_order_qty = sur_order_qty + (
	SELECT
		sur_order_qty
	FROM
		VIEW_OVERDUE_DLV_TO_ORDER b
	WHERE
		A . ID = b. ORDER_ITEM_ID
)
WHERE
	A . ID IN (
		SELECT
			ORDER_ITEM_ID
		FROM
			VIEW_OVERDUE_DLV_TO_ORDER
	);

--返回订单
 UPDATE QEWEB_PURCHASE_ORDER_ITEM A
SET sur_base_qty = sur_base_qty + (
	SELECT
		sur_base_qty
	FROM
		VIEW_OVERDUE_DLV_TO_ORDER b
	WHERE
		A . ID = b. ORDER_ITEM_ID
)
WHERE
	A . ID IN (
		SELECT
			ORDER_ITEM_ID
		FROM
			VIEW_OVERDUE_DLV_TO_ORDER
	);

 --返回要货计划SUR_QRY
	UPDATE PURCHASE_GOODS_REQUEST A
SET SUR_QRY = SUR_QRY + (
	SELECT
		SUR_QRY
	FROM
		VIEW_OVERDUE_DLV_TO_REQUEST b
	WHERE
		A . ID = b. REQUEST_ID
)
WHERE
	A . ID IN (
		SELECT
			REQUEST_ID
		FROM
			VIEW_OVERDUE_DLV_TO_REQUEST
	);


---供货计划:去掉待发货数据
update QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN set ITEM_PLAN.order_qty=(ITEM_PLAN.DELIVERY_QTY+ITEM_PLAN.TO_DELIVERY_QTY),ITEM_PLAN.UNDELIVERY_QTY=0 
where ITEM_PLAN.ORDER_QTY-ITEM_PLAN.DELIVERY_QTY-ITEM_PLAN.TO_DELIVERY_QTY>0
      AND ITEM_PLAN.REQUEST_TIME<SYSDATE
		AND ITEM_PLAN.ABOLISHED = 0;

-----------------------------删除已有发货单并且过期的数据end-------------------------------

------------------------------更新临时表中的发布状态和供应商确认状态start-----------------------
--更新供应商状态
UPDATE PURCHASE_GOODS_REQUEST A
SET A.VENDOR_CONFIRM_STATUS = nvl((
	SELECT
		CASE
	WHEN MAX (ITEM_PAN.CONFIRM_STATUS) = 0
	AND MIN (ITEM_PAN.CONFIRM_STATUS) = 0 THEN
		0
	WHEN MAX (ITEM_PAN.CONFIRM_STATUS) = 1
	AND MIN (ITEM_PAN.CONFIRM_STATUS) = 1 THEN
		1
	ELSE
		0
	END AS VENDOR_CONFIRM_STATUS
FROM
	PURCHASE_GOODS_REQUEST REQ
LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PAN ON REQ. ID = ITEM_PAN.GOODS_REQUEST_ID
WHERE
	ITEM_PAN.ABOLISHED = 0
AND A . ID = REQ. ID
AND ITEM_PAN.ABOLISHED = 0
AND ITEM_PAN.SHIP_TYPE = 1
AND REQ. ID IN (
	SELECT
		ID
	FROM
		PURCHASE_GOODS_REQUEST_CLEAR
)
GROUP BY
	REQ. ID
),0)
WHERE
	A . ID IN (
		SELECT
			ID
		FROM
			PURCHASE_GOODS_REQUEST_CLEAR
	);

--更新发布状态
UPDATE PURCHASE_GOODS_REQUEST A
SET A .PUBLISH_STATUS = nvl((
	SELECT
		CASE
	WHEN MAX (ITEM_PAN.PUBLISH_STATUS) = 0
	AND MIN (ITEM_PAN.PUBLISH_STATUS) = 0 THEN
		0
	WHEN MAX (ITEM_PAN.PUBLISH_STATUS) = 1
	AND MIN (ITEM_PAN.PUBLISH_STATUS) = 1 THEN
		1
	ELSE
		2
	END AS PUBLISH_STATUS
FROM
	PURCHASE_GOODS_REQUEST REQ
LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PAN ON REQ. ID = ITEM_PAN.GOODS_REQUEST_ID
WHERE
	ITEM_PAN.ABOLISHED = 0
AND ITEM_PAN.ABOLISHED = 0
AND ITEM_PAN.SHIP_TYPE = 1
AND A . ID = REQ. ID
AND REQ. ID IN (
	SELECT
		ID
	FROM
		PURCHASE_GOODS_REQUEST_CLEAR
)
GROUP BY
	REQ. ID
),0)
WHERE
	A . ID IN (
		SELECT
			ID
		FROM
			PURCHASE_GOODS_REQUEST_CLEAR
	);
------------------------------更新临时表中的发布状态和供应商确认状态end-----------------------
commit;
v_flag:=1;
exception
when others then
    rollback;
    v_flag:=-1;
end;

	