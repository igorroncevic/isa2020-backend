-- Adrese
insert into address(id, city, country, street)
values ('3b00204e-1897-4b6e-a175-5d0595833ced', 'Novi Sad', 'Serbia', 'Danila Kisa 4');
insert into address(id, city, country, street)
values ('1eb485a6-4afd-4121-ad03-af571841ddbd', 'Novi Sad', 'Serbia', 'Laze Stajica 19');
insert into address(id, city, country, street)
values ('9241601b-a20a-4f26-9523-d4a670a881dc', 'Belgrade', 'Serbia', 'Hercegovacka 33');
insert into address(id, city, country, street)
values ('34c4d86e-8781-4163-b61a-4720c39b7ad6', 'Nis', 'Serbia', 'Vozda Karadjordja 26');
insert into address(id, city, country, street)
values ('30cedadd-1379-42fc-9a0c-185b977ed3d9', 'Novi Sad', 'Serbia', 'Doza Djerdja 40');
insert into address(id, city, country, street)
values ('01aeb937-998a-4efc-b4d9-e7aa4dd980c6', 'Novi Sad', 'Serbia', 'Pavla Papa 3');
insert into address(id, city, country, street)
values ('eb797275-30de-460e-8f9b-f5034ccefc7d', 'Novi Sad', 'Serbia', 'Andje Rankovic 8');

-- Dermatolozi
insert into doctor (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('a5ac174a-45b3-487f-91cb-3d3f727d6f1c', 'Pera', 'Peric', 'peraperic@gmail.com', '065/123-456', 'nekipass123',
        'dermatologist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into doctor (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('1accde09-0aa2-4ea1-9a38-17f4635198b9', 'Jovan', 'Jovanovic', 'jovajovanovic@gmail.com', '065/598-127', 'nekipass123',
        'dermatologist', '9241601b-a20a-4f26-9523-d4a670a881dc');
insert into doctor (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('61e9bf31-afae-4b2f-9197-8e2a328707c5', 'Ivana', 'Ivanovic', 'ivanaivanovic@gmail.com', '065/917-427', 'nekipass123',
        'dermatologist', '34c4d86e-8781-4163-b61a-4720c39b7ad6');

-- Farmaceuti
insert into doctor (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('cb317110-4d3e-4a52-8ca6-0c73bc62b4d2', 'Milos', 'Markovic', 'milosmarkovic@gmail.com', '069/384-012', 'nekipass123',
        'pharmacist', '01aeb937-998a-4efc-b4d9-e7aa4dd980c6');
insert into doctor (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('02fb4f65-923c-49fe-b32a-c40ffee74b47', 'Milija', 'Raicevic', 'milijaraicevic@gmail.com', '069/222-111', 'nekipass123',
        'pharmacist', 'eb797275-30de-460e-8f9b-f5034ccefc7d');

-- Loyalty program
insert into loyalty(id, category, discount, min_points, max_points)
values ('c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 'Regular', 0, 0, 49);
insert into loyalty(id, category, discount, min_points, max_points)
values ('a0f298f1-ccb1-4366-9ca8-77338e017726', 'Silver', 5, 50, 99);
insert into loyalty(id, category, discount, min_points, max_points)
values ('4b0dec94-b362-49a7-acda-b125a02c2692', 'Gold', 10, 100, 149);
insert into loyalty(id, category, discount, min_points, max_points)
values ('ab98fe4c-0e70-4087-b53f-637bbac6bfe4', 'Platinum', 14, 150, -1);
--ako je max -1, to znaci da su svi 150+ u ovoj kategoriji

-- Pacijenti
insert into patient (id, "name", surname, email, phone_number, "password", "role", fk_address, loyalty_points, loyalty_id, penalties)
values ('cc6fd408-0084-420b-8078-687d8a72744b', 'Slobodanka', 'Ilic', 'slobodankailic@gmail.com', '063/111-456', 'nekipass123', 'patient',
        '1eb485a6-4afd-4121-ad03-af571841ddbd', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0);
insert into patient (id, "name", surname, email, phone_number, "password", "role", fk_address, loyalty_points, loyalty_id, penalties)
values ('dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 'Ilija', 'Stevic', 'ilijastevic@gmail.com', '063/999-254', 'nekipass123', 'patient',
        '1eb485a6-4afd-4121-ad03-af571841ddbd', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0);
insert into patient (id, "name", surname, email, phone_number, "password", "role", fk_address, loyalty_points, loyalty_id, penalties)
values ('5ffe884f-9cd8-42f5-adc4-2a27cd8d2737', 'Mileta', 'Perovic', 'miletaperovic@gmail.com', '063/948-949', 'nekipass123', 'patient',
        '34c4d86e-8781-4163-b61a-4720c39b7ad6', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0);

-- Termini
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('1d96a5d6-f5f6-4a74-9208-109991010f31', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-28 12:25:00',
        '2021-02-28 13:00:00', 12, 'checkup', 5, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('0988b5b2-1d09-47ad-ace7-45282f11e24b', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-28 13:05:00',
        '2021-02-28 13:15:00', 12, 'checkup', 5, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('f05c035a-ffcc-4810-b34a-bb0c782f59a0', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-02-28 12:25:00',
        '2021-02-28 13:00:00', 19, 'checkup', 8, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('43501e13-caf6-4d98-a69a-db0178c582e9', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-01-18 13:15:00',
        '2021-01-18 13:35:00', 19, 'checkup', 8, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('ebec212e-5ff8-45f8-8411-82d6ac3b69a6', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-02-28 13:35:00',
        '2021-02-28 14:00:00', 19, 'counseling', 8, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('bef34914-aaab-4c7d-8ef5-c2844eb33d6d', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-01-15 13:15:00',
        '2021-01-15 13:35:00', 19, 'checkup', 8, null, null);
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('c3695891-66a8-421f-8759-5602513975f8', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-01-15 12:25:00',
        '2021-02-28 13:00:00', 12, 'checkup', 5, null, 'cc6fd408-0084-420b-8078-687d8a72744b');
insert into term (id, doctor_id, start_time, end_time, price, "type", loyalty_points, report_id, patient_id)
values ('f1694aa0-a662-405e-9eb6-60d6a9ffd87c', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-01-15 13:15:00',
        '2021-02-28 13:45:00', 12, 'checkup', 5, null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb');

-- Lijekovi
insert into medicine (id, loyalty_points, "name")
values ('7571786b-2fc5-4756-ab5c-1f4af756e6f2', 10, 'Aspirin');
insert into medicine (id, loyalty_points, "name")
values ('592e558c-b3f9-4088-b468-28764908bd92', 13, 'Brufen');
insert into medicine (id, loyalty_points, "name")
values ('3163f62a-5b88-4295-854c-c9400e19089f', 12, 'Kafetin');
insert into medicine (id, loyalty_points, "name")
values ('32636111-4bc6-424c-a200-067e7f8a9386', 8, 'Strepsils');
insert into medicine (id, loyalty_points, "name")
values ('659d0931-63d9-4ef6-bbc4-abdeb4a99539', 11, 'Fluimucil');

-- Apoteke
insert into pharmacy (id, address_id, "name")
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '34c4d86e-8781-4163-b61a-4720c39b7ad6', 'Apoteka Jankovic');
insert into pharmacy (id, address_id, "name")
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '9241601b-a20a-4f26-9523-d4a670a881dc', 'BENU apoteka');

-- Administratori apoteka
insert into pharmacy_admin (id, "name", surname, email, phone_number, "password", "role", fk_address, pharmacy_id)
values ('40c88a70-d8cd-4d8f-b56f-eb158f7c27fa', 'Mile', 'Ulemek', 'mileulemek@gmail.com', '069/885-555', 'ssifra', 'pharmacyAdmin',
        '30cedadd-1379-42fc-9a0c-185b977ed3d9', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');

-- Work schedules
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-01-01 09:00:00', '2021-01-01 17:00:00', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-01-01 09:00:00', '2021-01-01 11:00:00', '1accde09-0aa2-4ea1-9a38-17f4635198b9', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-01-01 13:00:00', '2021-01-01 17:00:00', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1');
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-01-01 09:00:00', '2021-01-01 12:00:00', '61e9bf31-afae-4b2f-9197-8e2a328707c5', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1');
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-01-01 09:00:00', '2021-01-01 17:00:00', 'cb317110-4d3e-4a52-8ca6-0c73bc62b4d2', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1');
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-01-01 09:00:00', '2021-01-01 17:00:00', '02fb4f65-923c-49fe-b32a-c40ffee74b47', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');

-- Lijekovi koji posjeduju apoteke
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 49, 1);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '592e558c-b3f9-4088-b468-28764908bd92', 129, 1);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '3163f62a-5b88-4295-854c-c9400e19089f', 14, 1);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '32636111-4bc6-424c-a200-067e7f8a9386', 23, 1);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '659d0931-63d9-4ef6-bbc4-abdeb4a99539', 0, 1);

insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 49, 1);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '592e558c-b3f9-4088-b468-28764908bd92', 0, 1);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '32636111-4bc6-424c-a200-067e7f8a9386', 23, 1);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '659d0931-63d9-4ef6-bbc4-abdeb4a99539', 44, 1);

-- Cijenovnici
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('6ec94f65-929d-466d-b278-290b4ca399cd', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 19, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '7571786b-2fc5-4756-ab5c-1f4af756e6f2');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('1626d2b5-b114-468e-9864-b0eab928075d', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 9, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '592e558c-b3f9-4088-b468-28764908bd92');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('0b8a5f82-089c-42c2-8e5b-0dbdff7f3a50', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 12, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '3163f62a-5b88-4295-854c-c9400e19089f');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('843fa24b-ec5d-47d4-9ed4-4cd6d0f05107', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 14, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '32636111-4bc6-424c-a200-067e7f8a9386');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('7d980093-7952-46d6-b1e8-c44451ae571d', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 25, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '659d0931-63d9-4ef6-bbc4-abdeb4a99539');

insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('9524e1dd-a31d-4365-abd6-cf7b8b6dcdb3', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 19, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '7571786b-2fc5-4756-ab5c-1f4af756e6f2');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('7fb749b2-ca20-429e-be68-1cd7381ce7a7', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 10, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '592e558c-b3f9-4088-b468-28764908bd92');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('3aac2730-6537-41a8-b7b3-548d42fdeb7f', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 15, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '32636111-4bc6-424c-a200-067e7f8a9386');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('d5397940-cafe-4f4d-865d-678cddadd07f', '2021-01-01 00:00:01', '2022-01-01 00:00:01', 21, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '659d0931-63d9-4ef6-bbc4-abdeb4a99539');

-- Rezervisani lijekovi
insert into reserved_medicines(id, pickup_date, medicine_id, patient_id, pharmacy_id, handled)
values ('ff1cc92f-30d2-4cad-b3d7-f4a65eaa2d07', '2021-02-25', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 'cc6fd408-0084-420b-8078-687d8a72744b', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', false);

-- Mark
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('3d54f12d-ba27-4ed1-b896-c75c284e2153', 4, 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', null, 'cc6fd408-0084-420b-8078-687d8a72744b', null);
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('d3bd5650-dc9c-4355-8e69-3e0e27e60c08', 5, 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', null);