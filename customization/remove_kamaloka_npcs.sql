delete from spawnlist where npc_templateid in (select id from npc where name in ('Pathfinder Worker', 'Bathis', 'Lucas', 'Gosta', 'Mouen', 'Vishotsky', 'Mathias'));
delete from npcskills where npcid in (select id from npc where name in ('Pathfinder Worker', 'Bathis', 'Lucas', 'Gosta', 'Mouen', 'Vishotsky', 'Mathias'));
delete from npcAIData where npc_id in (select id from npc where name in ('Pathfinder Worker', 'Bathis', 'Lucas', 'Gosta', 'Mouen', 'Vishotsky', 'Mathias'));
delete from npc where name in ('Pathfinder Worker', 'Bathis', 'Lucas', 'Gosta', 'Mouen', 'Vishotsky', 'Mathias');
