delete from spawnlist where npc_templateid in (select id from npc where name in ("Ketra Orc Shaman", "Inspector Adler", "Devil's Isle Survivor"));
delete from npcskills where npcid in (select id from npc  where name in ("Ketra Orc Shaman", "Inspector Adler", "Devil's Isle Survivor"));
delete from npcAIData where npc_id in (select id from npc  where name in ("Ketra Orc Shaman", "Inspector Adler", "Devil's Isle Survivor"));
delete from npc where name in ("Ketra Orc Shaman", "Inspector Adler", "Devil's Isle Survivor");
