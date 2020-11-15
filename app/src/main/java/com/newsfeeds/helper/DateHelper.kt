package com.newsfeeds.helper

import android.app.DatePickerDialog
import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import java.util.Locale.getDefault

object DateHelper {

    val MOBILE_DATE_FORMAT_DETAIL = "dd MMMM yyyy"
    val MOBILE_DATE_FORMAT_LIST = "dd MMM yyyy"
    val MOBILE_DATE_FORMAT_LIST_RIGHT = "dd/MM/yy"
    val SERVICE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    val SERVICE_HOUR_FORMAT = "HH:mm:ss"
    val MOBILE_DATE_FORMAT_TAKING_ORDER = "dd/MM/yyyy"
    val MOBILE_DATE_FORMAT_INVOICE = "dd-MM-yyyy"
    val MOBILE_DATE_MONTH_YEAR = "MM/yyyy"

    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS

    fun day(): Int {
        return getInstance().get(DAY_OF_MONTH)
    }

    fun hour(): Int {
        return getInstance().get(HOUR_OF_DAY)
    }

    fun minute(): Int {
        return getInstance().get(MINUTE)
    }

    fun dateNumber(): Int {
        return getInstance().get(DATE)
    }

    fun day(calendar: Calendar): Int {
        return calendar.get(DAY_OF_MONTH)
    }

    fun day(date: Date): Int {
        val c = getInstance()
        c.time = date
        return day(c)
    }

    fun month(): Int {
        return getInstance().get(MONTH)
    }

    fun month(calendar: Calendar): Int {
        return calendar.get(MONTH)
    }

    fun month(date: Date): String {
        val calendar = getInstance()
        calendar.time = date
        val m = month(calendar) + 1
        return m.toString()
    }

    fun year(date: Date): String {
        val calendar = getInstance()
        calendar.time = date
        val y = year(calendar)
        return y.toString()
    }

    fun year(): Int {
        return getInstance().get(YEAR)
    }

    fun year(calendar: Calendar): Int {
        return calendar.get(YEAR)
    }

    internal fun timestamp(): Long {
        return System.currentTimeMillis() / 1000
    }

    fun date(): Date {
        val cal = getInstance()
        cal.timeInMillis = timestamp()
        return cal.time
    }

    fun addDayAfter(date: Date, amount: Int): Date {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, amount)
        return c.time
    }

    fun dateToCalendar(date: Date): Calendar {
        val calendar = getInstance()
        calendar.time = date
        return calendar
    }

    /**
     * @see BaseApplication,
     * Locale by default is Locale("in")
     */
    fun dateFormat(format: String): SimpleDateFormat {
        return SimpleDateFormat(format, getDefault())
    }

    fun dateStr(from: Date): String {
        val formatter = dateFormat(MOBILE_DATE_FORMAT_DETAIL)
        val result = formatter.format(from)
        return result
    }

    fun toDay(from: Date, customFormat: String): String {
        val formatter = dateFormat(customFormat)
        val result = formatter.format(from)
        return result
    }

    fun dateStr(from: Date?, customFormat: String): String {
        val formatter = dateFormat(customFormat)
        val result = formatter.format(from)
        return result
    }

    fun dateObj(from: String): Date {
        val formatter = dateFormat(SERVICE_DATE_FORMAT)
        val result = formatter.parse(from)
        return result
    }

    fun dateCalendar(from: String): Date {
        val formatter = dateFormat("yyyy-MM-dd")
        val result = formatter.parse(from)
        return result
    }

    fun dateObj(from: String, customFormat: String): Date {
        val formatter = dateFormat(customFormat)
        val result = formatter.parse(from)
        return result
    }

    fun changeFormat(stringDate: String, prevFormat: String, toFormat: String): String {
        val df_prevFormat = dateFormat(prevFormat)
        val df_toFormat = dateFormat(toFormat)
        val date: Date
        try {
            date = df_prevFormat.parse(stringDate)
        } catch (e: ParseException) {
            return ""
        }

        return df_toFormat.format(date)
    }

    fun getFirstDateOfTheMonth(date: Date): String {
        val c = Calendar.getInstance()
        c.set(year(date).toInt(), month(date).toInt() - 1, day(date))
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH))

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(c.time)
    }

    fun getLastDateOfTheMonth(date: Date): String {
        val c = Calendar.getInstance()
        c.set(year(date).toInt(), month(date).toInt() - 1, day(date))
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH))

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(c.time)
    }

    fun datePicker(
        context: Context,
        listener: DatePickerDialogListener,
        defaultDate: Date,
        requestCode: Int,
        requestedFormat: String
    ) {
        val cal_defaultDate = getInstance()
        cal_defaultDate.time = defaultDate
        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
            listener.onDateSet(
                requestCode, dateFormat(requestedFormat)
                    .format(
                        GregorianCalendar(
                            datePicker.year,
                            datePicker.month,
                            datePicker.dayOfMonth
                        ).time
                    )
            )
        }
            , year(cal_defaultDate), month(cal_defaultDate), day(cal_defaultDate)).show()
    }

    fun today(): Date {
        return Calendar.getInstance().time
    }

    fun todayStr(format: String): String {
        var dateStr = dateStr(today(), format)
        return dateStr
    }

    interface DatePickerDialogListener {
        fun onDateSet(requestCode: Int, date: String)
    }

    fun getTotalDayBetween(date1: Date, date2: Date): Int {
        val c1 = getInstance()
        c1.time = date2

        val c2 = getInstance()
        c2.time = date1

        return c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR)
    }

    fun dateObj(from: Long, customFormat: String): String {
        val formatter = dateFormat(customFormat)
        val result = formatter.format(from)
        return result
    }

    fun getTimeAgo(currentTime: Long): String? {
        var time = currentTime
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return "recently"
        }

        // TODO: localize
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "recently"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "1m ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            "${diff / MINUTE_MILLIS}m ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "1h ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            "${diff / HOUR_MILLIS}h ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "1d ago"
        } else {
            dateObj(time, "MMM. dd, yyyy")
        }
    }

    fun dateFormatTomorrow(): String {
        var CALENDAR = Calendar.getInstance()
        CALENDAR.add(DAY_OF_YEAR, 1)
        val sdf = SimpleDateFormat(MOBILE_DATE_FORMAT_TAKING_ORDER, Locale.US)
        return sdf.format(CALENDAR.time)
    }

    fun dateFormatToday(): String {
        var CALENDAR = Calendar.getInstance()
        val sdf = SimpleDateFormat(MOBILE_DATE_FORMAT_TAKING_ORDER, Locale.US)
        return sdf.format(CALENDAR.time)
    }

    fun dateFormatTodayInvoiceDate(): String {
        var CALENDAR = Calendar.getInstance()
        val sdf = SimpleDateFormat(MOBILE_DATE_FORMAT_INVOICE, Locale.US)
        return sdf.format(CALENDAR.time)
    }

    fun dateFormatTodayInvoiceTime(): String {
        var CALENDAR = Calendar.getInstance()
        val sdf = SimpleDateFormat(SERVICE_HOUR_FORMAT, Locale.US)
        return sdf.format(CALENDAR.time)
    }

    fun dateFormatTodayMonthYear(): String {
        var CALENDAR = Calendar.getInstance()
        val sdf = SimpleDateFormat(MOBILE_DATE_MONTH_YEAR, Locale.US)
        return sdf.format(CALENDAR.time)
    }

    fun dateFormatLastMonthYear(): String {
        var CALENDAR = Calendar.getInstance()
        CALENDAR.add(Calendar.MONTH, -1)
        val sdf = SimpleDateFormat(MOBILE_DATE_MONTH_YEAR, Locale.US)
        return sdf.format(CALENDAR.time)
    }
}