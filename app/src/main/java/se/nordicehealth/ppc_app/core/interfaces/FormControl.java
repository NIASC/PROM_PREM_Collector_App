package se.nordicehealth.ppc_app.core.interfaces;

import java.util.List;

import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

public interface FormControl
{
	ValidationStatus validateUserInput(List<FormContainer> form);
	void callNext();

	class ValidationStatus
	{
		public boolean valid;
		public String message;
	}
}
