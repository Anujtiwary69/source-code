package co.stutzen.shopzen.constants;

import java.util.ArrayList;

import co.stutzen.shopzen.bo.ColorBO;
import co.stutzen.shopzen.bo.NotificationBO;

public class CommonFunctions {

    public static ArrayList<ColorBO> getFilterMenu(){
        ArrayList<ColorBO> menu = new ArrayList<>();
        ColorBO bo = new ColorBO();
        bo.setCatcolor("Categories");
        bo.setSelected(true);
        menu.add(bo);

        bo = new ColorBO();
        bo.setCatcolor("Price");
       // menu.add(bo);
        return menu;
    }

    public static ArrayList<NotificationBO> getNotifications() {
        ArrayList<NotificationBO> noti = new ArrayList<NotificationBO>();
        NotificationBO bo = new NotificationBO();
        bo.setMessage("consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam");
        bo.setTime("6 hours ago");
        noti.add(bo);

        bo = new NotificationBO();
        bo.setMessage("nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat");
        bo.setTime("11 hours ago");
        noti.add(bo);

        bo = new NotificationBO();
        bo.setMessage("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla");
        bo.setTime("2 days ago");
        noti.add(bo);

        return noti;
    }
}
