package se.nordicehealth.ppc_app.core.interfaces;

public interface Messages
{
	String  ERROR_UH_PR_PASSWORD_SIMPLE     = "UH_PR_PASSWORD_SIMPLE"   ,
			ERROR_UH_PR_INVALID_LENGTH      = "UH_PR_INVALID_LENGTH"    ,
			ERROR_UH_PR_MISMATCH_NEW        = "UH_PR_MISMATCH_NEW"      ,
			ERROR_UH_PR_INVALID_CURRENT     = "UH_PR_INVALID_CURRENT"   ,
			ERROR_UH_INVALID_LOGIN          = "UH_INVALID_LOGIN"        ,
			ERROR_NOT_LOGGED_IN             = "NOT_LOGGED_IN"           ,
			ERROR_OPERATION_NOT_PERMITTED   = "OPERATION_NOT_PERMITTED" ,
			ERROR_NULL_SELECTED             = "NULL_SELECTED"           ,
			ERROR_UNKNOWN_RESPONSE          = "UNKNOWN_RESPONSE"        ,
			ERROR_UH_ALREADY_ONLINE         = "UH_ALREADY_ONLINE"       ,
			ERROR_UH_SERVER_FULL            = "UH_SERVER_FULL"          ,
			ERROR_REG_REQUEST_FAILED        = "REG_REQUEST_FAILED"      ,
			ERROR_QP_INVALID_PID            = "QP_INVALID_PID"          ,
			ERROR_VD_INVALID_PERIOD         = "VD_INVALID_PERIOD"       ,
			ERROR_VD_FEW_ENTRIES            = "VD_FEW_ENTRIES"          ;

	enum INFO
	{
		SELECT_OPTION, EXIT, LOGIN, REGISTER, NEW_PASS_INFO, CURRENT_PASSWORD,
		NEW_PASSWORD, RE_NEW_PASSWORD, START_QUESTIONNAIRE, VIEW_STATISTICS, LOGOUT,

		UH_ENTER_USERNAME, UH_ENTER_PASSWORD, UH_UPDATE_PASSWORD,

		REG_CLINIC_NAME, REG_USER_NAME, REG_USER_EMAIL, REG_REQUEST_SENT,

		UI_UNFILLED, UI_FILLED, UI_FORM_OPTIONAL, UI_FORM_BACK, UI_FORM_NEXT, UI_FORM_PREVIOUS,
		UI_FORM_CONTINUE, UI_FORM_FINISH,

		VD_SELECT_PERIOD, VD_SELECT_QUESTIONS, VD_DATE_FROM, VD_DATE_TO,

		Q_PATIENT_FORENAME, Q_PATIENT_SURNAME, Q_PATIENT_PNR
	}
	
	String  INFO_SELECT_OPTION              = "SELECT_OPTION"           ,
			INFO_EXIT                       = "EXIT"                    ,
			INFO_LOGIN                      = "LOGIN"                   ,
			INFO_REGISTER                   = "REGISTER"                ,
			INFO_NEW_PASS_INFO              = "NEW_PASS_INFO"           ,
			INFO_CURRENT_PASSWORD           = "CURRENT_PASSWORD"        ,
			INFO_NEW_PASSWORD               = "NEW_PASSWORD"            ,
			INFO_RE_NEW_PASSWORD            = "RE_NEW_PASSWORD"         ,
			INFO_START_QUESTIONNAIRE        = "START_QUESTIONNAIRE"     ,
			INFO_VIEW_STATISTICS            = "VIEW_STATISTICS"         ,
			INFO_LOGOUT                     = "LOGOUT"                  ,
			INFO_UH_ENTER_USERNAME          = "UH_ENTER_USERNAME"       ,
			INFO_UH_ENTER_PASSWORD          = "UH_ENTER_PASSWORD"       ,
			INFO_UH_UPDATE_PASSWORD         = "UH_UPDATE_PASSWORD"      ,
			INFO_REG_CLINIC_NAME            = "REG_CLINIC_NAME"         ,
			INFO_REG_USER_NAME              = "REG_USER_NAME"           ,
			INFO_REG_USER_EMAIL             = "REG_USER_EMAIL"          ,
			INFO_REG_REQUEST_SENT           = "REG_REQUEST_SENT"        ,
			INFO_UI_UNFILLED                = "UI_UNFILLED"             ,
			INFO_UI_FILLED                  = "UI_FILLED"               ,
			INFO_UI_FORM_OPTIONAL           = "UI_FORM_OPTIONAL"        ,
			INFO_UI_FORM_BACK               = "UI_FORM_BACK"            ,
			INFO_UI_FORM_NEXT               = "UI_FORM_NEXT"            ,
			INFO_UI_FORM_PREVIOUS           = "UI_FORM_PREVIOUS"        ,
			INFO_UI_FORM_CONTINUE           = "UI_FORM_CONTINUE"        ,
			INFO_UI_FORM_FINISH             = "UI_FORM_FINISH"          ,
			INFO_VD_SELECT_PERIOD           = "VD_SELECT_PREIOD"        ,
			INFO_VD_SELECT_QUESTIONS        = "VD_SELECT_QUESTIONS"     ,
			INFO_VD_DATE_FROM               = "VD_DATE_FROM"            ,
			INFO_VD_DATE_TO                 = "VD_DATE_TO"              ,
			INFO_Q_PATIENT_FORENAME         = "Q_PATIENT_FORENAME"      ,
			INFO_Q_PATIENT_SURNAME          = "Q_PATIENT_SURNAME"       ,
			INFO_Q_PATIENT_PNR              = "Q_PATIENT_PNR"           ;

	String error(String errorName);

	String info(String infoName);
}
