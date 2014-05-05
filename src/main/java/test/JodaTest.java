package test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.UnknownUserException;

public class JodaTest {

	private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	 
	
	public static void main(String[] args) throws Exception {
		UserManager um = new UserManager();
		
		User user = um.getUser("wanyaok");
		user.setPassword("okneuespasswort");
		user = um.updateUser(user);
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		df.setTimeZone(tz);
		String nowAsISO = df.format(user.getLastModificationTime());
		System.out.println(nowAsISO);
		
		System.out.println(user.getLastModificationTime().toString());
	}

}
