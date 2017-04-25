package co.stutzen.shopzen.constants;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

public class AppConstants {

    public static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "xk2xgnxxtjmwx93g",
            "xyddc58pgdm9bw9t",
            "e0ce5db4ba45b3025e53847dd502f04e"
    );

    public static String domain = "https://safetymatch.in/wp-json/wc/v1";

    public static String cust_keysecret = "consumer_key=ck_0670699479cf38bc9903808143ffebf8e4e8ceac&consumer_secret=cs_d3e67af0dd772414a97c0962bd8e5e3146c057f8";

    public static String ALGOLIA_APP_ID = "E7I7UH21H4";

    public static String ALGOLIA_APP_KEY = "8778ebddedfe4059393cd8ac27625a6d";

    public static String fromemail = "";//enter you mail to send inbuild email

    public static String receipient = ""; // if multiple receipient you can add email by comma(,) seperated

    public static String password = "";//enter your email password to send inbuild email

    public static String CATEGORY_LIST_API = domain+"/products/categories?"+cust_keysecret;

    public static String PRODUCT_LIST_API = domain+"/products?"+cust_keysecret;

    public static String CREATE_CUSTOMER = domain+"/customers?"+cust_keysecret;

    public static String CREATE_ORDER = domain+"/orders?"+cust_keysecret;

    public static String LOGIN_USER = "https://safetymatch.in/wp-json/jwt-auth/v1/token";

    public static String ORDER_RETIREVE_API = domain+"/orders/";

    public static String PRODUCT_RETIREVE_API = domain+"/products/";

    public static int discountInPercentage = 0;

    public static int giftWrapAmount = 0;
}
