package se.nordicehealth.ppc_app.core.containers;

public class Patient
{
	public Patient(String forename, String surname, String pnr)
	{
		this.forename = forename.trim().toLowerCase();
		this.surname = surname.trim().toLowerCase();
		this.pnr = pnr;
	}

	public String getForename()
	{
		return forename;
	}

	public String getSurname()
	{
		return surname;
	}

	public String getPersonalNumber()
	{
		return pnr;
	}
	
	private final String forename, surname, pnr;
}