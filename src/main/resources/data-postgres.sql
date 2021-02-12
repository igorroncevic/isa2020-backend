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

---Authority
insert into authority (id,"name") values('d774ee37-be38-4bfb-8c01-ef0e8a73e1cf','ROLE_PATIENT');
insert into authority (id,"name") values('ff2ecb46-200f-4a25-a6c4-0a96f9ab082f','ROLE_DERMATOLOGIST');
insert into authority (id,"name") values('f71f029d-20c1-46c7-97f3-d64ec1c7e06a','ROLE_PHARMACIST');
insert into authority (id,"name") values('19c1565a-0e28-4930-9b63-fa2851a69d39','ROLE_PHADMIN');
insert into authority (id,"name") values('2fef2655-53d0-4233-bcf9-771d12040383','ROLE_SYSADMIN');
insert into authority (id,"name") values('91541917-0498-4e3a-a8c5-966118f78683','ROLE_SUPPLIER');

-- RegisteredUser (Sve sifre su "nekipass123")
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('a5ac174a-45b3-487f-91cb-3d3f727d6f1c', 'Pera', false , 'Peric', 'peraperic@gmail.com', '065/123-456', '$2a$10$Nqiae.pIzL.ww2rMu0kNS.1EcCvfsJQW0/XxK1pbd5hPIp5zi5aGe',
        'dermatologist', '3b00204e-1897-4b6e-a175-5d0595833ced' );
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('1accde09-0aa2-4ea1-9a38-17f4635198b9', 'Jovan', false ,'Jovanovic', 'jovajovanovic@gmail.com', '065/598-127', '$2a$10$aOIWj.EDQuwcOumNKyW7NekDhDKwZVnsVuQbXQttDIRD31/vSo1gy',
        'dermatologist', '9241601b-a20a-4f26-9523-d4a670a881dc');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('61e9bf31-afae-4b2f-9197-8e2a328707c5', 'Ivana', false ,'Ivanovic', 'ivanaivanovic@gmail.com', '065/917-427', '$2a$10$w8QK3oJiXQE21N3.kYbFyuezcKwnTeyTHJPNe83q92agWsThv4edS',
        'dermatologist', '34c4d86e-8781-4163-b61a-4720c39b7ad6');

insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('b0a591d4-f627-45ba-8aa9-926e85c93e08', 'Jovana', false, 'Jovic', 'jovanajovic@gmail.com', '065/123-456', '$2a$10$SzQ8ZS1udry6KVa2vHu4ROKiepZO3EVFuURFrzzYUSZ61wE9KHu3q',
        'pharmacist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('b7d924b5-c488-47bc-be78-2bdad330f468', 'Milan', false, 'Milovanovic', 'milanmilovanovic@gmail.com', '065/123-456', '$2a$10$buYpoUuIrJYRHo3vJT/YnOHF8IKA97iUlDUhfxr5ZP2AKdP9YLeEa',
        'pharmacist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('236b04cd-2c94-4222-a101-16fb827ce816', 'Jelena', false, 'Jelicic', 'jelenajelicic@gmail.com', '065/123-456', '$2a$10$CmDtvHE423bYzSoVnXnPy.VDI3ZBCbN18FsoEgBsQKjso2zl3tIWS',
        'pharmacist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('cb317110-4d3e-4a52-8ca6-0c73bc62b4d2', 'Milos', false, 'Markovic', 'milosmarkovic@gmail.com', '069/384-012', '$2a$10$R3oeMC9CsWm.lv7eLfujBe1TB3NN/euhrRckzRwaT5y2zkOcPBlxO',
        'pharmacist', '01aeb937-998a-4efc-b4d9-e7aa4dd980c6');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('02fb4f65-923c-49fe-b32a-c40ffee74b47', 'Milija', false, 'Raicevic', 'milijaraicevic@gmail.com', '069/222-111', '$2a$10$amwjmmU535aSE6JHKgiNeezlElteZ5oX1oGXHG6tVYZIsga5acykK',
        'pharmacist', 'eb797275-30de-460e-8f9b-f5034ccefc7d');

insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('b861fa5d-e543-4c61-adac-0c3f27bc32a0', 'Jagoš', false, 'Marić', 'jagosmaric@gmail.com', '064123456', '$2a$10$ezrRpEVuAn9W9dwGBYkB.uUv8kf52xf9E0LEFrnHjQI7fVWncwi7a',
        'supplier', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('6dbd78ec-c0c7-403a-b1d0-bea63b5a1981', 'Janko', false, 'Jovanovic', 'jankojovanovic@gmail.com', '064598127', '$2a$10$eSKR4LWxm.AYYMufmhr4YOBeUoClxJWGR2hMovgjmHxcQOitRFTs.',
        'supplier', '9241601b-a20a-4f26-9523-d4a670a881dc');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('84f5d15e-8b52-4e00-ac4f-6b35e030c732', 'Marina', false, 'Marković', 'marinamarkovic@gmail.com', '065917427', '$2a$10$HsD/nwSnitpUfTSCVbIZ5e/ZyA1WNbs0CjzU3j76/1IuLdzWq.aza',
        'supplier', '34c4d86e-8781-4163-b61a-4720c39b7ad6');

insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('5baee785-9b8b-4287-a0bb-cfbee680b88d', 'Olivera', false, 'Petrović', 'opetrovic@gmail.com', '064/987-456', '$2a$10$s0/U4wkSfEKVf5Q0wkBKZOwpkAgi4NhVKkEcikd2Z6bOLnngkJqHO',
        'sysAdmin', '3b00204e-1897-4b6e-a175-5d0595833ced');

insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('cc6fd408-0084-420b-8078-687d8a72744b', 'Slobodanka', false, 'Ilic', 'slobodankailic@gmail.com', '063/111-456', '$2a$10$esXjEn1ag7xsrBDMbkPXYeUNeBrALSQ3iY.YckxYQ/CUY4L0menlC',
        'patient', '1eb485a6-4afd-4121-ad03-af571841ddbd');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 'Ilija', false, 'Stevic', 'ilijastevic@gmail.com', '063/999-254', '$2a$10$WUJNdKTH8hIWsT6QnfLpq..ZoG8N/.kswU13Rpl8bCuv6Ap2KiDGi',
        'patient', '1eb485a6-4afd-4121-ad03-af571841ddbd');
insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('5ffe884f-9cd8-42f5-adc4-2a27cd8d2737', 'Mileta', false, 'Perovic', 'miletaperovic@gmail.com', '063/948-949', '$2a$10$783b7eQgIcsXgPNctBJV6O2f/vD87qgVQT0JgfOfH9sdeKcNHceMC',
        'patient', '34c4d86e-8781-4163-b61a-4720c39b7ad6');

insert into registered_user (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('40c88a70-d8cd-4d8f-b56f-eb158f7c27fa', 'Mile', false, 'Ulemek', 'mileulemek@gmail.com', '069/885-555', '$2a$10$6ydFb4jE77JMJKNN1uEjrOFSoO6i1Z43sAOnQXIlzpzWRSAImNMfa',
        'pharmacyAdmin', '30cedadd-1379-42fc-9a0c-185b977ed3d9');


-- User authority
-- Pacijenti
insert into user_authority(user_id, authority_id) values ('cc6fd408-0084-420b-8078-687d8a72744b', 'd774ee37-be38-4bfb-8c01-ef0e8a73e1cf');
insert into user_authority(user_id, authority_id) values ('dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 'd774ee37-be38-4bfb-8c01-ef0e8a73e1cf');
insert into user_authority(user_id, authority_id) values ('5ffe884f-9cd8-42f5-adc4-2a27cd8d2737', 'd774ee37-be38-4bfb-8c01-ef0e8a73e1cf');
-- Farmaceuti
insert into user_authority(user_id, authority_id) values ('b0a591d4-f627-45ba-8aa9-926e85c93e08', 'f71f029d-20c1-46c7-97f3-d64ec1c7e06a');
insert into user_authority(user_id, authority_id) values ('b7d924b5-c488-47bc-be78-2bdad330f468', 'f71f029d-20c1-46c7-97f3-d64ec1c7e06a');
insert into user_authority(user_id, authority_id) values ('236b04cd-2c94-4222-a101-16fb827ce816', 'f71f029d-20c1-46c7-97f3-d64ec1c7e06a');
insert into user_authority(user_id, authority_id) values ('cb317110-4d3e-4a52-8ca6-0c73bc62b4d2', 'f71f029d-20c1-46c7-97f3-d64ec1c7e06a');
insert into user_authority(user_id, authority_id) values ('02fb4f65-923c-49fe-b32a-c40ffee74b47', 'f71f029d-20c1-46c7-97f3-d64ec1c7e06a');
-- Dermatolozi
insert into user_authority(user_id, authority_id) values ('a5ac174a-45b3-487f-91cb-3d3f727d6f1c', 'ff2ecb46-200f-4a25-a6c4-0a96f9ab082f');
insert into user_authority(user_id, authority_id) values ('1accde09-0aa2-4ea1-9a38-17f4635198b9', 'ff2ecb46-200f-4a25-a6c4-0a96f9ab082f');
insert into user_authority(user_id, authority_id) values ('61e9bf31-afae-4b2f-9197-8e2a328707c5', 'ff2ecb46-200f-4a25-a6c4-0a96f9ab082f');
insert into user_authority (user_id, authority_id) values ('b861fa5d-e543-4c61-adac-0c3f27bc32a0', '91541917-0498-4e3a-a8c5-966118f78683');
insert into user_authority (user_id, authority_id) values ('6dbd78ec-c0c7-403a-b1d0-bea63b5a1981', '91541917-0498-4e3a-a8c5-966118f78683');
insert into user_authority (user_id, authority_id) values ('84f5d15e-8b52-4e00-ac4f-6b35e030c732', '91541917-0498-4e3a-a8c5-966118f78683');
--Sys admin
insert into user_authority (user_id, authority_id) values ('5baee785-9b8b-4287-a0bb-cfbee680b88d', '2fef2655-53d0-4233-bcf9-771d12040383');

-- Dermatolozi
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('a5ac174a-45b3-487f-91cb-3d3f727d6f1c', 'Pera', false, 'Peric', 'peraperic@gmail.com', '065/123-456', '$2a$10$Nqiae.pIzL.ww2rMu0kNS.1EcCvfsJQW0/XxK1pbd5hPIp5zi5aGe',
        'dermatologist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('1accde09-0aa2-4ea1-9a38-17f4635198b9', 'Jovan', false, 'Jovanovic', 'jovajovanovic@gmail.com', '065/598-127', '$2a$10$aOIWj.EDQuwcOumNKyW7NekDhDKwZVnsVuQbXQttDIRD31/vSo1gy',
        'dermatologist', '9241601b-a20a-4f26-9523-d4a670a881dc');
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('61e9bf31-afae-4b2f-9197-8e2a328707c5', 'Ivana', false, 'Ivanovic', 'ivanaivanovic@gmail.com', '065/917-427', '$2a$10$w8QK3oJiXQE21N3.kYbFyuezcKwnTeyTHJPNe83q92agWsThv4edS',
        'dermatologist', '34c4d86e-8781-4163-b61a-4720c39b7ad6');

-- Farmaceuti
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('b0a591d4-f627-45ba-8aa9-926e85c93e08', 'Jovana', false, 'Jovic', 'jovanajovic@gmail.com', '065/123-456', '$2a$10$SzQ8ZS1udry6KVa2vHu4ROKiepZO3EVFuURFrzzYUSZ61wE9KHu3q',
        'pharmacist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('b7d924b5-c488-47bc-be78-2bdad330f468', 'Milan', false, 'Milovanovic', 'milanmilovanovic@gmail.com', '065/123-456', '$2a$10$buYpoUuIrJYRHo3vJT/YnOHF8IKA97iUlDUhfxr5ZP2AKdP9YLeEa',
        'pharmacist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('236b04cd-2c94-4222-a101-16fb827ce816', 'Jelena', false, 'Jelicic', 'jelenajelicic@gmail.com', '065/123-456', '$2a$10$CmDtvHE423bYzSoVnXnPy.VDI3ZBCbN18FsoEgBsQKjso2zl3tIWS',
        'pharmacist', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('cb317110-4d3e-4a52-8ca6-0c73bc62b4d2', 'Milos', false, 'Markovic', 'milosmarkovic@gmail.com', '069/384-012', '$2a$10$R3oeMC9CsWm.lv7eLfujBe1TB3NN/euhrRckzRwaT5y2zkOcPBlxO',
        'pharmacist', '01aeb937-998a-4efc-b4d9-e7aa4dd980c6');
insert into doctor (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address)
values ('02fb4f65-923c-49fe-b32a-c40ffee74b47', 'Milija', false,'Raicevic', 'milijaraicevic@gmail.com', '069/222-111', '$2a$10$amwjmmU535aSE6JHKgiNeezlElteZ5oX1oGXHG6tVYZIsga5acykK',
        'pharmacist', 'eb797275-30de-460e-8f9b-f5034ccefc7d');

-- Dobavljaci
insert into supplier (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('b861fa5d-e543-4c61-adac-0c3f27bc32a0', 'Jagoš', 'Marić', 'jagosmaric@gmail.com', '064123456', '$2a$10$ezrRpEVuAn9W9dwGBYkB.uUv8kf52xf9E0LEFrnHjQI7fVWncwi7a',
        'supplier', '3b00204e-1897-4b6e-a175-5d0595833ced');
insert into supplier (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('6dbd78ec-c0c7-403a-b1d0-bea63b5a1981', 'Janko', 'Jovanovic', 'jankojovanovic@gmail.com', '064598127', '$2a$10$eSKR4LWxm.AYYMufmhr4YOBeUoClxJWGR2hMovgjmHxcQOitRFTs.',
        'supplier', '9241601b-a20a-4f26-9523-d4a670a881dc');
insert into supplier (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('84f5d15e-8b52-4e00-ac4f-6b35e030c732', 'Marina', 'Marković', 'marinamarkovic@gmail.com', '065917427', '$2a$10$HsD/nwSnitpUfTSCVbIZ5e/ZyA1WNbs0CjzU3j76/1IuLdzWq.aza',
        'supplier', '34c4d86e-8781-4163-b61a-4720c39b7ad6');

-- Sistem administrator
insert into system_admin (id, "name", surname, email, phone_number, "password", "role", fk_address)
values ('5baee785-9b8b-4287-a0bb-cfbee680b88d', 'Olivera', 'Petrović', 'opetrovic@gmail.com', '064/987-456', '$2a$10$s0/U4wkSfEKVf5Q0wkBKZOwpkAgi4NhVKkEcikd2Z6bOLnngkJqHO',
        'sysAdmin', '3b00204e-1897-4b6e-a175-5d0595833ced');

-- Loyalty program
insert into loyalty(id, category, discount, min_points, max_points, checkup_points, counseling_points)
values ('c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 'Regular', 0, 0, 49, 3, 2);
insert into loyalty(id, category, discount, min_points, max_points, checkup_points, counseling_points)
values ('a0f298f1-ccb1-4366-9ca8-77338e017726', 'Silver', 5, 50, 99, 5, 4);
insert into loyalty(id, category, discount, min_points, max_points, checkup_points, counseling_points)
values ('4b0dec94-b362-49a7-acda-b125a02c2692', 'Gold', 10, 100, 149, 7, 6);
insert into loyalty(id, category, discount, min_points, max_points, checkup_points, counseling_points)
values ('ab98fe4c-0e70-4087-b53f-637bbac6bfe4', 'Platinum', 14, 150, -1, 10, 9);
--ako je max -1, to znaci da su svi 150+ u ovoj kategoriji

-- Pacijenti
insert into patient (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address, loyalty_points, loyalty_id, penalties, activated)
values ('cc6fd408-0084-420b-8078-687d8a72744b', 'Slobodanka', false, 'Ilic', 'slobodankailic@gmail.com', '063/111-456', '$2a$10$esXjEn1ag7xsrBDMbkPXYeUNeBrALSQ3iY.YckxYQ/CUY4L0menlC', 'patient',
        '1eb485a6-4afd-4121-ad03-af571841ddbd', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0, true);
insert into patient (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address, loyalty_points, loyalty_id, penalties, activated)
values ('dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 'Ilija', false, 'Stevic', 'ilijastevic@gmail.com', '063/999-254', '$2a$10$WUJNdKTH8hIWsT6QnfLpq..ZoG8N/.kswU13Rpl8bCuv6Ap2KiDGi', 'patient',
        '1eb485a6-4afd-4121-ad03-af571841ddbd', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0, true);
insert into patient (id, "name", "first_login", surname, email, phone_number, "password", "role", fk_address, loyalty_points, loyalty_id, penalties, activated)
values ('5ffe884f-9cd8-42f5-adc4-2a27cd8d2737', 'Mileta', false, 'Perovic', 'miletaperovic@gmail.com', '063/948-949', '$2a$10$783b7eQgIcsXgPNctBJV6O2f/vD87qgVQT0JgfOfH9sdeKcNHceMC', 'patient',
        '34c4d86e-8781-4163-b61a-4720c39b7ad6', 0, 'c976e4d9-0bd0-470d-9a10-bdc1ff1ba91c', 0, true);

-- Pregledi
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('1d96a5d6-f5f6-4a74-9208-109991010f31', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-28 12:25:00',
        '2021-02-28 13:00:00', 12, 'checkup', null, null, 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('0988b5b2-1d09-47ad-ace7-45282f11e24b', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-28 13:05:00',
        '2021-02-28 13:15:00', 12, 'checkup', null, null, 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('f05c035a-ffcc-4810-b34a-bb0c782f59a0', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-02-28 12:25:00',
        '2021-02-28 13:00:00', 19, 'checkup', null, null, 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('43501e13-caf6-4d98-a69a-db0178c582e9', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-01-18 13:15:00',
        '2021-01-18 13:35:00', 19, 'checkup', null, null, 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('ebec212e-5ff8-45f8-8411-82d6ac3b69a6', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-02-28 13:35:00',
        '2021-02-28 14:00:00', 19, 'counseling', null, null, 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('bef34914-aaab-4c7d-8ef5-c2844eb33d6d', '1accde09-0aa2-4ea1-9a38-17f4635198b9', '2021-01-24 13:15:00',
        '2021-01-24 13:35:00', 19, 'checkup', null, 'cc6fd408-0084-420b-8078-687d8a72744b', 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('c3695891-66a8-421f-8759-5602513975f8', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-01-25 12:25:00',
        '2021-01-25 13:00:00', 12, 'checkup', null, 'cc6fd408-0084-420b-8078-687d8a72744b', 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('f1694aa0-a662-405e-9eb6-60d6a9ffd87c', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-28 13:15:00',
        '2021-02-28 13:45:00', 12, 'checkup', null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('ddf04525-26f4-4d13-a267-00a30ec2412a', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-01-15 14:15:00',
        '2021-01-15 14:45:00', 12, 'checkup', null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 0);

insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('a045baba-1d99-44e9-a6c9-693c6717a336', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-21 12:25:00',
        '2021-02-21 13:00:00', 12, 'checkup', null, 'cc6fd408-0084-420b-8078-687d8a72744b', 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('a6d049b5-8fe0-471d-8b33-d83bdb0c4602', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '2021-02-23 10:00:00',
        '2021-02-23 10:15:00', 9, 'checkup', null, 'cc6fd408-0084-420b-8078-687d8a72744b', 0);

-- Savjetovanja
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('fa417459-da91-4812-9d74-3a8ebaa25288', 'b0a591d4-f627-45ba-8aa9-926e85c93e08', '2021-01-15 13:15:00',
        '2021-01-15 13:35:00', 14, 'counseling', null, null, 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('d3e0f32b-f933-445a-b743-1930da41aa00', 'b7d924b5-c488-47bc-be78-2bdad330f468', '2021-01-15 13:15:00',
        '2021-01-15 13:35:00', 15, 'counseling', null, 'cc6fd408-0084-420b-8078-687d8a72744b', 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('8f31bed6-1816-48dc-93f4-f7c5fc3cffe2', 'b7d924b5-c488-47bc-be78-2bdad330f468', '2021-03-01 13:15:00',
        '2021-03-01 13:35:00', 15, 'counseling', null, 'cc6fd408-0084-420b-8078-687d8a72744b', 0);
insert into term (id, doctor_id, start_time, end_time, price, "type", report_id, patient_id, version)
values ('3c16d087-fcc9-4c31-9431-71a92bc781fd', 'b0a591d4-f627-45ba-8aa9-926e85c93e08', '2021-01-15 13:45:00',
        '2021-01-15 14:00:00', 19, 'counseling', null, null, 0);

-- Lijekovi
insert into medicine (id, loyalty_points, "name", medicine_code, medicine_type, medicine_form, manufacturer, issuing_regime)
values ('7571786b-2fc5-4756-ab5c-1f4af756e6f2', 10, 'Aspirin', 'AS01', 'human_medicine', 'tablet', 'Bayer', 'without_prescription');
insert into medicine (id, loyalty_points, "name", medicine_code, medicine_type, medicine_form, manufacturer, issuing_regime)
values ('592e558c-b3f9-4088-b468-28764908bd92', 13, 'Brufen', 'BR01', 'human_medicine', 'tablet', 'Hemofarm', 'without_prescription');
insert into medicine (id, loyalty_points, "name", medicine_code, medicine_type, medicine_form, manufacturer, issuing_regime)
values ('3163f62a-5b88-4295-854c-c9400e19089f', 12, 'Kafetin', 'CAF01', 'human_medicine', 'tablet', 'Alkaloid', 'without_prescription');
insert into medicine (id, loyalty_points, "name", medicine_code, medicine_type, medicine_form, manufacturer, issuing_regime)
values ('32636111-4bc6-424c-a200-067e7f8a9386', 8, 'Strepsils', 'S01', 'human_medicine', 'buccalMedicines', 'Bayer', 'without_prescription');
insert into medicine (id, loyalty_points, "name", medicine_code, medicine_type, medicine_form, manufacturer, issuing_regime)
values ('659d0931-63d9-4ef6-bbc4-abdeb4a99539', 11, 'Fluimucil', 'FL01', 'human_medicine', 'tablet', 'Zambon', 'with_prescription');

-- Specifikacija leka
insert into medicine_specification (id, medicine_id, replacement_medicine_code, recommended_dose, contraindications, drug_composition, additional_notes)
values ('004bf154-bd94-4bbb-ae8a-9e18f7103653', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 'BR01', 3, 'glavobolja, mucnina', 'acetilsalicilna kiselina', 'ne preporucije se deci mladjoj od 6 godina');
insert into medicine_specification (id, medicine_id, replacement_medicine_code, recommended_dose, contraindications, drug_composition, additional_notes)
values ('83c48dad-439d-4f59-82ef-c3315ba8e2e3','592e558c-b3f9-4088-b468-28764908bd92', 'CAF01', 4, 'mucnina, malaksalost', 'ibuprofen', null);
-- insert into medicine_specification (id, replacement_medicine_code, recommended_dose, contraindications, drug_composition, additional_notes)
-- values ('7d3fe4e7-6336-48cc-ac98-1b9d0de36f61', 'BR01', 3, null, null, 'ne preporucije se deci mladjoj od 6 godina');
-- insert into medicine_specification (id, replacement_medicine_code, recommended_dose, contraindications, drug_composition, additional_notes)
-- values ('8f338b9b-c4e9-4514-9c1b-9488f8ac84e3', null, 4, null, 'flurbiprofen', 'ne preporucije se deci mladjoj od 4 godine');
-- insert into medicine_specification (id, replacement_medicine_code, recommended_dose, contraindications, drug_composition, additional_notes)
-- values ('7e31746d-da76-424c-823c-55b8306ac9a1', null, 4, null, 'acetilcistein', 'pre koriscenja posavetovati se sa lekarom');

-- Alergije
insert into alergicto(patient_id, medicine_id)
values('cc6fd408-0084-420b-8078-687d8a72744b', '659d0931-63d9-4ef6-bbc4-abdeb4a99539');

-- Apoteke
insert into pharmacy (id, address_id, "name")
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '34c4d86e-8781-4163-b61a-4720c39b7ad6', 'Apoteka Jankovic');
insert into pharmacy (id, address_id, "name")
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '9241601b-a20a-4f26-9523-d4a670a881dc', 'BENU apoteka');
insert into pharmacy (id, address_id, "name")
values ('1272bfbd-08bb-4f2d-a9f5-cd06ad5cdf6d', '9241601b-a20a-4f26-9523-d4a670a881dc', 'Neka apoteka');

-- Administratori apoteka
insert into pharmacy_admin (id, "name", surname, email, phone_number, "password", "role", fk_address, pharmacy_id)
values ('40c88a70-d8cd-4d8f-b56f-eb158f7c27fa', 'Mile', 'Ulemek', 'mileulemek@gmail.com', '069/885-555', '$2a$10$6ydFb4jE77JMJKNN1uEjrOFSoO6i1Z43sAOnQXIlzpzWRSAImNMfa', 'pharmacyAdmin',
        '30cedadd-1379-42fc-9a0c-185b977ed3d9', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');

-- Work schedules
insert into work_schedule(pharmacy_id, doctor_id, from_hour, to_hour)
values('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 'b0a591d4-f627-45ba-8aa9-926e85c93e08', '2021-01-01 8:00:00', '2022-01-01 16:00:00');
insert into work_schedule(pharmacy_id, doctor_id, from_hour, to_hour)
values('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 'b7d924b5-c488-47bc-be78-2bdad330f468', '2021-01-01 8:00:00', '2022-01-01 16:00:00');
insert into work_schedule(pharmacy_id, doctor_id, from_hour, to_hour)
values('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '236b04cd-2c94-4222-a101-16fb827ce816', '2021-01-01 8:00:00', '2022-01-01 16:00:00');
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-01-01 09:00:00', '2021-01-31 17:00:00', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');
insert into work_schedule (from_hour, to_hour, doctor_id, pharmacy_id)
values ('2021-02-01 09:00:00', '2021-03-30 17:00:00', 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1');
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
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 49, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '592e558c-b3f9-4088-b468-28764908bd92', 129, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '3163f62a-5b88-4295-854c-c9400e19089f', 14, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '32636111-4bc6-424c-a200-067e7f8a9386', 23, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '659d0931-63d9-4ef6-bbc4-abdeb4a99539', 5, 0);

insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 49, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '592e558c-b3f9-4088-b468-28764908bd92', 5, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '3163f62a-5b88-4295-854c-c9400e19089f', 15, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '32636111-4bc6-424c-a200-067e7f8a9386', 23, 0);
insert into pharmacy_medicines (pharmacy_id, medicine_id, quantity, version)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '659d0931-63d9-4ef6-bbc4-abdeb4a99539', 44, 0);

-- Cijenovnici
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('6ec94f65-929d-466d-b278-290b4ca399cd', '2021-01-01', '2022-01-01', 19, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '7571786b-2fc5-4756-ab5c-1f4af756e6f2');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('1626d2b5-b114-468e-9864-b0eab928075d', '2021-01-01', '2022-01-01', 9, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '592e558c-b3f9-4088-b468-28764908bd92');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('0b8a5f82-089c-42c2-8e5b-0dbdff7f3a50', '2021-01-01', '2022-01-01', 12, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '3163f62a-5b88-4295-854c-c9400e19089f');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('843fa24b-ec5d-47d4-9ed4-4cd6d0f05107', '2021-01-01', '2022-01-01', 14, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '32636111-4bc6-424c-a200-067e7f8a9386');

insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('9524e1dd-a31d-4365-abd6-cf7b8b6dcdb3', '2021-01-01', '2022-01-01', 19, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '7571786b-2fc5-4756-ab5c-1f4af756e6f2');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('7fb749b2-ca20-429e-be68-1cd7381ce7a7', '2021-01-01', '2022-01-01', 10, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '592e558c-b3f9-4088-b468-28764908bd92');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('3aac2730-6537-41a8-b7b3-548d42fdeb7f', '2021-01-01', '2022-01-01', 15, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '32636111-4bc6-424c-a200-067e7f8a9386');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('7d980093-7952-46d6-b1e8-c44451ae571d', '2019-01-01', '2019-12-31', 25, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '659d0931-63d9-4ef6-bbc4-abdeb4a99539');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('2a5ea26d-8e0b-4a57-8afe-9e06d9b879b2', '2020-01-01', '2020-12-31', 25, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '659d0931-63d9-4ef6-bbc4-abdeb4a99539');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('6b65c7c3-f23f-48c5-9960-9d63153e195f', '2021-01-01', '2021-12-31', 25, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '659d0931-63d9-4ef6-bbc4-abdeb4a99539');
insert into pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id)
values ('cc98dae0-012b-4d4d-b281-6219ad66af75', '2022-01-01', '2022-12-31', 25, 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '659d0931-63d9-4ef6-bbc4-abdeb4a99539');

-- Rezervisani lijekovi
insert into reserved_medicines(id, pickup_date, medicine_id, patient_id, pharmacy_id, handled, version)
values ('ff1cc92f-30d2-4cad-b3d7-f4a65eaa2d07', '2021-02-25', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 'cc6fd408-0084-420b-8078-687d8a72744b', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', false, 0);
insert into reserved_medicines(id, pickup_date, medicine_id, patient_id, pharmacy_id, handled, version)
values ('3a67b4a6-d043-46ad-a4c3-8629cadca585', '2021-01-25', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed', true, 0);

-- Mark
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('3d54f12d-ba27-4ed1-b896-c75c284e2153', 4, 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', null, 'cc6fd408-0084-420b-8078-687d8a72744b', null);
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('d3bd5650-dc9c-4355-8e69-3e0e27e60c08', 5, 'a5ac174a-45b3-487f-91cb-3d3f727d6f1c', null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', null);

insert into mark (id, mark, pharmacy_id, medicine_id, doctor_id, patient_id)
values ('f6d9f69e-9ec0-4c0e-b9dc-d4cba7fb3c8c', 4, '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', null, null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb');
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('d1c19003-6489-41ad-a4c2-dec900222afe', 5, null, null, 'cc6fd408-0084-420b-8078-687d8a72744b', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1');
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('e15abd59-dca9-4691-a224-38ce345c7d6f', 5, null, null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1');
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('3071dbd0-0034-4b81-84d5-cf96fbf42c4c', 4, null, null, 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');
insert into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id)
values ('4f89d077-d550-4435-8636-aa528eb9f7da', 4, null, '7571786b-2fc5-4756-ab5c-1f4af756e6f2', 'cc6fd408-0084-420b-8078-687d8a72744b', null);

-- Vacation
insert into vacation(id, status, start_date, end_date, rejection_reason, doctor_id)
values ('425a7925-786e-4f4b-80d6-3044e2de8fb3', 'pending', '2021-08-01', '2021-08-15', null, 'b0a591d4-f627-45ba-8aa9-926e85c93e08');
insert into vacation(id, status, start_date, end_date, rejection_reason, doctor_id)
values ('20140d7c-ba8b-4225-82b3-a22e330b9ed8', 'pending', '2021-07-01', '2021-07-15', null, 'b7d924b5-c488-47bc-be78-2bdad330f468');
insert into vacation(id, status, start_date, end_date, rejection_reason, doctor_id)
values ('4eaae612-212a-4d1f-992b-bab47a5bdeff', 'pending', '2021-07-01', '2021-08-15', null, '236b04cd-2c94-4222-a101-16fb827ce816');
insert into vacation(id, status, start_date, end_date, rejection_reason, doctor_id)
values ('d430072f-57e3-4434-aa5f-516ac45e5715', 'pending', '2021-05-01', '2021-05-25', null, 'cb317110-4d3e-4a52-8ca6-0c73bc62b4d2');
insert into vacation(id, status, start_date, end_date, rejection_reason, doctor_id)
values ('18be1095-8158-4a8f-b9c7-9cf91f4fab4e', 'pending', '2021-08-01', '2021-08-20', null, '02fb4f65-923c-49fe-b32a-c40ffee74b47');

-- EPrescriptions
insert into eprescription(id, issue_date, patient_id, status)
values ('aa7b9e7a-0838-481a-af5a-35d237d2a701', '2021-02-01', 'cc6fd408-0084-420b-8078-687d8a72744b', 'pending');
insert into eprescription(id, issue_date, patient_id, status)
values ('7679b21d-386d-4913-97a8-81447b0e922b', '2021-02-05', 'cc6fd408-0084-420b-8078-687d8a72744b', 'pending');
insert into eprescription(id, issue_date, patient_id, status)
values ('63ecb45f-8873-43a2-b0f5-586f701ee6c1', '2021-02-11', 'cc6fd408-0084-420b-8078-687d8a72744b', 'processed');
insert into eprescription(id, issue_date, patient_id, status)
values ('4160a8fe-efd6-4891-97be-b2618919469d', '2021-02-15', 'cc6fd408-0084-420b-8078-687d8a72744b', 'declined');

insert into eprescription_medicines(e_prescription_id, eprescription_medicine_id, eprescription_pharmacy_id, quantity)
values ('aa7b9e7a-0838-481a-af5a-35d237d2a701', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 2);
insert into eprescription_medicines(e_prescription_id, eprescription_medicine_id, eprescription_pharmacy_id, quantity)
values ('7679b21d-386d-4913-97a8-81447b0e922b', '592e558c-b3f9-4088-b468-28764908bd92', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 5);
insert into eprescription_medicines(e_prescription_id, eprescription_medicine_id, eprescription_pharmacy_id, quantity)
values ('63ecb45f-8873-43a2-b0f5-586f701ee6c1', '32636111-4bc6-424c-a200-067e7f8a9386', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 3);
insert into eprescription_medicines(e_prescription_id, eprescription_medicine_id, eprescription_pharmacy_id, quantity)
values ('4160a8fe-efd6-4891-97be-b2618919469d', '7571786b-2fc5-4756-ab5c-1f4af756e6f2', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 2);
insert into eprescription_medicines(e_prescription_id, eprescription_medicine_id, eprescription_pharmacy_id, quantity)
values ('4160a8fe-efd6-4891-97be-b2618919469d', '32636111-4bc6-424c-a200-067e7f8a9386', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 4);
insert into eprescription_medicines(e_prescription_id, eprescription_medicine_id, eprescription_pharmacy_id, quantity)
values ('4160a8fe-efd6-4891-97be-b2618919469d', '592e558c-b3f9-4088-b468-28764908bd92', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 2);

-- Promocije
insert into promotion(id, start_date, end_date, text, pharmacy_id)
values ('11cc4eb4-6113-4a7b-b466-758aa35da89f', '2021-02-21', '2021-03-01', 'Promocija COVID-19 vakcine Sputnik sredom i petkom od 19:00h', '25fff0b2-ad45-4310-ac7f-96bcc5e517c1');
insert into promotion(id, start_date, end_date, text, pharmacy_id)
values ('693b2fbb-2082-46e1-ad1b-93970ae6d316', '2021-02-21', '2021-03-01', 'Promocija COVID-19 vakcine Pfizer sredom i petkom od 19:00h', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');
insert into promotion(id, start_date, end_date, text, pharmacy_id)
values ('420d9dad-e742-4e53-a2d5-66aa6ba3e50e', '2021-02-10', '2021-02-25', 'U nasoj apoteci bice postavljen stand za promociju Ferfex-a', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');
insert into promotion(id, start_date, end_date, text, pharmacy_id)
values ('b0acb3c6-f244-44d3-b833-4a3d307f47d2', '2021-02-01', '2021-04-01', 'Na rasprodaji je bensedin!', 'e93cab4a-f007-412c-b631-7a9a5ee2c6ed');

-- Pharmacy subscribed patients
insert into pharmacy_subscribed_patients(subscribed_pharmacies_id, subscribed_patients_id)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', 'cc6fd408-0084-420b-8078-687d8a72744b');
insert into pharmacy_subscribed_patients(subscribed_pharmacies_id, subscribed_patients_id)
values ('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', 'dd4c7e5f-f2c4-42dc-a809-fd7f588b3acb');
insert into pharmacy_subscribed_patients(subscribed_pharmacies_id, subscribed_patients_id)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '5ffe884f-9cd8-42f5-adc4-2a27cd8d2737');
insert into pharmacy_subscribed_patients(subscribed_pharmacies_id, subscribed_patients_id)
values ('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', 'cc6fd408-0084-420b-8078-687d8a72744b');

-- Mislim da ne treba ova tabela ali je iz nekog razloga generisana
insert into pharmacy_promotions(pharmacy_id, promotions_id)
values('e93cab4a-f007-412c-b631-7a9a5ee2c6ed', '693b2fbb-2082-46e1-ad1b-93970ae6d316');
insert into pharmacy_promotions(pharmacy_id, promotions_id)
values('25fff0b2-ad45-4310-ac7f-96bcc5e517c1', '11cc4eb4-6113-4a7b-b466-758aa35da89f');

-- Purchase orders
insert into purchase_order (id, end_date, status, pharmacy_admin_id)
values ('40c88a70-d8cd-4d8f-b56f-eb158f7c27fa', '2021-02-15 18:19:43.000000', 'accepted', '40c88a70-d8cd-4d8f-b56f-eb158f7c27fa');
insert into purchase_order (id, end_date, status, pharmacy_admin_id)
values ('1e5e53cf-8c57-4425-b07b-0420b1de156b', '2021-02-13 18:31:51.000000', 'waiting_for_response', '40c88a70-d8cd-4d8f-b56f-eb158f7c27fa');

--Purchase order medicines
insert into purchase_order_medicine (quantity, medicine_id, purchase_order_id)
values (15, '7571786b-2fc5-4756-ab5c-1f4af756e6f2', '40c88a70-d8cd-4d8f-b56f-eb158f7c27fa');
insert into purchase_order_medicine (quantity, medicine_id, purchase_order_id)
values (10, '592e558c-b3f9-4088-b468-28764908bd92', '40c88a70-d8cd-4d8f-b56f-eb158f7c27fa');
insert into purchase_order_medicine (quantity, medicine_id, purchase_order_id)
values (5, '3163f62a-5b88-4295-854c-c9400e19089f', '40c88a70-d8cd-4d8f-b56f-eb158f7c27fa');
insert into purchase_order_medicine (quantity, medicine_id, purchase_order_id)
values (2, '7571786b-2fc5-4756-ab5c-1f4af756e6f2', '1e5e53cf-8c57-4425-b07b-0420b1de156b');
insert into purchase_order_medicine (quantity, medicine_id, purchase_order_id)
values (5, '592e558c-b3f9-4088-b468-28764908bd92', '1e5e53cf-8c57-4425-b07b-0420b1de156b');

--Supplier purchase orders
insert into supplier_purchase_order (delivery_date, price, purchase_order_id, supplier_id)
values ('2021-02-11 18:33:53.000000', 15000, '40c88a70-d8cd-4d8f-b56f-eb158f7c27fa', 'b861fa5d-e543-4c61-adac-0c3f27bc32a0');
insert into supplier_purchase_order (delivery_date, price, purchase_order_id, supplier_id)
values ('2021-02-10 18:34:19.000000', 10000, '40c88a70-d8cd-4d8f-b56f-eb158f7c27fa', '6dbd78ec-c0c7-403a-b1d0-bea63b5a1981');
insert into supplier_purchase_order (delivery_date, price, purchase_order_id, supplier_id)
values ('2021-02-12 18:35:09.000000', 5000, '1e5e53cf-8c57-4425-b07b-0420b1de156b', '84f5d15e-8b52-4e00-ac4f-6b35e030c732');

--Supplier medicines
insert into supplier_medicine (quantity, medicine_id, supplier_id)
values (20, '7571786b-2fc5-4756-ab5c-1f4af756e6f2', '6dbd78ec-c0c7-403a-b1d0-bea63b5a1981');
insert into supplier_medicine (quantity, medicine_id, supplier_id)
values (11, '592e558c-b3f9-4088-b468-28764908bd92', '6dbd78ec-c0c7-403a-b1d0-bea63b5a1981');
insert into supplier_medicine (quantity, medicine_id, supplier_id)
values (6, '3163f62a-5b88-4295-854c-c9400e19089f', '6dbd78ec-c0c7-403a-b1d0-bea63b5a1981');