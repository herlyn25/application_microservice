package bootcamp.reto.powerup.model;

public class ConstantsApps {


    public static final String STATE_REQUERIED_FIELD = "Id state is required";
    public static final String ID_APPS_REQUERIED_FIELD = "Id credit request is required";

    public static String PATTERN_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
       public static String PATTERN_DOCUMENT_ID = "^[0-9]{6,10}$";

    // Fields no exists
    public static String TYPE_LOAN_NO_EXIST = "Loan type no exists";
    public static String USER_NO_EXIST = "User no exists";
    public static String APPLICATIONS_NO_EXIST = "Credit request no exists";
    public static final String NOT_FOUND_STATE = "No Found resource states";

    // Invalid fields
    public static String INVALID_EMAIL = "Invalid Format Email";
    public static String INVALID_DOCUMENT_ID = "Invalid Document Id only numbers (longitude 7-10 characters)";
    public static String INVALID_STATES = "You can not change state, The state alreay is: ";
    public static String ERROR_APROB_STATES = "The status of the application is: ";

    // Empty Fields
    public static String REQUIRED_EMAIL = "Email is required";
    public static String REQUIRED_TERMS = "Terms is required";
    public static String REQUIRED_DOCUMENT = "Document ID is required";
    public static String REQUIRED_AMOUNT = "Amount is required";
    public static String REQUIRED_TYPE_LOAN = "Type Loan is required";

    //JWT

    public static final String STATUS_401 = "Unauthorized";
    public static final String TOKEN_INVALID = "Invalid Token";
    public static final String TOKEN_REQUIRED = "Token is required";
    public static final String TOKEN_EXPIRED = "Token expired";

    // SQS
    public static final String STATE_DIFFERENT_APROB_RECH = "Status other than approved or rejected";
    public static final String ERROR_TO_UPDATE = "Error updating credit request: ";
}