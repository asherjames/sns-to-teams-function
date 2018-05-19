package ash.lambda

import ash.lambda.model.SnsAlarmMessage
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.apache.commons.text.StringEscapeUtils

class SnsHandler : RequestHandler<SNSEvent, String>
{
  private val objectMapper = ObjectMapper()
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .registerModule(JavaTimeModule())
      .registerKotlinModule()

  private val teamsUrl = System.getenv("TEAMS_URL") ?: throw SnsHandlerException("TEAMS_URL must be populated")

  override fun handleRequest(input: SNSEvent?, context: Context?): String
  {
    if (input == null)
    {
      throw SnsHandlerException("Input is null!")
    }

    val alarmMessages = input.records
        .map { snsRecord -> snsRecord.sns.message }
        .map { StringEscapeUtils.escapeJson(it) }
        .map { objectMapper.readValue<SnsAlarmMessage>(it) }

    if (alarmMessages.isEmpty())
    {
      throw SnsHandlerException("No alarm messages!")
    }

    for (message in alarmMessages)
    {
      teamsUrl.httpPost()
          .body(
              """
                {
                  "text": "Raised ${message.alarmName} at ${message.stateChangeTime}"
                }
              """.trimIndent()
          )
          .responseString { _, _, result ->
        when(result)
        {
          is Result.Failure -> {
            throw SnsHandlerException(result.getException())
          }
          is Result.Success -> {
            context?.logger?.log("Teams request successful")
          }
        }
      }
    }

    return "Request successfully processed"
  }
}

class SnsHandlerException : Throwable
{
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
}