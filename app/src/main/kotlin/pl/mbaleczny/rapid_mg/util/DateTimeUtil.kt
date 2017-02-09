package pl.mbaleczny.rapid_mg.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Created by mariusz on 06.02.17.
 */

private val input_date_time_formatter_pattern = "EEE MMM dd HH:mm:ss Z yyyy"
private val output_date_time_formatter_pattern = "dd/MM/yyyy HH:mm:ss"

private val formatter: DateTimeFormatter = DateTimeFormat.forPattern(input_date_time_formatter_pattern)

fun transformTweetDateTime(input: String): String {
    val dateTime = DateTime.parse(input, formatter)
    return DateTimeFormat.forPattern(output_date_time_formatter_pattern).print(dateTime)
}