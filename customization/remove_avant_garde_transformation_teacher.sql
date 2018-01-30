delete from spawnlist where npc_templateid in (select id from npc  where name = 'Avant-Garde');
delete from npcskills where npcid in (select id from npc  where name = 'Avant-Garde');
delete from npcAIData where npc_id in (select id from npc  where name = 'Avant-Garde');
delete from npc where name = 'Avant-Garde';
