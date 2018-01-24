delete from droplist where itemId in (select item_id from armor where name like '%common item%' or name like '%shadow item%');
delete from droplist where itemId in (select item_id from weapon where name like '%common item%' or name like '%shadow item%');
