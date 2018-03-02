package ash.lambda

import com.fasterxml.jackson.databind.MapperFeature
import org.assertj.core.api.Assertions.*

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Test

class MapperTest
{
  private val objectMapper = ObjectMapper()
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .registerModule(JodaModule())
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
            "StateChangeTime": "2016-01-01T00:00:00.000+0000",
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

    print(result)

    assertThat(result).isNotNull
  }
}