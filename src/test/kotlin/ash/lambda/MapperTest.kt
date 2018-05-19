package ash.lambda

import ash.lambda.model.SnsAlarmMessage
import com.fasterxml.jackson.databind.MapperFeature
import org.assertj.core.api.Assertions.*

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Test

class MapperTest
{
  private val objectMapper = ObjectMapper()
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .registerModule(JavaTimeModule())
      .registerKotlinModule()

  @Test
  fun testAlarmMessageIsMappedCorrectly()
  {
    val result: SnsAlarmMessage = objectMapper
        .readValue("""
          {
            "AlarmName": "anAlarm",
            "AlarmDescription": "Created from EC2 Console",
            "AWSAccountId": "11111111",
            "NewStateValue": "ALARM",
            "NewStateReason": "Threshold Crossed: 1 datapoint (3.0) was not less than the threshold (3.0).",
            "StateChangeTime": "2018-05-18T11:24:15+00:00",
            "Region": "A Region",
            "OldStateValue": "OK",
            "Trigger": {
              "MetricName": "HealthyHostCount",
              "Namespace": "AWS/ELB",
              "Statistic": "AVERAGE",
              "Unit": null,
              "Dimensions": [
                {
                  "name": "LoadBalancerName",
                  "value": "LB"
                }
              ],
              "Period": 60,
              "EvaluationPeriods": 5,
              "ComparisonOperator": "LessThanThreshold",
              "Threshold": 3.0
            }
          }
        """.trimIndent())

    assertThat(result).isNotNull
    assertThat(result.alarmName).isEqualTo("anAlarm")
    assertThat(result.stateChangeTime.dayOfMonth).isEqualTo(18)
    assertThat(result.stateChangeTime.monthValue).isEqualTo(5)
    assertThat(result.stateChangeTime.hour).isEqualTo(11)
    assertThat(result.stateChangeTime.minute).isEqualTo(24)
    assertThat(result.stateChangeTime.zone.id).isEqualTo("UTC")
    assertThat(result.trigger.metricName).isEqualTo("HealthyHostCount")
  }
}