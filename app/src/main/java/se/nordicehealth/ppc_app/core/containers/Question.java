package se.nordicehealth.ppc_app.core.containers;

import java.util.List;

import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;

public class Question
{
    public int getID() { return id; }

    public boolean isOptional() { return optional; }

    public String getStatement() { return question; }

    public String getDescription() { return description; }

    public String getOption(int id)
    {
        try {
            return options.get(id);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Question(int id, String type, String question, String description,
                     List<String> options, boolean optional, Integer upper, Integer lower)
    {
        this.id = id;
        this.type = type;
        this.question = question;
        this.description = description;
        this.options = options;
        this.optional = optional;
        this.upper = upper;
        this.lower = lower;
    }

    public FormContainer getContainer()
    {
        if (type == null)
            return null;
			
        if (type.equalsIgnoreCase("SingleOption")) {
            SingleOptionContainer soc = new SingleOptionContainer(optional, question, description);
            int i = 0;
            for (String str : options)
                soc.addOption(i++, str);
            return soc;
        } else if (type.equalsIgnoreCase("MultipleOption")) {
            MultipleOptionContainer moc = new MultipleOptionContainer(optional, question, description);
            int i = 0;
            for (String str : options)
                moc.addOption(i++, str);
            return moc;
        } else if (type.equalsIgnoreCase("Area"))
            return new AreaContainer(optional, question, description, 512);
        else if (type.equalsIgnoreCase("Field"))
            return new FieldContainer(optional, false, question, description);
        else if (type.equalsIgnoreCase("Slider"))
            return new SliderContainer(optional, question, description, lower, upper);
        else
            return null;
    }

    private int id;
    private boolean optional;
    private String type;
    private String question;
    private String description;
    private List<String> options;
    private Integer upper;
    private Integer lower;
}
