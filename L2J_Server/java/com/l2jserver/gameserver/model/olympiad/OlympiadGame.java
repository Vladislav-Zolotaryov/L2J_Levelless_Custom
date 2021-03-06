/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.olympiad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.HeroSkillTable;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.olympiad.Olympiad.COMP_TYPE;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.ExOlympiadMatchEnd;
import com.l2jserver.gameserver.network.serverpackets.ExOlympiadMode;
import com.l2jserver.gameserver.network.serverpackets.ExOlympiadUserInfo;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.SkillCoolTime;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.templates.StatsSet;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;
import com.l2jserver.util.L2FastList;


/**
 * 
 * @author GodKratos
 */
class OlympiadGame
{
	protected static final Logger _log = Logger.getLogger(OlympiadGame.class.getName());
	protected static final Logger _logResults = Logger.getLogger("olympiad");
	protected final COMP_TYPE _type;
	protected boolean _aborted;
	protected boolean _gamestarted;
	protected boolean _playerOneDisconnected;
	protected boolean _playerTwoDisconnected;
	protected boolean _playerOneDefaulted;
	protected boolean _playerTwoDefaulted;
	protected String _playerOneName;
	protected String _playerTwoName;
	protected int _playerOneID = 0;
	protected int _playerTwoID = 0;
	protected int _playerOneClass = 0;
	protected int _playerTwoClass = 0;
	protected static final int OLY_BUFFER = 36402;
	protected static final int OLY_MANAGER = 31688;
	private static final String POINTS = "olympiad_points";
	private static final String COMP_DONE = "competitions_done";
	private static final String COMP_WON = "competitions_won";
	private static final String COMP_LOST = "competitions_lost";
	private static final String COMP_DRAWN = "competitions_drawn";
	protected static boolean _battleStarted;
	protected static boolean _gameIsStarted;
	
	protected long _startTime = 0;
	
	public int _damageP1 = 0;
	public int _damageP2 = 0;
	
	public L2PcInstance _playerOne;
	public L2PcInstance _playerTwo;
	public L2Spawn _spawnOne;
	public L2Spawn _spawnTwo;
	protected L2FastList<L2PcInstance> _players;
	private final int[] _stadiumPort;
	private int x1, y1, z1, x2, y2, z2;
	public final int _stadiumID;
	protected SystemMessage _sm;
	private SystemMessage _sm2;
	private SystemMessage _sm3;
	
	protected OlympiadGame(int id, COMP_TYPE type, L2FastList<L2PcInstance> list)
	{
		_aborted = false;
		_gamestarted = false;
		_stadiumID = id;
		_playerOneDisconnected = false;
		_playerTwoDisconnected = false;
		_type = type;
		_stadiumPort = OlympiadManager.STADIUMS[id].getCoordinates();
		
		if (list != null)
		{
			_players = list;
			_playerOne = list.get(0);
			_playerTwo = list.get(1);
			
			try
			{
				_playerOneName = _playerOne.getName();
				_playerTwoName = _playerTwo.getName();
				_playerOne.setOlympiadGameId(id);
				_playerTwo.setOlympiadGameId(id);
				_playerOneID = _playerOne.getObjectId();
				_playerTwoID = _playerTwo.getObjectId();
				_playerOneClass = _playerOne.getBaseClass();
				_playerTwoClass = _playerTwo.getBaseClass();
			}
			catch (Exception e)
			{
				_aborted = true;
				clearPlayers();
			}
			
			if (Config.DEBUG)
				_log.info("Olympiad System: Game - " + id + ": "
				        + _playerOne.getName() + " Vs " + _playerTwo.getName());
		}
		else
		{
			_aborted = true;
			clearPlayers();
			return;
		}
	}
	
	public boolean isAborted()
	{
		return _aborted;
	}
	
	protected void clearPlayers()
	{
		_playerOne = null;
		_playerTwo = null;
		_players = null;
		_playerOneName = "";
		_playerTwoName = "";
		_playerOneID = 0;
		_playerTwoID = 0;
	}
	
	protected void handleDisconnect(L2PcInstance player)
	{
		if (_gamestarted)
		{
			if (player == _playerOne)
				_playerOneDisconnected = true;
			else if (player == _playerTwo)
				_playerTwoDisconnected = true;
		}
	}
	
	public L2Spawn SpawnBuffer(int xPos, int yPos, int zPos, int npcId)
	{
		final L2NpcTemplate template = NpcTable.getInstance().getTemplate(npcId);
		try
		{
			final L2Spawn spawn = new L2Spawn(template);
			spawn.setLocx(xPos);
			spawn.setLocy(yPos);
			spawn.setLocz(zPos);
			spawn.setAmount(1);
			spawn.setHeading(0);
			spawn.setRespawnDelay(1);
			SpawnTable.getInstance().addNewSpawn(spawn, false);
			spawn.init();
			return spawn;
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "", e);
			return null;
		}
	}
	
	protected void removals()
	{
		if (_aborted)
			return;
		
		if (_playerOne == null || _playerTwo == null)
			return;
		if (_playerOneDisconnected || _playerTwoDisconnected)
			return;
		
		for (L2PcInstance player : _players)
		{
			try
			{
				// Remove Buffs
				player.stopAllEffectsExceptThoseThatLastThroughDeath();
				
				// Remove Clan Skills
				if (player.getClan() != null)
				{
					for (L2Skill skill : player.getClan().getAllSkills())
						player.removeSkill(skill, false, true);
					if (player.getClan().getHasCastle() > 0)
						CastleManager.getInstance().getCastleByOwner(player.getClan()).removeResidentialSkills(player);
					if (player.getClan().getHasFort() > 0)
						FortManager.getInstance().getFortByOwner(player.getClan()).removeResidentialSkills(player);
				}
				// Abort casting if player casting
				if (player.isCastingNow())
				{
					player.abortCast();
				}
				
				// Force the character to be visible
				player.getAppearance().setVisible();
				
				// Remove Hero Skills
				if (player.isHero())
				{
					for (L2Skill skill : HeroSkillTable.getHeroSkills())
						player.removeSkill(skill, false);
				}
				
				// Heal Player fully
				player.setCurrentCp(player.getMaxCp());
				player.setCurrentHp(player.getMaxHp());
				player.setCurrentMp(player.getMaxMp());
				
				// Remove Summon's Buffs
				if (player.getPet() != null)
				{
					L2Summon summon = player.getPet();
					summon.stopAllEffects();
					
					if (summon instanceof L2PetInstance)
						summon.unSummon(player);
				}
				
				// stop any cubic that has been given by other player.
				player.stopCubicsByOthers();
				
				// Remove player from his party
				if (player.getParty() != null)
				{
					L2Party party = player.getParty();
					party.removePartyMember(player);
				}
				// Remove Agathion
				if (player.getAgathionId() > 0)
				{
					player.setAgathionId(0);
					player.broadcastUserInfo();
				}

				player.checkItemRestriction();

				// Remove shot automation
				player.disableAutoShotsAll();
				
				// Discharge any active shots
				if (player.getActiveWeaponInstance() != null)
				{
					player.getActiveWeaponInstance().setChargedSoulshot(L2ItemInstance.CHARGED_NONE);
					player.getActiveWeaponInstance().setChargedSpiritshot(L2ItemInstance.CHARGED_NONE);
				}

				// enable skills with cool time <= 15 minutes
				for (L2Skill skill : player.getAllSkills())
					if (skill.getReuseDelay() <= 900000)
						player.enableSkill(skill);
					
				player.sendSkillList();
				player.sendPacket(new SkillCoolTime(player));
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "", e);
			}
		}
	}
	
	protected boolean portPlayersToArena()
	{
		final boolean _playerOneCrash = (_playerOne == null || _playerOneDisconnected);
		final boolean _playerTwoCrash = (_playerTwo == null || _playerTwoDisconnected);
		
		if (_playerOneCrash || _playerTwoCrash || _aborted)
		{
			_playerOne = null;
			_playerTwo = null;
			_aborted = true;
			return false;
		}
		
		try
		{
			x1 = _playerOne.getX();
			y1 = _playerOne.getY();
			z1 = _playerOne.getZ();
			
			x2 = _playerTwo.getX();
			y2 = _playerTwo.getY();
			z2 = _playerTwo.getZ();
			
			if (_playerOne.isSitting())
				_playerOne.standUp();
			
			if (_playerTwo.isSitting())
				_playerTwo.standUp();
			
			_playerOne.setTarget(null);
			_playerTwo.setTarget(null);
			
			_gamestarted = true;

			_playerOne.setIsInOlympiadMode(true);
			_playerOne.setIsOlympiadStart(false);
			_playerOne.setOlympiadSide(1);
			_playerOne.olyBuff = 5;
			
			_playerTwo.setIsInOlympiadMode(true);
			_playerTwo.setIsOlympiadStart(false);
			_playerTwo.setOlympiadSide(2);
			_playerTwo.olyBuff = 5;

			_playerOne.setInstanceId(0);
			_playerOne.teleToLocation(_stadiumPort[0] + 1200, _stadiumPort[1], _stadiumPort[2], false);
			_playerTwo.setInstanceId(0);
			_playerTwo.teleToLocation(_stadiumPort[0] - 1200, _stadiumPort[1], _stadiumPort[2], false);
			
			_playerOne.sendPacket(new ExOlympiadMode(2));
			_playerTwo.sendPacket(new ExOlympiadMode(2));
			
			_spawnOne = SpawnBuffer(_stadiumPort[0] + 1100, _stadiumPort[1], _stadiumPort[2], OLY_BUFFER);
			_spawnTwo = SpawnBuffer(_stadiumPort[0] - 1100, _stadiumPort[1], _stadiumPort[2], OLY_BUFFER);
			
			_gameIsStarted = false;
		}
		catch (NullPointerException e)
		{
			_log.log(Level.WARNING, "", e);
			return false;
		}
		return true;
	}
	
	protected void cleanEffects()
	{
		if (_playerOne == null || _playerTwo == null)
			return;

		if (_playerOneDisconnected || _playerTwoDisconnected)
			return;

		for (L2PcInstance player : _players)
		{
			try
			{
				player.stopAllEffectsExceptThoseThatLastThroughDeath();
				player.clearSouls();
				player.clearCharges();
				if(player.getAgathionId() > 0)
					player.setAgathionId(0);
				if (player.getPet() != null)
				{
					L2Summon summon = player.getPet();
					summon.stopAllEffects();
				}
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "cleanEffects()", e);
			}
		}
	}

	protected void portPlayersBack()
	{
		if (_playerOne != null)
		{
			_playerOne.sendPacket(new ExOlympiadMatchEnd());
			_playerOne.teleToLocation(x1, y1, z1, true);
		}
		
		if (_playerTwo != null)
		{
			_playerTwo.sendPacket(new ExOlympiadMatchEnd());
			_playerTwo.teleToLocation(x2, y2, z2, true);
		}
	}
	
	protected void PlayersStatusBack()
	{
		for (L2PcInstance player : _players)
		{
			if (player == null)
				continue;
			try
			{
				if (Olympiad.getInstance().playerInStadia(player))
				{
					if (player.isDead())
						player.setIsDead(false);

					player.getStatus().startHpMpRegeneration();
					player.setCurrentCp(player.getMaxCp());
					player.setCurrentHp(player.getMaxHp());
					player.setCurrentMp(player.getMaxMp());
				}

				if(player.isTransformed())
					player.untransform();
				player.setIsInOlympiadMode(false);
				player.setIsOlympiadStart(false);
				player.setOlympiadSide(-1);
				player.setOlympiadGameId(-1);
				player.sendPacket(new ExOlympiadMode(0));
				
				// Add Clan Skills
				if (player.getClan() != null)
				{
					for (L2Skill skill : player.getClan().getAllSkills())
					{
						if (skill.getMinPledgeClass() <= player.getPledgeClass())
							player.addSkill(skill, false);
					}
					if (player.getClan().getHasCastle() > 0)
						CastleManager.getInstance().getCastleByOwner(player.getClan()).giveResidentialSkills(player);
					if (player.getClan().getHasFort() > 0)
						FortManager.getInstance().getFortByOwner(player.getClan()).giveResidentialSkills(player);
				}
				
				// Add Hero Skills
				if (player.isHero())
				{
					for (L2Skill skill : HeroSkillTable.getHeroSkills())
						player.addSkill(skill, false);
				}
				player.sendSkillList();
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "portPlayersToArena()", e);
			}
		}
	}
	
	protected boolean haveWinner()
	{
		if (_aborted || _playerOne == null || _playerTwo == null ||
				_playerOneDisconnected || _playerTwoDisconnected)
		{
			return true;
		}
		
		double playerOneHp = 0;
		
		try
		{
			if (_playerOne != null && _playerOne.getOlympiadGameId() != -1)
			{
				playerOneHp = _playerOne.getCurrentHp();
			}
		}
		catch (Exception e)
		{
			playerOneHp = 0;
		}
		
		double playerTwoHp = 0;
		try
		{
			if (_playerTwo != null && _playerTwo.getOlympiadGameId() != -1)
			{
				playerTwoHp = _playerTwo.getCurrentHp();
			}
		}
		catch (Exception e)
		{
			playerTwoHp = 0;
		}
		
		if (playerTwoHp <= 0 || playerOneHp <= 0)
		{
			return true;
		}
		
		return false;
	}
	
	protected void validateWinner()
	{
		if (_aborted)
			return;
		
		final boolean _pOneCrash = (_playerOne == null || _playerOneDisconnected);
		final boolean _pTwoCrash = (_playerTwo == null || _playerTwoDisconnected);
		
		final int _div;
		final int _gpreward;
		
		final String classed;
		switch (_type)
		{
			case NON_CLASSED:
				_div = 5;
				_gpreward = Config.ALT_OLY_NONCLASSED_RITEM_C;
				classed = "no";
				break;
			default:
				_div = 3;
				_gpreward = Config.ALT_OLY_CLASSED_RITEM_C;
				classed = "yes";
				break;
		}

		final StatsSet playerOneStat = Olympiad.getNobleStats(_playerOneID);
		final StatsSet playerTwoStat = Olympiad.getNobleStats(_playerTwoID);
		
		final int playerOnePlayed = playerOneStat.getInteger(COMP_DONE);
		final int playerTwoPlayed = playerTwoStat.getInteger(COMP_DONE);
		final int playerOneWon = playerOneStat.getInteger(COMP_WON);
		final int playerTwoWon = playerTwoStat.getInteger(COMP_WON);
		final int playerOneLost = playerOneStat.getInteger(COMP_LOST);
		final int playerTwoLost = playerTwoStat.getInteger(COMP_LOST);
		final int playerOneDrawn = playerOneStat.getInteger(COMP_DRAWN);
		final int playerTwoDrawn = playerTwoStat.getInteger(COMP_DRAWN);
		
		final int playerOnePoints = playerOneStat.getInteger(POINTS);
		final int playerTwoPoints = playerTwoStat.getInteger(POINTS);
		final int pointDiff = Math.min(Math.min(playerOnePoints, playerTwoPoints) / _div, Config.ALT_OLY_MAX_POINTS);

		// Check for if a player defaulted before battle started
		if (_playerOneDefaulted || _playerTwoDefaulted)
		{
			if (_playerOneDefaulted)
			{
				final int lostPoints = Math.min(playerOnePoints / 3, Config.ALT_OLY_MAX_POINTS);
				playerOneStat.set(POINTS, playerOnePoints - lostPoints);
				Olympiad.updateNobleStats(_playerOneID, playerOneStat);
				SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_LOST_S2_OLYMPIAD_POINTS);
				sm.addString(_playerOneName);
				sm.addNumber(lostPoints);
				broadcastMessage(sm, false);

				if (Config.DEBUG)
					_log.info("Olympia Result: " + _playerOneName + " lost " + lostPoints + " points for defaulting");

				if (Config.ALT_OLY_LOG_FIGHTS)
				{
					LogRecord record = new LogRecord(Level.INFO, _playerOneName+" default");
					record.setParameters(new Object[]{_playerOneName, _playerTwoName, 0, 0, 0, 0, lostPoints, classed});
					_logResults.log(record);
				}
			}
			if (_playerTwoDefaulted)
			{
				final int lostPoints = Math.min(playerTwoPoints / 3, Config.ALT_OLY_MAX_POINTS);
				playerTwoStat.set(POINTS, playerTwoPoints - lostPoints);
				Olympiad.updateNobleStats(_playerTwoID, playerTwoStat);
				SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_LOST_S2_OLYMPIAD_POINTS);
				sm.addString(_playerTwoName);
				sm.addNumber(lostPoints);
				broadcastMessage(sm, false);

				if (Config.DEBUG)
					_log.info("Olympia Result: " + _playerTwoName + " lost " + lostPoints + " points for defaulting");
				
				if (Config.ALT_OLY_LOG_FIGHTS)
				{
					LogRecord record = new LogRecord(Level.INFO, _playerTwoName+" default");
					record.setParameters(new Object[]{_playerOneName, _playerTwoName, 0, 0, 0, 0, lostPoints, classed});
					_logResults.log(record);
				}
			}
			return;
		}
		
		// Create results for players if a player crashed
		if (_pOneCrash || _pTwoCrash)
		{
			if (_pOneCrash && !_pTwoCrash)
			{
				try
				{
					playerOneStat.set(POINTS, playerOnePoints - pointDiff);
					playerOneStat.set(COMP_LOST, playerOneLost + 1);
					
					if (Config.DEBUG)
						_log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... "
						        + _playerOneName + " lost " + pointDiff + " points for crash");
					
					if (Config.ALT_OLY_LOG_FIGHTS)
					{
						LogRecord record = new LogRecord(Level.INFO, _playerOneName+" crash");
						record.setParameters(new Object[]{_playerOneName, _playerTwoName, 0, 0, 0, 0, pointDiff, classed});
						_logResults.log(record);
					}
					
					playerTwoStat.set(POINTS, playerTwoPoints + pointDiff);
					playerTwoStat.set(COMP_WON, playerTwoWon + 1);
					
					if (Config.DEBUG)
						_log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... "
						        + _playerTwoName + " Win " + pointDiff + " points");
					
					_sm = new SystemMessage(SystemMessageId.C1_HAS_WON_THE_GAME);
					_sm2 = new SystemMessage(SystemMessageId.C1_HAS_GAINED_S2_OLYMPIAD_POINTS);
					_sm.addString(_playerTwoName);
					broadcastMessage(_sm, true);
					_sm2.addString(_playerTwoName);
					_sm2.addNumber(pointDiff);
					broadcastMessage(_sm2, false);
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "Exception on validateWinnder(): " + e.getMessage(), e);
				}
				
			}
			else if (_pTwoCrash && !_pOneCrash)
			{
				try
				{
					playerTwoStat.set(POINTS, playerTwoPoints - pointDiff);
					playerTwoStat.set(COMP_LOST, playerTwoLost + 1);
					
					if (Config.DEBUG)
						_log.info("Olympia Result: " + _playerTwoName + " vs " + _playerOneName + " ... " 
								+ _playerTwoName + " lost " + pointDiff + " points for crash");
					
					if (Config.ALT_OLY_LOG_FIGHTS)
					{
						LogRecord record = new LogRecord(Level.INFO, _playerTwoName+" crash");
						record.setParameters(new Object[]{_playerOneName, _playerTwoName, 0, 0, 0, 0, pointDiff, classed});
						_logResults.log(record);
					}
					
					playerOneStat.set(POINTS, playerOnePoints + pointDiff);
					playerOneStat.set(COMP_WON, playerOneWon + 1);
					
					if (Config.DEBUG)
						_log.info("Olympia Result: " + _playerTwoName + " vs " + _playerOneName + " ... "
						        + _playerOneName + " Win " + pointDiff + " points");
					
					_sm = new SystemMessage(SystemMessageId.C1_HAS_WON_THE_GAME);
					_sm2 = new SystemMessage(SystemMessageId.C1_HAS_GAINED_S2_OLYMPIAD_POINTS);
					_sm.addString(_playerOneName);
					broadcastMessage(_sm, true);
					_sm2.addString(_playerOneName);
					_sm2.addNumber(pointDiff);
					broadcastMessage(_sm2, false);
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "Exception on validateWinnder(): " + e.getMessage(), e);
				}
			}
			else if (_pOneCrash && _pTwoCrash)
			{
				try
				{
					playerOneStat.set(POINTS, playerOnePoints - pointDiff);
					playerOneStat.set(COMP_LOST, playerOneLost + 1);
					
					playerTwoStat.set(POINTS, playerTwoPoints - pointDiff);
					playerTwoStat.set(COMP_LOST, playerTwoLost + 1);
					
					if (Config.DEBUG)
						_log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... " 
								+ " both lost " + pointDiff + " points for crash");
					
					if (Config.ALT_OLY_LOG_FIGHTS)
					{
						LogRecord record = new LogRecord(Level.INFO, "both crash");
						record.setParameters(new Object[]{_playerOneName, _playerTwoName, 0, 0, 0, 0, pointDiff, classed});
						_logResults.log(record);
					}
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "Exception on validateWinnder(): " + e.getMessage(), e);
				}
			}
			playerOneStat.set(COMP_DONE, playerOnePlayed + 1);
			playerTwoStat.set(COMP_DONE, playerTwoPlayed + 1);
			
			Olympiad.updateNobleStats(_playerOneID, playerOneStat);
			Olympiad.updateNobleStats(_playerTwoID, playerTwoStat);
			
			return;
		}
		
		double playerOneHp = 0;
		if (!_playerOne.isDead())
		{
			playerOneHp = _playerOne.getCurrentHp() + _playerOne.getCurrentCp();
		}
		
		double playerTwoHp = 0;
		if (!_playerTwo.isDead())
		{
			playerTwoHp = _playerTwo.getCurrentHp() + _playerTwo.getCurrentCp();
		}
		
		_sm = new SystemMessage(SystemMessageId.C1_HAS_WON_THE_GAME);
		_sm2 = new SystemMessage(SystemMessageId.C1_HAS_GAINED_S2_OLYMPIAD_POINTS);
		_sm3 = new SystemMessage(SystemMessageId.C1_HAS_LOST_S2_OLYMPIAD_POINTS);
		
		String result = "";
		
		// if players crashed, search if they've relogged
		_playerOne = L2World.getInstance().getPlayer(_playerOneID);
		_players.set(0, _playerOne);
		_playerTwo = L2World.getInstance().getPlayer(_playerTwoID);
		_players.set(1, _playerTwo);
		
		String winner = "draw";

		// Calculate Fight time
		long _fightTime = (System.currentTimeMillis() - _startTime);
		
		if (_playerOne == null && _playerTwo == null)
		{
			playerOneStat.set(COMP_DRAWN, playerOneDrawn + 1);
			playerTwoStat.set(COMP_DRAWN, playerTwoDrawn + 1);
			result = " tie";
			_sm = new SystemMessage(SystemMessageId.THE_GAME_ENDED_IN_A_TIE);
			broadcastMessage(_sm, true);
		}
		else if (_playerTwo == null
		        || _playerTwo.isOnline() == 0
		        || (playerTwoHp == 0 && playerOneHp != 0)
		        || (_damageP1 > _damageP2 && playerTwoHp != 0 && playerOneHp != 0))
		{
			playerOneStat.set(POINTS, playerOnePoints + pointDiff);
			playerTwoStat.set(POINTS, playerTwoPoints - pointDiff);
			playerOneStat.set(COMP_WON, playerOneWon + 1);
			playerTwoStat.set(COMP_LOST, playerTwoLost + 1);
			
			_sm.addString(_playerOneName);
			broadcastMessage(_sm, true);
			_sm2.addString(_playerOneName);
			_sm2.addNumber(pointDiff);
			broadcastMessage(_sm2, false);
			_sm3.addString(_playerTwoName);
			_sm3.addNumber(pointDiff);
			broadcastMessage(_sm3, false);
			winner = _playerOneName + " won";
			
			try
			{
				// Save Fight Result
				saveResults(_playerOneID,_playerTwoID,_playerOneClass,_playerTwoClass,1,_startTime,_fightTime, (_type == COMP_TYPE.CLASSED ? 1 : 0));
				
				result = " (" + playerOneHp + "hp vs " + playerTwoHp + "hp - "
				        + _damageP1 + "dmg vs " + _damageP2 + "dmg) "
				        + _playerOneName + " win " + pointDiff + " points";
				L2ItemInstance item = _playerOne.getInventory().addItem("Olympiad", Config.ALT_OLY_BATTLE_REWARD_ITEM, _gpreward, _playerOne, null);
				InventoryUpdate iu = new InventoryUpdate();
				iu.addModifiedItem(item);
				_playerOne.sendPacket(iu);
				
				SystemMessage sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
				sm.addItemName(item);
				sm.addNumber(_gpreward);
				_playerOne.sendPacket(sm);
			}
			catch (Exception e)
			{
			}
		}
		else if (_playerOne == null
		        || _playerOne.isOnline() == 0
		        || (playerOneHp == 0 && playerTwoHp != 0)
		        || (_damageP2 > _damageP1 && playerOneHp != 0 && playerTwoHp != 0))
		{
			playerTwoStat.set(POINTS, playerTwoPoints + pointDiff);
			playerOneStat.set(POINTS, playerOnePoints - pointDiff);
			playerTwoStat.set(COMP_WON, playerTwoWon + 1);
			playerOneStat.set(COMP_LOST, playerOneLost + 1);
			
			_sm.addString(_playerTwoName);
			broadcastMessage(_sm, true);
			_sm2.addString(_playerTwoName);
			_sm2.addNumber(pointDiff);
			broadcastMessage(_sm2, false);
			_sm3.addString(_playerOneName);
			_sm3.addNumber(pointDiff);
			broadcastMessage(_sm3, false);
			winner = _playerTwoName + " won";
			
			try
			{
				// Save Fight Result
				saveResults(_playerOneID,_playerTwoID,_playerOneClass,_playerTwoClass,2,_startTime,_fightTime,(_type == COMP_TYPE.CLASSED ? 1 : 0));

				result = " (" + playerOneHp + "hp vs " + playerTwoHp + "hp - "
				        + _damageP1 + "dmg vs " + _damageP2 + "dmg) "
				        + _playerTwoName + " win " + pointDiff + " points";
				L2ItemInstance item = _playerTwo.getInventory().addItem("Olympiad", Config.ALT_OLY_BATTLE_REWARD_ITEM, _gpreward, _playerTwo, null);
				InventoryUpdate iu = new InventoryUpdate();
				iu.addModifiedItem(item);
				_playerTwo.sendPacket(iu);
				
				SystemMessage sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
				sm.addItemName(item);
				sm.addNumber(_gpreward);
				_playerTwo.sendPacket(sm);
			}
			catch (Exception e)
			{
			}
		}
		else
		{
			// Save Fight Result
			saveResults(_playerOneID,_playerTwoID,_playerOneClass,_playerTwoClass,0,_startTime,_fightTime,(_type == COMP_TYPE.CLASSED ? 1 : 0));
			
			result = " tie";
			_sm = new SystemMessage(SystemMessageId.THE_GAME_ENDED_IN_A_TIE);
			broadcastMessage(_sm, true);
			final int pointOneDiff = Math.min(playerOnePoints / 5, Config.ALT_OLY_MAX_POINTS);
			final int pointTwoDiff = Math.min(playerTwoPoints / 5, Config.ALT_OLY_MAX_POINTS);
		 	playerOneStat.set(POINTS, playerOnePoints - pointOneDiff);
		 	playerTwoStat.set(POINTS, playerTwoPoints - pointTwoDiff);
		 	playerOneStat.set(COMP_DRAWN, playerOneDrawn + 1);
			playerTwoStat.set(COMP_DRAWN, playerTwoDrawn + 1);
			_sm2 = new SystemMessage(SystemMessageId.C1_HAS_LOST_S2_OLYMPIAD_POINTS);
		 	_sm2.addString(_playerOneName);
		 	_sm2.addNumber(pointOneDiff);
		 	broadcastMessage(_sm2, false);
		 	_sm3 = new SystemMessage(SystemMessageId.C1_HAS_LOST_S2_OLYMPIAD_POINTS);
		 	_sm3.addString(_playerTwoName);
		 	_sm3.addNumber(pointTwoDiff);
		 	broadcastMessage(_sm3, false);
		}
		
		if (Config.DEBUG)
			_log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... " + result);
		
		playerOneStat.set(COMP_DONE, playerOnePlayed + 1);
		playerTwoStat.set(COMP_DONE, playerTwoPlayed + 1);
		
		Olympiad.updateNobleStats(_playerOneID, playerOneStat);
		Olympiad.updateNobleStats(_playerTwoID, playerTwoStat);
		
		if (Config.ALT_OLY_LOG_FIGHTS)
		{
			LogRecord record = new LogRecord(Level.INFO, winner);
			record.setParameters(new Object[]{_playerOneName, _playerTwoName, playerOneHp, playerTwoHp, _damageP1, _damageP2, pointDiff, classed});
			_logResults.log(record);
		}

		byte step = 10;
		for (byte i = 40; i > 0; i -= step)
		{
			_sm = new SystemMessage(SystemMessageId.YOU_WILL_BE_MOVED_TO_TOWN_IN_S1_SECONDS);
			_sm.addNumber(i);
			broadcastMessage(_sm, false);
			switch (i)
			{
				case 10:
					step = 5;
					break;
				case 5:
					step = 1;
					break;
			}
			try
			{
				Thread.sleep(step*1000);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	protected boolean makeCompetitionStart()
	{
		_startTime = System.currentTimeMillis();
		if (_aborted)
			return false;
		
		_sm = new SystemMessage(SystemMessageId.STARTS_THE_GAME);
		broadcastMessage(_sm, true);
		_gameIsStarted = true;
		try
		{
			for (L2PcInstance player : _players)
			{
				player.setIsOlympiadStart(true);
				player.updateEffectIcons();
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "", e);
			_aborted = true;
			return false;
		}
		return true;
	}
	
	protected void addDamage(L2PcInstance player, int damage)
	{
		if (_playerOne == null || _playerTwo == null)
			return;
		if (player == _playerOne)
			_damageP1 += damage;
		else if (player == _playerTwo)
			_damageP2 += damage;
	}
	
	protected String getTitle()
	{
		final String msg = _playerOneName + " / " + _playerTwoName;
		return msg;
	}
	
	protected L2PcInstance[] getPlayers()
	{
		if (_players == null || _players.isEmpty())
			return null;

		final L2PcInstance[] players = new L2PcInstance[_players.size()];
		_players.toArray(players);

		return players;
	}
	
	protected void broadcastMessage(SystemMessage sm, boolean toAll)
	{
		for (L2PcInstance player : _players)
		{
			if (player != null)
			{
				player.sendPacket(sm);
			}
		}
		
		if (toAll && OlympiadManager.STADIUMS[_stadiumID].getSpectators() != null)
		{
			for (L2PcInstance spec : OlympiadManager.STADIUMS[_stadiumID].getSpectators())
			{
				if (spec != null)
					spec.sendPacket(sm);
			}
		}
	}
	
	protected void announceGame() 
 	{
		int objId;
		String npcName;
		String gameType = null;
		switch (_type)
		{
			case NON_CLASSED:
				gameType = "class-free individual match";
				break;
			default:
				gameType = "class-specific individual match";
				break;
		}
		
		for (L2Spawn manager : SpawnTable.getInstance().getSpawnTable().values())
		{
			if (manager != null && manager.getNpcid() == OLY_MANAGER)
			{
				objId = manager.getLastSpawn().getObjectId();
				npcName = manager.getLastSpawn().getName();
				manager.getLastSpawn().broadcastPacket(new CreatureSay(objId, Say2.SHOUT, npcName, "Olympiad " + gameType + " is going to begin in Arena " + (_stadiumID + 1) + " in a moment."));
			}
		}
	}
	
	protected void saveResults(int _playerOne, int _playerTwo, int _playerOneClass, int _playerTwoClass, int _winner, long _startTime, long _fightTime, int _classed)
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO olympiad_fights (charOneId, charTwoId, charOneClass, charTwoClass, winner, start, time, classed) values(?,?,?,?,?,?,?,?)");
			statement.setInt(1, _playerOne);
			statement.setInt(2, _playerTwo);
			statement.setInt(3, _playerOneClass);
			statement.setInt(4, _playerTwoClass);
			statement.setInt(5, _winner);
			statement.setLong(6, _startTime);
			statement.setLong(7, _fightTime);
			statement.setInt(8, _classed);
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			if(_log.isLoggable(Level.SEVERE))
				_log.log(Level.SEVERE, "SQL exception while saving olympiad fight.", e);
		}
		finally
		{
			L2DatabaseFactory.close(con);
		}
	}
}

/**
 * 
 * @author ascharot
 * 
 */
class OlympiadGameTask implements Runnable
{
	protected static final Logger _log = Logger.getLogger(OlympiadGameTask.class.getName());
	public OlympiadGame _game = null;
	protected static final long BATTLE_PERIOD = Config.ALT_OLY_BATTLE; // 6 mins
	
	private boolean _terminated = false;
	private boolean _started = false;
	
	public boolean isTerminated()
	{
		return _terminated || _game._aborted;
	}
	
	public boolean isStarted()
	{
		return _started;
	}
	
	public OlympiadGameTask(OlympiadGame game)
	{
		_game = game;
	}
	
	protected boolean checkBattleStatus()
	{
		boolean _pOneCrash = (_game._playerOne == null || _game._playerOneDisconnected);
		boolean _pTwoCrash = (_game._playerTwo == null || _game._playerTwoDisconnected);
		if (_pOneCrash || _pTwoCrash || _game._aborted)
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean checkDefaulted()
	{
		_game._playerOne = L2World.getInstance().getPlayer(_game._playerOneID);
		_game._players.set(0, _game._playerOne);
		_game._playerTwo = L2World.getInstance().getPlayer(_game._playerTwoID);
		_game._players.set(1, _game._playerTwo);
		
		for (int i = 0; i < 2; i++)
		{
			boolean defaulted = false;
			L2PcInstance player = _game._players.get(i);
			if (player != null)
				player.setOlympiadGameId(_game._stadiumID);
			L2PcInstance otherPlayer = _game._players.get(i^1);
			SystemMessage sm = null;
			
			if (player == null)
			{
				defaulted = true;
			}
			else if (player.isDead())
			{
				sm = new SystemMessage(SystemMessageId.C1_CANNOT_PARTICIPATE_OLYMPIAD_WHILE_DEAD);
				sm.addPcName(player);
				defaulted = true;
			}
			else if (player.isSubClassActive())
			{
				sm = new SystemMessage(SystemMessageId.C1_CANNOT_PARTICIPATE_IN_OLYMPIAD_WHILE_CHANGED_TO_SUB_CLASS);
				sm.addPcName(player);
				defaulted = true;
			}
			else if (player.isCursedWeaponEquipped())
			{
				sm = new SystemMessage(SystemMessageId.C1_CANNOT_JOIN_OLYMPIAD_POSSESSING_S2);
				sm.addPcName(player);
				sm.addItemName(player.getCursedWeaponEquippedId());
				defaulted = true;
			}
			else if (player.getInventoryLimit()*0.8 <= player.getInventory().getSize())
			{
				sm = new SystemMessage(SystemMessageId.C1_CANNOT_PARTICIPATE_IN_OLYMPIAD_INVENTORY_SLOT_EXCEEDS_80_PERCENT);
				sm.addPcName(player);
				defaulted = true;
			}
			
			if (defaulted)
			{
				if (player != null)
					player.sendPacket(sm);
				if (otherPlayer != null)
					otherPlayer.sendPacket(new SystemMessage(SystemMessageId.THE_GAME_HAS_BEEN_CANCELLED_BECAUSE_THE_OTHER_PARTY_DOES_NOT_MEET_THE_REQUIREMENTS_FOR_JOINING_THE_GAME));
				if (i == 0)
					_game._playerOneDefaulted = true;
				else
					_game._playerTwoDefaulted = true;
			}
		}
		return _game._playerOneDefaulted || _game._playerTwoDefaulted;
	}
	
	public void run()
	{
		_started = true;
		if (_game != null)
		{
			if (_game._playerOne == null || _game._playerTwo == null)
			{
				return;
			}
			
			if (teleportCountdown())
				runGame();
			
			_terminated = true;
			_game.validateWinner();
			_game.PlayersStatusBack();
			_game.cleanEffects();
			
			if (_game._gamestarted)
			{
				_game._gamestarted = false;
				OlympiadManager.STADIUMS[_game._stadiumID].closeDoors();
				try
				{
					_game.portPlayersBack();
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "Exception on portPlayersBack(): " + e.getMessage(), e);
				}
			}

			if (OlympiadManager.STADIUMS[_game._stadiumID].getSpectators() != null)
			{
				for (L2PcInstance spec : OlympiadManager.STADIUMS[_game._stadiumID].getSpectators())
				{
					if (spec != null)
						spec.sendPacket(new ExOlympiadMatchEnd());
				}
			}
			
			if (_game._spawnOne != null)
			{
				_game._spawnOne.getLastSpawn().deleteMe();
				_game._spawnOne = null;
			}
			if (_game._spawnTwo != null)
			{
				_game._spawnTwo.getLastSpawn().deleteMe();
				_game._spawnTwo = null;
			}
			
			_game.clearPlayers();
			OlympiadManager.getInstance().removeGame(_game);
			_game = null;
		}
	}
	
	private boolean runGame()
	{
		SystemMessage sm;
		// Checking for opponents and teleporting to arena
		if (checkDefaulted())
		{
			return false;
		}
		OlympiadManager.STADIUMS[_game._stadiumID].closeDoors();
		_game.portPlayersToArena();
		_game.removals();
		if (Config.ALT_OLY_ANNOUNCE_GAMES)
			_game.announceGame();
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
		}
		
		synchronized (this)
		{
			if (!OlympiadGame._battleStarted)
				OlympiadGame._battleStarted = true;
		}

		byte step = 10;
		for (byte i = 60; i > 0; i -= step)
		{
			sm = new SystemMessage(SystemMessageId.THE_GAME_WILL_START_IN_S1_SECOND_S);
			sm.addNumber(i);
			_game.broadcastMessage(sm, true);

			switch (i)
			{
				case 10:
					_game._damageP1 = 0;
					_game._damageP2 = 0;
					OlympiadManager.STADIUMS[_game._stadiumID].openDoors();
					step = 5;
					break;
				case 5:
					step = 1;
					break;
			}

			try
			{
				Thread.sleep(step*1000);
			}
			catch (InterruptedException e)
			{
			}
		}
		
		if (!checkBattleStatus())
		{
			return false;
		}

		_game._spawnOne.getLastSpawn().deleteMe();
		_game._spawnTwo.getLastSpawn().deleteMe();
		_game._spawnOne = null;
		_game._spawnTwo = null;
		
		if (!_game.makeCompetitionStart())
		{
			return false;
		}
		
		_game._playerOne.sendPacket(new ExOlympiadUserInfo(_game._playerOne));
		_game._playerOne.sendPacket(new ExOlympiadUserInfo(_game._playerTwo));
		_game._playerTwo.sendPacket(new ExOlympiadUserInfo(_game._playerTwo));
		_game._playerTwo.sendPacket(new ExOlympiadUserInfo(_game._playerOne));
		if (OlympiadManager.STADIUMS[_game._stadiumID].getSpectators() != null)
		{
			for (L2PcInstance spec : OlympiadManager.STADIUMS[_game._stadiumID].getSpectators())
			{
				if (spec != null)
				{
					spec.sendPacket(new ExOlympiadUserInfo(_game._playerOne));
					spec.sendPacket(new ExOlympiadUserInfo(_game._playerTwo));
				}
			}
		}
		
		// Wait 3 mins (Battle)
		for (int i = 0; i < BATTLE_PERIOD; i += 10000)
		{
			try
			{
				Thread.sleep(10000);
				// If game haveWinner then stop waiting battle_period
				// and validate winner
				if (_game.haveWinner())
					break;
			}
			catch (InterruptedException e)
			{
			}
		}
		
		return checkBattleStatus();
	}
	
	private boolean teleportCountdown()
	{
		SystemMessage sm;
		// Waiting for teleport to arena
		byte step = 60;
		for (byte i = Config.ALT_OLY_WAIT_TIME; i > 0; i -= step)
		{
			sm = new SystemMessage(SystemMessageId.YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S);
			sm.addNumber(i);
			_game.broadcastMessage(sm, false);

			switch (i)
			{
				case 60:
					step = 30;
					break;
				case 30:
					step = 15;
					break;
				case 15:
					step = 5;
					break;
				case 5:
					step = 1;
					break;
			}
			try
			{
				Thread.sleep(step*1000);
			}
			catch (InterruptedException e)
			{
				return false;
			}
		}

		return true;
	}
}
