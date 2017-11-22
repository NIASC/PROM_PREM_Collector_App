package se.nordicehealth.ppc_app.implementation;

import android.app.Activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.nordicehealth.ppc_app.R;

import se.nordicehealth.ppc_app.core.UserHandle;
import se.nordicehealth.ppc_app.core.containers.ViewDataContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.interfaces.FormUtils;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.FormUtils.RetFunContainer;
import se.nordicehealth.ppc_app.implementation.containerdisplay.ContainerDisplays;
import se.nordicehealth.ppc_app.implementation.io.PacketHandler;
import se.nordicehealth.ppc_app.implementation.res.Resource;
import se.nordicehealth.ppc_app.implementation.security.RSA;

public class PPCGUI extends Activity implements UserInterface
{
    private UserHandle uh;
    private ScrollView pageContent;
    private TextView messagePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Resource.loadResources(getResources());
        PacketHandler.initialize(new RSA(Resource.key().exp(),
                Resource.key().mod()));

        uh = new UserHandle(this);
        initGUI();

        /*
        if (!ResourceStrings.loadMessages(getResources())
                || !Questions.getQuestions().loadQuestionnaire()) {
            displayError(Database.DATABASE_ERROR, false);
        } else {
            setContent(new LoginScreen(this));
        }
        */
        setContent(new LoginScreen(this));
    }

    private void initGUI()
    {
        final PPCGUI c = this;
        setContentView(R.layout.activity_login);

        pageContent = findViewById(R.id.page_scroll);
        messagePanel = findViewById(R.id.message_area);

        Button mainmenuButton = findViewById(R.id.button1);
        mainmenuButton.setText(R.string.main_menu_btn);
        mainmenuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uh.isLoggedIn())
                    setContent(new WelcomeScreen(c));
                else
                    setContent(new LoginScreen(c));
            }
        });

        Button logoutButton = findViewById(R.id.button2);
        logoutButton.setText(R.string.logout_btn);
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uh.logout();
                setContent(new LoginScreen(c));
            }
        });

        Button exitButton = findViewById(R.id.button3);
        exitButton.setText(R.string.exit_btn);
        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uh.logout();
                c.finish();
                Process.killProcess(Process.myPid());
            }
        });
    }

    public void setContent(View panel)
    {
        if (panel == null) {
            return;
        }
        displayMessage("", false); // reset any error messages
        pageContent.removeAllViews();
        pageContent.addView(panel);
        panel.requestFocus();
        pageContent.invalidate();
    }

    @Override
    public void displayError(String message, boolean popup) {
        displayEmbeddedMessage(message, 0xffff0000);
    }

    @Override
    public void displayMessage(String message, boolean popup) {
        displayEmbeddedMessage(message, 0xff000000);
    }

    @Override
    public boolean presentForm(List<FormContainer> form, FormUtils retfun, boolean multiple) {
        setContent(new GUIForm(this, form, retfun, getContent(), multiple));
        return true;
    }

    @Override
    public boolean presentViewData(ViewDataContainer vdc) {
        setContent(new ViewDataDisplay(this, vdc));
        return true;
    }

    @Override
    public FormComponentDisplay getContainerDisplay(FormContainer fc) {
        return ContainerDisplays.getDisplay(this, fc);
    }


    private void displayEmbeddedMessage(String message, int textColor)
    {
        messagePanel.setTextColor(textColor);
        messagePanel.setText(message);
    }

    public View getContent()
    {
        return pageContent.getChildAt(0);
    }

    private class LoginScreen extends LinearLayout
    {
        Button login, register;
        EditText usernameTF;
        EditText passwordTF;

        /**
         * Creates the login screen.
         */
        LoginScreen(Context c)
        {
            super(c);
            setOrientation(LinearLayout.VERTICAL);


			/* entry fields */

            LinearLayout userPanel = new LinearLayout(c);
            userPanel.setLayoutParams(new FrameLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            userPanel.setOrientation(LinearLayout.VERTICAL);

            usernameTF = new EditText(c);
            usernameTF.setHint(String.format("%s",
                    Resource.messages().getInfo(Messages.INFO_UH_ENTER_USERNAME)));
            userPanel.addView(usernameTF);

            passwordTF = new EditText(c);
            passwordTF.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordTF.setHint(String.format("%s",
                    Resource.messages().getInfo(Messages.INFO_UH_ENTER_PASSWORD)));
            userPanel.addView(passwordTF);

			/* button panel */
            LinearLayout buttons = new LinearLayout(c);
            buttons.setOrientation(LinearLayout.HORIZONTAL);

            final Context _c = c;

            login = new Button(c);
            login.setText(Resource.messages().getInfo(Messages.INFO_LOGIN));
            login.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uh.login(usernameTF.getText().toString(),
                            passwordTF.getText().toString());
                    if (uh.isLoggedIn()) {
                        usernameTF.setText(null);
                        passwordTF.setText(null);
                        setContent(new WelcomeScreen(_c));
                        uh.updatePassword();
                    }
                }
            });

            register = new Button(c);
            register.setText(Resource.messages().getInfo(Messages.INFO_REGISTER));
            register.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uh.register();
                    }
                });

            buttons.addView(login);
            buttons.addView(register);

			/* add components */
            addView(userPanel);
            addView(buttons);
        }
    }

    private class WelcomeScreen extends LinearLayout
    {

        Button questionnaire, viewData;

        WelcomeScreen(Context c)
        {
            super(c);
            setOrientation(LinearLayout.VERTICAL);


            questionnaire = new Button(c);
            questionnaire.setText(Resource.messages().getInfo(Messages.INFO_START_QUESTIONNAIRE));
            questionnaire.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uh.startQuestionnaire();
                }
            });

            viewData = new Button(c);
            viewData.setText(Resource.messages().getInfo(Messages.INFO_VIEW_STATISTICS));
            viewData.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uh.viewData();
                }
            });

            addView(questionnaire);
            addView(viewData);
        }
    }

    private class GUIForm extends LinearLayout
    {
        final int nEntries;
        boolean displayMultiple;
        int cIdx;
        Messages msg = Resource.messages();

        LinearLayout formControl;
        Button fc_continue, fc_previous, fc_next, fc_back;

        final List<FormComponentDisplay> components;
        final List<FormContainer> form;
        final FormUtils function;
        final View retpan;

        GUIForm(Context c, final List<FormContainer> form, final FormUtils function,
                final View retpan, boolean displayMultiple)
        {
            super(c);
            displayMessage("Setting PPCGUI form as content", false);

            this.form = form;
            this.function = function;
            this.retpan = retpan;
            this.displayMultiple = displayMultiple;

            setOrientation(LinearLayout.VERTICAL);

            components = fillContents(form);
            nEntries = components.size();

            formControl = initControlPanel(c);

            setFormContent();
        }

        List<FormComponentDisplay> fillContents(List<FormContainer> form)
        {
            List<FormComponentDisplay> contents = new ArrayList<>();
            for (FormContainer fc : form)
                contents.add(fc.getDisplayable(PPCGUI.this));
            return contents;
        }

        int getNextEntry(int cIndex, int nEntries, int steps,
                         boolean wrap)
        {
            if (wrap)
                return (cIndex + steps + nEntries) % nEntries;

            if (cIndex + steps >= nEntries)
                return cIndex;
            else if (cIndex + steps < 0)
                return 0;
            else
                return cIndex + steps;
        }

        int getNextUnfilledEntry(int currentIdx,
                                 List<FormComponentDisplay> form)
        {
            int entries = form.size();
            int i = getNextEntry(currentIdx, entries, 1, true);
            while (form.get(i).entryFilled()
                    && i != currentIdx)
                i = getNextEntry(i, entries, 1, true);
            return i;
        }

        void updateButtons()
        {
            fc_next.setEnabled(cIdx != getNextEntry(cIdx, nEntries, 1, false));
            fc_previous.setEnabled(cIdx != getNextEntry(cIdx, nEntries, -1, false));
            if (getNextUnfilledEntry(cIdx, components) == cIdx)
                fc_continue.setText(msg.getInfo(
                        Messages.INFO_UI_FORM_FINISH));
            else
                fc_continue.setText(msg.getInfo(
                        Messages.INFO_UI_FORM_CONTINUE));
        }

        void setFormContent()
        {
            removeAllViews();

            if (displayMultiple) {
                for (int i = 0; i < components.size(); ++i) {
                    FormComponentDisplay fcd = components.get(i);
                    addView((View) fcd);
                }
            } else {
                addView((View) components.get(cIdx));
            }
            addView(formControl);

            invalidate();
        }

        LinearLayout initControlPanel(Context c)
        {
            LinearLayout panel = new LinearLayout(c);

            fc_continue = new Button(c);
            fc_continue.setText(msg.getInfo(Messages.INFO_UI_FORM_CONTINUE));
            fc_continue.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    components.get(cIdx).fillEntry();
                    int nextComponent = getNextUnfilledEntry(cIdx, components);
                    if (nextComponent == cIdx && components.get(cIdx).entryFilled()) {
                        RetFunContainer rfc = function.ValidateUserInput(form);
                        if (rfc.valid) {
                            displayMessage("", false);
                            setContent(retpan);
                            function.callNext();
                        } else if (rfc.message != null)
                            displayError(rfc.message, false);
                    } else {
                        cIdx = nextComponent;
                        setFormContent();
                    }

                    updateButtons();
                }
            });

            fc_previous = new Button(c);
            fc_previous.setText(msg.getInfo(Messages.INFO_UI_FORM_PREVIOUS));
            fc_previous.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    components.get(cIdx).fillEntry();
                    cIdx = getNextEntry(cIdx, nEntries, -1, false);
                    setFormContent();

                    updateButtons();
                }
            });
            if (cIdx == getNextEntry(cIdx, nEntries, -1, false))
                fc_previous.setEnabled(false);

            fc_next = new Button(c);
            fc_next.setText(msg.getInfo(Messages.INFO_UI_FORM_NEXT));
            fc_next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    components.get(cIdx).fillEntry();
                    cIdx = getNextEntry(cIdx, nEntries, 1, false);
                    setFormContent();

                    updateButtons();
                }
            });

            fc_back = new Button(c);
            fc_back.setText(msg.getInfo(Messages.INFO_UI_FORM_BACK));
            fc_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContent(retpan);

                    updateButtons();
                }
            });

            panel.addView(fc_continue);
            if (!displayMultiple) {
                panel.addView(fc_previous);
                panel.addView(fc_next);
            }
            panel.addView(fc_back);
            return panel;
        }
    }

    public class ViewDataDisplay extends LinearLayout
    {
        public ViewDataDisplay(Context c, ViewDataContainer vdc)
        {
            super(c);
            setOrientation(LinearLayout.VERTICAL);

            title = new TextView(c);
            title.setLayoutParams(new FrameLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            title.setSingleLine(false);
            title.setMaxLines(35);
            title.setText(vdc.getTitle());

            results = new TextView(c);
            results.setLayoutParams(new FrameLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            results.setSingleLine(false);
            results.setMaxLines(35);
            results.setText(vdc.getResults());

            addView(title);
            addView(results);
        }

        private TextView title, results;
    }
}

