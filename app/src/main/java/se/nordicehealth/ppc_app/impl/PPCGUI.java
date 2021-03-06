package se.nordicehealth.ppc_app.impl;

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
import se.nordicehealth.ppc_app.core.interfaces.FormControl;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.FormControl.ValidationStatus;
import se.nordicehealth.ppc_app.impl.containerdisplay.ContainerDisplays;
import se.nordicehealth.ppc_app.impl.io.PacketHandler;
import se.nordicehealth.ppc_app.impl.res.Resource;
import se.nordicehealth.ppc_app.impl.security.RSA;

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
        Key key = Resource.key();
        PacketHandler.initialize(new RSA(key.exp(), key.mod()));

        uh = new UserHandle(this);
        initGUI();
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
        displayMessage("", false);
        pageContent.removeAllViews();
        pageContent.addView(panel);
        panel.requestFocus();
        pageContent.invalidate();
    }

    @Override
    public void displayError(String message, boolean popup)
    {
        displayEmbeddedMessage(message, 0xffff0000);
    }

    @Override
    public void displayMessage(String message, boolean popup)
    {
        displayEmbeddedMessage(message, 0xff000000);
    }

    @Override
    public boolean presentForm(List<FormContainer> form, FormControl requester, boolean displayMultiple)
    {
        setContent(new GUIForm(this, form, requester, getContent(), displayMultiple));
        return true;
    }

    @Override
    public boolean presentViewData(ViewDataContainer vdc)
    {
        setContent(new ViewDataDisplay(this, vdc));
        return true;
    }

    @Override
    public FormComponentDisplay getContainerDisplay(FormContainer fc)
    {
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
        Messages msg = Resource.messages();

        LoginScreen(Context c)
        {
            super(c);
            setOrientation(LinearLayout.VERTICAL);
            addView(entryFields(c));
            addView(buttonPanel(c));
        }

        LinearLayout entryFields(final Context c)
        {
            LinearLayout userPanel = new LinearLayout(c);
            userPanel.setLayoutParams(new FrameLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            userPanel.setOrientation(LinearLayout.VERTICAL);

            usernameTF = username(c);
            passwordTF = password(c);
            userPanel.addView(usernameTF);
            userPanel.addView(passwordTF);

            return userPanel;
        }

        LinearLayout buttonPanel(final Context c)
        {
            LinearLayout buttons = new LinearLayout(c);
            buttons.setOrientation(LinearLayout.HORIZONTAL);

            login = loginButton(c);
            register = registerButton(c);
            buttons.addView(login);
            buttons.addView(register);

            return buttons;
        }

        Button loginButton(final Context c)
        {
            Button login = new Button(c);
            login.setText(msg.info(Messages.INFO.LOGIN));
            login.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uh.login(usernameTF.getText().toString(), passwordTF.getText().toString());
                    if (uh.isLoggedIn()) {
                        usernameTF.setText(null);
                        passwordTF.setText(null);
                        setContent(new WelcomeScreen(c));
                        uh.updatePassword();
                    }
                }
            });
            return login;
        }

        Button registerButton(final Context c)
        {
            Button register = new Button(c);
            register.setText(msg.info(Messages.INFO.REGISTER));
            register.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uh.registration();
                }
            });
            return register;
        }

        EditText username(final Context c)
        {
            EditText usernameTF = new EditText(c);
            usernameTF.setHint(String.format("%s", msg.info(Messages.INFO.UH_ENTER_USERNAME)));
            return usernameTF;
        }

        EditText password(final Context c)
        {
            EditText passwordTF = new EditText(c);
            passwordTF.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordTF.setHint(String.format("%s", msg.info(Messages.INFO.UH_ENTER_PASSWORD)));
            return passwordTF;
        }
    }

    private class WelcomeScreen extends LinearLayout
    {
        Button questionnaire, viewData;
        Messages msg = Resource.messages();

        WelcomeScreen(Context c)
        {
            super(c);
            setOrientation(LinearLayout.VERTICAL);

            questionnaire = questionnaireBtn(c);
            viewData = viewDataBtn(c);
            addView(questionnaire);
            addView(viewData);
        }

        Button questionnaireBtn(final Context c)
        {
            Button questionnaire = new Button(c);
            questionnaire.setText(msg.info(Messages.INFO.START_QUESTIONNAIRE));
            questionnaire.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uh.startQuestionnaire();
                }
            });
            return questionnaire;
        }

        Button viewDataBtn(final Context c)
        {
            Button viewData = new Button(c);
            viewData.setText(msg.info(Messages.INFO.VIEW_STATISTICS));
            viewData.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uh.viewData();
                }
            });
            return viewData;
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
        final FormControl function;
        final View retpan;

        GUIForm(Context c, final List<FormContainer> form, final FormControl function,
                final View retpan, boolean displayMultiple)
        {
            super(c);

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

        int getNextEntry(int cIndex, int nEntries, int steps, boolean wrap) {
            if (wrap)
                return (cIndex + steps + nEntries) % nEntries;
            if (cIndex + steps >= nEntries)
                return cIndex;
            else if (cIndex + steps < 0)
                return 0;
            else
                return cIndex + steps;
        }

        int getNextUnfilledEntry(int currentIdx, List<FormComponentDisplay> form)
        {
            int entries = form.size();
            int i = getNextEntry(currentIdx, entries, 1, true);
            while (form.get(i).entryIsFilled() && i != currentIdx) {
                i = getNextEntry(i, entries, 1, true);
            }
            return i;
        }

        void updateButtons()
        {
            fc_next.setEnabled(cIdx != getNextEntry(cIdx, nEntries, 1, false));
            fc_previous.setEnabled(cIdx != getNextEntry(cIdx, nEntries, -1, false));
            if (getNextUnfilledEntry(cIdx, components) == cIdx)
                fc_continue.setText(msg.info(Messages.INFO.UI_FORM_FINISH));
            else
                fc_continue.setText(msg.info(Messages.INFO.UI_FORM_CONTINUE));
        }

        void setFormContent()
        {
            removeAllViews();

            if (displayMultiple)
                for (FormComponentDisplay fcd : components)
                    addView((View) fcd);
            else
                addView((View) components.get(cIdx));
            addView(formControl);

            invalidate();
        }

        LinearLayout initControlPanel(Context c)
        {
            LinearLayout panel = new LinearLayout(c);

            fc_continue = continueBtn(c);
            fc_previous = previousBtn(c);
            fc_next = nextBtn(c);
            fc_back = backBtn(c);

            panel.addView(fc_continue);
            if (!displayMultiple) {
                panel.addView(fc_previous);
                panel.addView(fc_next);
            }
            panel.addView(fc_back);
            return panel;
        }

        Button continueBtn(final Context c)
        {
            Button fc_continue = new Button(c);
            fc_continue.setText(msg.info(Messages.INFO.UI_FORM_CONTINUE));
            fc_continue.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    components.get(cIdx).fillEntry();
                    int nextComponent = getNextUnfilledEntry(cIdx, components);
                    if (nextComponent == cIdx && components.get(cIdx).entryIsFilled()) {
                        ValidationStatus rfc = function.validateUserInput(form);
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
            return fc_continue;
        }

        Button previousBtn(final Context c)
        {
            Button fc_previous = new Button(c);
            fc_previous.setText(msg.info(Messages.INFO.UI_FORM_PREVIOUS));
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
            return fc_previous;
        }

        Button nextBtn(final Context c)
        {
            Button fc_next = new Button(c);
            fc_next.setText(msg.info(Messages.INFO.UI_FORM_NEXT));
            fc_next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    components.get(cIdx).fillEntry();
                    cIdx = getNextEntry(cIdx, nEntries, 1, false);
                    setFormContent();

                    updateButtons();
                }
            });
            return fc_next;
        }

        Button backBtn(final Context c)
        {
            Button fc_back = new Button(c);
            fc_back.setText(msg.info(Messages.INFO.UI_FORM_BACK));
            fc_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContent(retpan);
                    updateButtons();
                }
            });
            return fc_back;
        }
    }

    private class ViewDataDisplay extends LinearLayout
    {
        ViewDataDisplay(Context c, ViewDataContainer vdc)
        {
            super(c);
            setOrientation(LinearLayout.VERTICAL);

            TextView title = titleArea(c);
            TextView results = resultsArea(c);

            title.setText(vdc.title());
            results.setText(vdc.representation());

            addView(title);
            addView(results);
        }

        TextView titleArea(final Context c)
        {
            TextView title = new TextView(c);
            title.setLayoutParams(new FrameLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            title.setSingleLine(false);
            title.setMaxLines(35);
            return title;
        }

        TextView resultsArea(final Context c)
        {
            TextView results = new TextView(c);
            results.setLayoutParams(new FrameLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            results.setSingleLine(false);
            results.setMaxLines(4096);
            return results;
        }
    }
}

