package truelecter.chat.taiga;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class ChatDateFormat extends DateFormat {
    public static final String IN = " в ";
    public static final String YESTARDAY = "Вчера";
    public static final String[] DAYS = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};
    public static final String[] MONTH = new String[]{"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль",
            "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", "Asssssssssssssssembler"};
    private static final long serialVersionUID = -1743943027558241269L;

    @SuppressWarnings("deprecation")
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        StringBuffer sb = new StringBuffer();
        Date now = new Date();
        if (date.getDay() == (now.getDay() - 1)) sb.append(YESTARDAY + IN);
        else if ((date.getDay() - now.getDay()) < 0) {
            sb.append(DAYS[date.getDay()]).append(IN);
        } else if (date.getDate() != now.getDate()) {
            sb.append(MONTH[date.getMonth()]).append(" ").append(date.getDate()).append(", ").append(1900 + date.getYear());
            return sb;
        }
        sb.append(date.getHours()).append(":").append(date.getMinutes());
        /*
         * if (Math.abs(date.getHours() - now.getHours()) != 0) { sb.append("");
         * } else { //sb.append(date.getMinutes() + " " +
         * getMinuteEnding(date.getMinutes()) + " назад."); sb.append(date."");
         * }
         */
        return sb;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return new Date();
    }
/*
    public static String getMinuteEnding(int i) {
        // *1 минута
        // *2-4 минуты
        // other минут
        i = i % 100;
        if ((i % 10) == 1)
            return "минута";
        if (((i % 10) > 1) && ((i % 10) < 5))
            return "минуты";
        return "минут";
    }
*/
}
