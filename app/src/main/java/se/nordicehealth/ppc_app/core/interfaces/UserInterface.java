package se.nordicehealth.ppc_app.core.interfaces;

import java.util.List;

import se.nordicehealth.ppc_app.core.containers.ViewDataContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

public interface UserInterface
{
	void displayError(String message, boolean popup);
	void displayMessage(String message, boolean popup);
	boolean presentForm(List<FormContainer> form, FormControl requester, boolean displayMultiple);
	boolean presentViewData(ViewDataContainer vdc);
	FormComponentDisplay getContainerDisplay(FormContainer fc);

	interface FormComponentDisplay
	{
		boolean fillEntry();
		boolean entryIsFilled();
	}
}
