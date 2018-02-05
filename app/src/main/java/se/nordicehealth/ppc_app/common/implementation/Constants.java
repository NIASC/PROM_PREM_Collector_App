/*! Constants.java
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
package se.nordicehealth.ppc_app.common.implementation;

import android.util.Log;

import java.io.IOException;
import java.net.URL;

/**
 * This interface contains constant values that are used in both the
 * applet and servlet when communicating between them.
 * 
 * @author Marcus Malmquist
 *
 */
public abstract class Constants {

	public static final URL SERVER_URL;
	
	static
    {
		URL url = null;
		try {
            //url = new URL("http://188.114.242.3:443/PROM_PREM_Collector/main");
			url = new URL("http://192.168.1.22:8080/PROM_PREM_Collector/main");
		}
		catch (IOException | IllegalArgumentException ignore) {
			Log.e("ECONN", ignore.getMessage());
		}
		SERVER_URL = url;
	}

    public enum Packet {
        __NULL__,
        TYPE,
        DATA;

        public enum Types {
            __NULL__,
            PING,
            VALIDATE_PID,
            ADD_QANS,
            SET_PASSWORD,
            LOAD_Q,
            LOAD_QR_DATE,
            LOAD_QR,
            REQ_REGISTR,
            REQ_LOGIN,
            REQ_LOGOUT;
        }

        public enum Data {
            __NULL__;

            public enum Ping {
                __NULL__,
                RESPONSE,
                DETAILS;

                public enum Details {
                    __NULL__,
                    UID
                }

                public enum Response {
                    __NULL__,
                    FAIL,
                    SUCCESS
                }
            } // Ping

            public enum ValidatePatientID {
                __NULL__,
                RESPONSE,
                PATIENT,
                DETAIL;

                public enum Details {
                    __NULL__,
                    UID
                }

                public enum Patient {
                    __NULL__,
                    PERSONAL_ID
                }

                public enum Response {
                    __NULL__,
                    FAIL,
                    SUCCESS
                }
            } // ValidatePatientID

            public enum AddQuestionnaireAnswers {
                __NULL__,
                RESPONSE,
                DETAILS,
                PATIENT,
                QUESTIONS;

                public enum Details {
                    __NULL__,
                    UID
                }

                public enum Patient {
                    __NULL__,
                    FORENAME,
                    SURNAME,
                    PERSONAL_ID
                }

                public enum Response {
                    __NULL__,
                    FAIL,
                    SUCCESS
                }
            } // AddQuestionnaireAnswers

            public enum SetPassword {
                __NULL__,
                RESPONSE,
                DETAILS;

                public enum Details {
                    __NULL__,
                    UID,
                    OLD_PASSWORD,
                    NEW_PASSWORD1,
                    NEW_PASSWORD2
                }

                public enum Response {
                    __NULL__,
                    ERROR,
                    SUCCESS,
                    INVALID_DETAILS,
                    MISMATCH_NEW,
                    PASSWORD_SHORT,
                    PASSWORD_SIMPLE
                }
            } // SetPassword

            public enum LoadQuestions {
                __NULL__,
                QUESTIONS;

                public enum Question {
                    __NULL__,
                    OPTIONS,
                    TYPE,
                    ID,
                    QUESTION,
                    DESCRIPTION,
                    OPTIONAL,
                    MAX_VAL,
                    MIN_VAL;

                    public enum Optional {
                        __NULL__,
                        YES,
                        NO
                    }
                }
            } // LoadQuestions

            public enum LoadQResultDates {
                __NULL__,
                DATES,
                DETAILS;

                public enum Details {
                    __NULL__,
                    UID
                }
            } // LoadQResultDates

            public enum LoadQResults {
                __NULL__,
                RESULTS,
                QUESTIONS,
                DETAILS,
                BEGIN,
                END;

                public enum Details {
                    __NULL__,
                    UID
                }
            } // LoadQResults

            public enum RequestRegistration {
                __NULL__,
                RESPONSE,
                DETAILS;

                public enum Details {
                    __NULL__,
                    NAME,
                    EMAIL,
                    CLINIC
                }

                public enum Response {
                    __NULL__,
                    FAIL,
                    SUCCESS
                }
            } // RequestRegistration

            public enum RequestLogin {
                __NULL__,
                RESPONSE,
                UPDATE_PASSWORD,
                UID,
                DETAILS;

                public enum Response {
                    __NULL__,
                    ERROR,
                    FAIL,
                    SUCCESS,
                    SERVER_FULL,
                    ALREADY_ONLINE,
                    INVALID_DETAILS,
                    UPDATE_PASSWORD
                }

                public enum Details {
                    __NULL__,
                    USERNAME,
                    PASSWORD
                }

                public enum UpdatePassword {
                    __NULL__,
                    YES,
                    NO
                }
            } // RequestLogin

            public enum RequestLogout {
                __NULL__,
                RESPONSE,
                DETAILS;

                public enum Response {
                    __NULL__,
                    ERROR,
                    SUCCESS
                }

                public enum Details {
                    __NULL__,
                    UID
                }
            } // RequestLogout
        } // Packets
    }

    public enum QuestionTypes {
        __NULL__,
        SINGLE_OPTION,
        MULTIPLE_OPTION,
        SLIDER,
        AREA
    }

    public static <T extends Enum<T>> T getEnum(T v[], int ordinal) {
        for (T t : v) {
            if (t.ordinal() == ordinal) {
                return t;
            }
        }
        return v[0];
    }

    public static <T extends Enum<T>> T getEnum(T v[], String ordinalStr) throws NumberFormatException {
        return getEnum(v, Integer.parseInt(ordinalStr));
    }

    public static <T extends Enum<T>> boolean equal(T lhs, T rhs) {
        return lhs.compareTo(rhs) == 0;
    }
}
