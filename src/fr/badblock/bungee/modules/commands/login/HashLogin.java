package fr.badblock.bungee.modules.commands.login;

import fr.badblock.api.common.utils.general.HashUtils;
import fr.badblock.bungee.modules.login.events.PlayerLoggedEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.BungeeCord;

public class HashLogin {

	private static final String salt = "NkZm8d==xnzFe3=R5d+bdcumv8_%!6!58QEGnb!5Hd-fckVMqjgqvvYnt_5pvJ#8fhn4*YeYp&9QB?G7C7GK^pYde&_nUuQxz=**57@8FvK--5jruExe?53-+z*8x$LC3QznUHKZKV8#HJDM2Wm92WxPdrRk#rspUpkT_PrDBD6VG5qvBkfDj8bZ5W%duk!E85cWN+^2dyLQ@ZyAUX9nBh_vEA_&?$^C=By?^Q2fLM^$gUezwsFccL^q$+=rNGYS=B5zbJD-DGwUX*Tc^bWeE&wJGy8*pRwX2b2p7GL=&C7_!Z2EnxVWy*e&5j8m?8+8MWMNSfVQB?Vy-p-aNQDUpc8!GaZ8KgFTketg=jHyF43aSrSDBv=#TqPruDANrd3sucvr88w^_8j!xLEd?TG4wPp9rkWEvjMD?&$wGc$&pS5rDCGbfyxj8p8Hmy66RqaCXwA!#_HQSh$Hty_#Rj7N-yFx=rbzFmazt5+$Gua$gccLdTedVmkV-ZJ%QXBcDjT?";

	public static String hash(String password) {
		return HashUtils.sha512(password, salt);
	}

	public static void log(BadPlayer badPlayer) {
		// Set login step ok
		badPlayer.setLoginStepOk(true);

		System.out.println("isNew: " + badPlayer.isNew());
		// Call logged event
		BungeeCord.getInstance().getPluginManager().callEvent(new PlayerLoggedEvent(badPlayer));
	}

	public static boolean isValidPassword(String password) {
		boolean atleastOneUpper = false;
		boolean atleastOneLower = false;
		boolean atleastOneDigit = false;

		if (password.length() < 8) {
			return false;
		}

		for (int i = 0; i < password.length(); i++) {
			if (Character.isUpperCase(password.charAt(i))) {
				atleastOneUpper = true;
			} else if (Character.isLowerCase(password.charAt(i))) {
				atleastOneLower = true;
			} else if (Character.isDigit(password.charAt(i))) {
				atleastOneDigit = true;
			}
		}

		return (atleastOneUpper && atleastOneLower && atleastOneDigit);
	}

	/**
	 * Send login message
	 * 
	 * @param badPlayer
	 */
	public static void sendLoginMessage(BadPlayer badPlayer) {
		if (badPlayer.getLoginPassword() == null || badPlayer.getLoginPassword().isEmpty()) {
			badPlayer.sendTranslatedOutgoingMessage("bungee.commands.register.usage", null, badPlayer.getName());
			return;
		}

		badPlayer.sendTranslatedOutgoingMessage("bungee.commands.login.usage", null, badPlayer.getName());
	}

}
