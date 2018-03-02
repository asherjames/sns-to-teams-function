package ash.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import org.joda.time.DateTime

class SnsHandler : RequestHandler<SNSEvent, Response>
{
  override fun handleRequest(input: SNSEvent?, context: Context?): Response
  {
    if (input == null)
    {
      return Response("Input is null")
    }

    val alarmMessages = input.records
        .map { snsRecord -> snsRecord.sns }
        .map { sns -> sns.message }



    return Response("Request successfully processed")
  }
}

data class Response(val message: String = "")

data class SnsAlarmMessage(val alarmName: String,
                           val alarmDescription: String,
                           val awsAccountId: String,
                           val newStateValue: String,
                           val newStateReason: String,
                           val stateChangeTime: DateTime,
                           val region: String,
                           val oldStateValue: String,
                           val metricName: String,
                           val namespace: String,
                           val statistic: String,
                           val unit: String,
                           val dimensions: Pair<String, String>,
                           val period: Int,
                           val evaluationPeriods: Int,
                           val comparisonOperator: String,
                           val threshold: Double)