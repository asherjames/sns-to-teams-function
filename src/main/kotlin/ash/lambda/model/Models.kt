package ash.lambda.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZonedDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class SnsAlarmMessage(val alarmName: String,
                           val alarmDescription: String,
                           val awsAccountId: String,
                           val newStateValue: String,
                           val newStateReason: String,
                           val stateChangeTime: ZonedDateTime,
                           val region: String,
                           val oldStateValue: String,
                           val trigger: Trigger)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Trigger(val metricName: String,
                   val namespace: String,
                   val statistic: String,
                   val unit: String?,
                   val dimensions: List<Dimension>,
                   val period: Int,
                   val evaluationPeriods: Int,
                   val comparisonOperator: String,
                   val threshold: Double)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Dimension(val name: String,
                     val value: String)