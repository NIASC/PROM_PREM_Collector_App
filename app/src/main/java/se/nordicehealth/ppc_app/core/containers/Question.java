/*! QuestionContainer.java
 * 
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package se.nordicehealth.ppc_app.core.containers;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.Questionnaire;
import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;

/**
 * This class contains questionnaire questions.
 *
 * @author Marcus Malmquist
 *
 */
public class Question
{

    /**
     * Retrieves the ID of the question.
     *
     * @return The ID of the question.
     */
    int getID() { return id; }

    /**
     * Retrieves this question's class.
     *
     * @return This question's class.
     */
    Class<?> getContainerClass() { return type; }

    /**
     * Retrieves the optional flag for the question.
     *
     * @return {@code true} if the question is optional.
     */
    public boolean isOptional() { return optional; }

    /**
     * Retrieves the statement of the question.
     *
     * @return The statement of the question.
     */
    public String getStatement() { return question; }

    /**
     * Retrieves the description of the question.
     *
     * @return The description of the question.
     */
    public String getDescription() { return description; }

    /**
     * Retrieves the option text associated with {@code id}
     * of the question.
     *
     * @param id the ID of the option.
     *
     * @return The option text associated with {@code id}.
     */
    String getOption(int id)
    {
        String optn = null;
        try {
            optn = options.get(id);
        } catch (IndexOutOfBoundsException ignored) {

        }
        return optn;
    }
		
    /**
     * Retrieves the options for this question.
     *
     * @return A list of the available options in this container.
     */
    List<String> getOptions()
    {
        return Collections.unmodifiableList(options);
    }
		
    /**
     * Retrieves the upper/max value that can be entered as an answer.
     *
     * @return The upper/max value for this question
     */
    int getUpper() { return upper; }
		
    /**
     * Retrieves the lower/min value that can be entered as an answer.
     *
     * @return The lower/min value for this question
     */
    int getLower() { return lower; }
		
    private int id;
		
    /**
     * Whether or not it is not required to answer this question.
     */
    private boolean optional;
		
    /**
     * The type of question (SliderContainer, SingleOptionContainer,
     * FieldContainer etc.)
     */
    private Class<? extends FormContainer> type;
		
    /**
     * The question/statement of this question.
     */
    private String question;
		
    /**
     * A more detailed description of the {@link #question}.
     */
    private String description;
		
    /**
     * The available options for this question.
     */
    private List<String> options;
		
    /**
     * The upper limit for this entry.
     */
    private Integer upper;
		
    /**
     * The lower limit for this entry.
     */
    private Integer lower;
		
    /**
     * Adds a {@code Questionnaire} entry to this container.
     *
     * @param id The ID of the question (as it appears in the
     * 		database).
     * @param type The type of question as it appears in the
     * 		database. Valid types are currently: SingleOption,
     * 		Slider, Field
     * @param question The question/statement that the user should
     * 		respond to.
     * @param description A more detailed description of the
     * 		{@code question}.
     * @param options The available options to select in response
     * 		to the question/statement. If this entry type does not
     * 		have options this variable will be discarded can be
     * 		set to {@code null}.
     * @param optional If it is <i><b>not</b></i> required to
     * 		answer this question this should be set to
     * 		{@code true} else {@code false}.
     * @param upper The upper limit for this entry. If this user
     * 		is supposed to enter a numerical value this should be
     * 		the upper limit for that value.
     * @param lower The lower limit for this entry. If this user
     * 		is supposed to enter a numerical value this should be
     * 		the lower limit for that value.
     */
    public Question(int id, Class<? extends FormContainer> type,
                     String question, String description,
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
		
    /**
     * Places this question in an appropriate container based on
     * the type of question this is.
     *
     * @return The container for this question, or {@code null} if no
     * 		container could be found for this question.
     *
     * @see FormContainer
     */
    FormContainer getContainer()
    {
        if (type == null)
            return null;
			
        if (SingleOptionContainer.class.isAssignableFrom(type)) {
            SingleOptionContainer soc = new SingleOptionContainer(
                    optional, question, description);
            int i = 0;
            for (Iterator<String> itr = options.iterator(); itr.hasNext(); ++i)
                soc.addOption(i, itr.next());
            return soc;
        } else if (MultipleOptionContainer.class.isAssignableFrom(type)) {
            MultipleOptionContainer moc = new MultipleOptionContainer(
                    optional, question, description);
            int i = 0;
            for (Iterator<String> itr = options.iterator(); itr.hasNext(); ++i)
                moc.addOption(i, itr.next());
            return moc;
        } else if (AreaContainer.class.isAssignableFrom(type)) {
            if (FieldContainer.class.isAssignableFrom(type))
                return new FieldContainer(
                        optional, false, question, description);
            return new AreaContainer(optional, question, description, 512);
        } else if (SliderContainer.class.isAssignableFrom(type))
            return new SliderContainer(
                    optional, question, description, lower, upper);
        else if (TimePeriodContainer.class.isAssignableFrom(type))
            return null;
        else
            return null;
    }
}
