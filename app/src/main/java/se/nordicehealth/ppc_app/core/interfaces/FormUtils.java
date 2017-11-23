package se.nordicehealth.ppc_app.core.interfaces;

import java.util.List;

import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

public interface FormUtils
{
	RetFunContainer validateUserInput(List<FormContainer> form);

	void callNext();

	class RetFunContainer
	{
		public boolean valid;
		public String message;
	}
}
