package se.nordicehealth.ppc_app.core.interfaces;

import se.nordicehealth.ppc_app.implementation.EmailRegistration;
import se.nordicehealth.ppc_app.implementation.res.Resource;
import se.nordicehealth.ppc_app.implementation.io.PacketHandler;

public abstract class Implementations
{
	public static Server Server() {
		return PacketHandler.instance;
	}

	public static Registration Registration(UserInterface ui) {
		return new EmailRegistration(ui);
	}

	public static Messages Messages() {
		return Resource.messages();
	}
}
