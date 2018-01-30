delete from droplist where itemid in (select item_id from etcitem where name like "%Transform%");
delete from merchant_buylist where item_id in (select item_id from etcitem where name like "%Transform%");
delete from etcitem where name like "%Transform%";
