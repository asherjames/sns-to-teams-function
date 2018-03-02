package ash.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.text.StringEscapeUtils
import org.joda.time.DateTime

class SnsHandler : RequestHandler<SNSEvent, Response>
{
  private val objectMapper = ObjectMapper()
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .registerModule(JodaModule())
      .registerKotlinModule()

  override fun handleRequest(input: SNSEvent?, context: Context?): Response
  {
    if (input == null)
    {
      return Response("Input is null")
    }

    val alarmMessages = input.records
        .map { snsRecord -> snsRecord.sns.message }
        .map { StringEscapeUtils.escapeJson(it) }
        .map { objectMapper.readValue<SnsAlarmMessage>(it) }



    return Response("Request successfully processed")
  }
}

data class Response(val message: String = "")

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class SnsAlarmMessage(val alarmName: String,
                           val alarmDescription: String,
                           val awsAccountId: String,
                           val newStateValue: String,
                           val newStateReason: String,
                           val stateChangeTime: DateTime,
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