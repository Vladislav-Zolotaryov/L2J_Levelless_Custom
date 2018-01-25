delete from merchant_buylists where item_id in (
select item_id from weapon where name like '%Common Item -%'
UNION ALL
select item_id from weapon where name like '%Shadow Item -%'
UNION ALL
select item_id from weapon where name like '%Shadow Item:%'
UNION ALL
select item_id from armor where name like '%Common Item -%'
UNION ALL
select item_id from armor where name like '%Shadow Item -%'
UNION ALL
select item_id from armor where name like '%Shadow Item:%'
);

delete from weapon where name like '%Common Item -%';
delete from weapon where name like '%Shadow Item -%';
delete from weapon where name like '%Shadow Item:%';
delete from weapon where name like '%Standard Item -%';


delete from armor where name like '%Common Item -%';
delete from armor where name like '%Shadow Item -%';
delete from armor where name like '%Shadow Item:%';
delete from armor where name like '%Standard Item -%';
