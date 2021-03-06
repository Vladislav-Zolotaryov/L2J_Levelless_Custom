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
package com.l2jserver.gameserver.skills.conditions;

import com.l2jserver.gameserver.skills.Env;


/**
 * The Class ConditionTargetNpcType.
 */
public class ConditionTargetNpcType extends Condition {

	private final String[] _npcType;

	/**
	 * Instantiates a new condition target npc type.
	 *
	 * @param type the type
	 */
	public ConditionTargetNpcType(String[] type)
	{
		_npcType = type;
	}

	/* (non-Javadoc)
	 * @see com.l2jserver.gameserver.skills.conditions.Condition#testImpl(com.l2jserver.gameserver.skills.Env)
	 */
	@Override
	public boolean testImpl(Env env) {
		if (env.target == null)
			return false;
		boolean mt;
		for (int i = 0; i < _npcType.length;i++)
		{
			mt = (env.target.getClass().getName().endsWith(_npcType[i] + "Instance"));
			if (mt)
				return true;
		}
		return false;
	}
}
