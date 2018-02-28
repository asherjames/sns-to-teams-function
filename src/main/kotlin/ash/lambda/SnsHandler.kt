package ash.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class SnsHandler : RequestHandler<SnsEvent, Response>
{
  override fun handleRequest(input: SnsEvent?, context: Context?): Response
  {
    return Response()
  }
}

data class SnsEvent(val records: List<SnsRecord>)

data class SnsRecord(val EventVersion: String,
                     val eventSubscriptionArn: String,
                     val eventSource: String)

data class Response(val message: String = "")