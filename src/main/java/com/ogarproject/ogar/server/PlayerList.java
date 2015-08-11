/**
 * This file is part of Ogar.
 *
 * Ogar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ogar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ogar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ogarproject.ogar.server;

import com.ogarproject.ogar.server.world.PlayerImpl;
import com.ogarproject.ogar.server.net.packet.Packet;
import com.google.common.collect.ImmutableSet;
import com.ogarproject.ogar.server.entity.impl.CellEntityImpl;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlayerList {

    private final OgarServer server;
    private final Set<PlayerImpl> players = new HashSet<>();

    public PlayerList(OgarServer server) {
        this.server = server;
    }

    public Collection<PlayerImpl> getAllPlayers() {
        return players;
    }

    public void addPlayer(PlayerImpl player) {
        players.add(player);
    }

    public void removePlayer(PlayerImpl player) {
        players.remove(player);
        if (player != null) {
            for (CellEntityImpl cell : player.getCells()) {
                server.getWorld().removeEntity(cell);
            }
        }
    }

    public void sendToAll(Packet packet, PlayerImpl... except) {
        Set<PlayerImpl> excludes = ImmutableSet.copyOf(except);

        getAllPlayers().stream().filter((p) -> !excludes.contains(p)).forEach((p) -> p.getConnection().sendPacket(packet));
    }
}
