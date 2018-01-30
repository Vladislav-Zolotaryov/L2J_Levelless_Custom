delete from spawnlist where npc_templateid in (select id from npc where name = 'Item Broker');
delete from npcskills where npcid in (select id from npc  where name = 'Item Broker');
delete from npcAIData where npc_id in (select id from npc  where name = 'Item Broker');
delete from npc where name = 'Item Broker';
