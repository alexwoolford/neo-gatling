package io.woolford

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SnowplowRecommenderSimulation extends Simulation{

  val csvFeeder = csv("domain_userids.csv").eager.random

  val httpProtocol = http
    .baseUrl("http://snowplow.woolford.io:8081") // Here is the root for all relative URLs
    .acceptHeader(
      "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
    ) // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader(
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
    )

  val scn =
    scenario("SnowplowRecommendation").feed(csvFeeder).repeat(100) {
      exec(
        http("getRecommendation")
          .get("/recommendations/${domain_userid}")
          .check(status.is(200))
      )
    }

  setUp(scn.inject(atOnceUsers(50)).protocols(httpProtocol))

}
