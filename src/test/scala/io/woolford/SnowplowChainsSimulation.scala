package io.woolford

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef.{http, status}
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase.driver
import org.neo4j.gatling.bolt.Predef._
import org.neo4j.gatling.bolt.builder.SessionHelper._

class SnowplowChainsSimulation extends Simulation{

  val csvFeeder = csv("domain_userids.csv").eager.random

  val boltProtocol = bolt(driver("bolt://neo4j.woolford.io:7687", AuthTokens.basic("neoadmin", "V1ctoria")))



  val scn = scenario("simpleCreate").feed(csvFeeder).repeat(1) {
    val cypherQueryProps = {}
    exec(
      cypher("CALL snowplow.append.journey.chain('123', 'http://woolford.io', apoc.date.fromISO8601('2020-11-04T12:21:33.000Z')) YIELD chainLength RETURN chainLength", cypherQueryProps, "snowplowtest")
    ).pause(2)
  }

  setUp(scn.inject(atOnceUsers(5))).protocols(boltProtocol)

}
