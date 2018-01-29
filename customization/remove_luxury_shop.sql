delete from spawnlist where npc_templateid in (select id from npc  where name = 'Galladucci');
delete from npcskills where npcid in (select id from npc  where name = 'Galladucci');
delete from npc where name = 'Galladucci';
