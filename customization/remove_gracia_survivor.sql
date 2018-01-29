delete from spawnlist where npc_templateid in (select id from npc  where name = 'Gracia Survivor');
delete from npcskills where npcid in (select id from npc  where name = 'Gracia Survivor');
delete from npc where name = 'Gracia Survivor';
