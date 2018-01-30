delete from spawnlist where npc_templateid in (select id from npc  where name = 'Dimensional Merchant');
delete from npcskills where npcid in (select id from npc  where name = 'Dimensional Merchant');
delete from npcAIData where npc_id in (select id from npc  where name = 'Dimensional Merchant');
delete from npc where name = 'Dimensional Merchant';
