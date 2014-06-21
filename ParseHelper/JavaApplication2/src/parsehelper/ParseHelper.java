package parsehelper;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

/**
 *
 * @author ian
 */
//         Parse.initialize(this, "pAwMknVW1OcjKXYK7Lpj5Jibigfzio8kb2CdBScO", "YRL8bqthghkC5TsIPdybWGTIzuwab2i808JiSeUZ");
public class ParseHelper {

    public static final int STATUS_UNASSIGNED = 0;
    public static final int STATUS_ASSIGNED = 1;
    public static final int STATUS_COMPLETE = 2;

    private ParseHelper() {
    }

    public static void login(String username, String password) throws ParseException {
        ParseUser.logIn(username, password);
    }

    public static ParseUser getUser(String username) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        return query.getFirst();
    }

    public static List<ParseUser> getUsers() throws ParseException {
        return ParseUser.getQuery().find();
    }

    public static ParseObject createEvent(String name) {
        ParseObject obj = new ParseObject("Event");
        obj.put("name", name);
        return obj;
    }

    public static ParseObject createTask(String name, int priority) {
        ParseObject obj = new ParseObject("Task");
        obj.put("name", name);
        obj.put("priority", priority);
        obj.put("status", STATUS_UNASSIGNED);
        return obj;
    }

    public static List<ParseObject> sortedTasks(ParseObject event) throws ParseException {
        ParseQuery<ParseObject> query = event.getRelation("tasks").getQuery();
        query.addAscendingOrder("name");
        query.addAscendingOrder("status");
        query.addAscendingOrder("priority");
        return query.find();
    }
}
