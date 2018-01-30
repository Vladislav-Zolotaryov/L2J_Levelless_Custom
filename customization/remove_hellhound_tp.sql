delete from spawnlist where npc_templateid in (select id from npc where (id = 32314 and name = 'Warpgate') or name = 'Galate');
delete from npcskills where npcid in (select id from npc where (id = 32314 and name = 'Warpgate') or name = 'Galate');
delete from npcAIData where npc_id in (select id from npc where (id = 32314 and name = 'Warpgate') or name = 'Galate');
delete from npc where (id = 32314 and name = 'Warpgate') or name = 'Galate';
