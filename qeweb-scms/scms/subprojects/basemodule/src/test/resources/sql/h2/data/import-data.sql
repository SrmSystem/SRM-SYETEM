insert into qeweb_organization(id,code,mid_code,name,legal_person,org_type,role_type,register_time,top_parent_id,parent_id,level_describe,create_time,last_update_time,create_user_id,update_user_id,create_user_name,update_user_name,abolished) 
values(1,'001','001','快维',null,0,0,'2012-06-04 01:00:00',null,null,null,'2012-06-04 01:00:00','2012-06-04 01:00:00',1,1,'admin','admin',0);
insert into qeweb_organization(id,code,mid_code,name,legal_person,org_type,role_type,register_time,top_parent_id,parent_id,level_describe,create_time,last_update_time,create_user_id,update_user_id,create_user_name,update_user_name,abolished) 
values(2,'002','002','研发部',null,1,0,'2012-06-04 01:00:00',1,1,'1','2012-06-04 01:00:00','2012-06-04 01:00:00',1,1,'admin','admin',0);

insert into qeweb_user (id, login_name, name, password, salt, roles, register_date,company_id,department_id,create_time,last_update_time,create_user_id,update_user_id,create_user_name,update_user_name,abolished) 
values(1,'admin','Admin','691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','admin','2012-06-04 01:00:00',1,2,'2012-06-04 01:00:00','2012-06-04 01:00:00',1,1,'admin','admin',0);
insert into qeweb_user (id, login_name, name, password, salt, roles, register_date,company_id,department_id,create_time,last_update_time,create_user_id,update_user_id,create_user_name,update_user_name,abolished) 
values(2,'user','Calvin','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','user','2012-06-04 02:00:00',1,2,'2012-06-04 01:00:00','2012-06-04 01:00:00',1,1,'admin','admin',0);
