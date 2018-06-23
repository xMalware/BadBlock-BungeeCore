package fr.badblock.bungee.modules.commands.modo.punishments;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;

@Getter
public enum PunishmentType {

	BAN(new PunishmentWorkerBan()), BANIP(new PunishmentWorkerBanIp()), KICK(new PunishmentWorkerKick()), MUTE(
			new PunishmentWorkerMute()), UNBAN(new PunishmentWorkerUnban()), UNBANIP(
					new PunishmentWorkerUnbanIp()), UNMUTE(
							new PunishmentWorkerUnmute()), WARN(new PunishmentWorkerWarn());

	private PunishmentWorker punishmentWorker;

	PunishmentType(PunishmentWorker punishmentWorker) {
		setPunishmentWorker(punishmentWorker);
	}

	public void setPunishmentWorker(PunishmentWorker punishmentWorker) {
		this.punishmentWorker = punishmentWorker;
	}

	public void process(CommandSender sender, String playerName, String reason, boolean isKey, long time) {
		getPunishmentWorker().process(sender, playerName, reason, isKey, time);
	}

	public void process(PunishmentPacket punishmentPacket) {
		getPunishmentWorker().process(punishmentPacket.getSender(), punishmentPacket.getPlayerName(),
				punishmentPacket.getReason(), punishmentPacket.isKey(), punishmentPacket.getTime());
	}

}