SET @unqiue_category:=1;
update droplist
set category=@unqiue_category:=@unqiue_category+1
where category != -1;
