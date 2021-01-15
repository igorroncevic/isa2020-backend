-- Adrese
insert into address(id, city, country, street) values ('3b00204e-1897-4b6e-a175-5d0595833ced', 'Novi Sad', 'Serbia', 'Danila Kisa 4');
insert into address(id, city, country, street) values ('1eb485a6-4afd-4121-ad03-af571841ddbd', 'Novi Sad', 'Serbia', 'Laze Stajica 19');
insert into address(id, city, country, street) values ('9241601b-a20a-4f26-9523-d4a670a881dc', 'Belgrade', 'Serbia', 'Hercegovacka 33');
insert into address(id, city, country, street) values ('34c4d86e-8781-4163-b61a-4720c39b7ad6', 'Nis', 'Serbia', 'Vozda Karadjordja 26');

-- Dermatolozi
insert into doctor (id, "name", surname, email, "password", "role", doctor_role, fk_address)
values ('a5ac174a-45b3-487f-91cb-3d3f727d6f1c','Pera', 'Peric', 'peraperic@gmail.com', 'nekipass123', 0, 0, '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into doctor (id, "name", surname, email, "password", "role", doctor_role, fk_address)
values ('1accde09-0aa2-4ea1-9a38-17f4635198b9','Jovan', 'Jovanovic', 'jovajovanovic@gmail.com', 'nekipass123', 0, 0, '9241601b-a20a-4f26-9523-d4a670a881dc');
insert into doctor (id, "name", surname, email, "password", "role", doctor_role, fk_address)
values ('61e9bf31-afae-4b2f-9197-8e2a328707c5','Ivana', 'Ivanovic', 'ivanaivanovic@gmail.com', 'nekipass123', 0, 0, '34c4d86e-8781-4163-b61a-4720c39b7ad6');

-- Loyalty program
insert into loyalty(id, category, discount, min_points, max_points) values ('c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 'Regular', 0, 0, 49);
insert into loyalty(id, category, discount, min_points, max_points) values ('a0f298f1-ccb1-4366-9ca8-77338e017726', 'Silver', 5, 50, 99);
insert into loyalty(id, category, discount, min_points, max_points) values ('4b0dec94-b362-49a7-acda-b125a02c2692', 'Gold', 10, 100, 149);
insert into loyalty(id, category, discount, min_points, max_points) values ('ab98fe4c-0e70-4087-b53f-637bbac6bfe4', 'Platinum', 14, 150, -1); --ako je max -1, to znaci da su svi 150+ u ovoj kategoriji

-- Pacijenti
insert into patient (id, "name", surname, email, "password", "role", fk_address, loyalty_points, loyalty_id, penalties)
values ('cc6fd408-0084-420b-8078-687d8a72744b', 'Slobodanka', 'Ilic', 'slobodankailic@gmail.com', 'nekipass123', 4, '1eb485a6-4afd-4121-ad03-af571841ddbd', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0);
insert into patient (id, "name", surname, email, "password", "role", fk_address, loyalty_points, loyalty_id, penalties)
values ('dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 'Ilija', 'Stevic', 'ilijastevic@gmail.com', 'nekipass123', 4, '1eb485a6-4afd-4121-ad03-af571841ddbd', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0);
insert into patient (id, "name", surname, email, "password", "role", fk_address, loyalty_points, loyalty_id, penalties)
values ('5ffe884f-9cd8-42f5-adc4-2a27cd8d2737', 'Mileta', 'Perovic', 'miletaperovic@gmail.com', 'nekipass123', 4, '34c4d86e-8781-4163-b61a-4720c39b7ad6', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0);

-- Termini
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('1d96a5d6-f5f6-4a74-9208-109991010f31', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-28 12:25:00', '2021-02-28 13:00:00', 12, 0, 5, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('f05c035a-ffcc-4810-b34a-bb0c782f59a0', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-02-28 12:25:00', '2021-02-28 13:00:00', 19, 0, 8, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('0988b5b2-1d09-47ad-ace7-45282f11e24b', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-28 13:05:00', '2021-02-28 13:30:00', 12, 0, 5, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('43501e13-caf6-4d98-a69a-db0178c582e9', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-02-28 13:05:00', '2021-02-28 13:05:00', 19, 0, 8, null, null);
