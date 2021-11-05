package io.woolford

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase.driver
import org.neo4j.gatling.bolt.Predef._
import org.neo4j.gatling.bolt.builder.SessionHelper._

class Neo4jJdbcSimulation extends Simulation {

  val boltProtocol = bolt(driver("bolt://neo4j.woolford.io:7687", AuthTokens.basic("neoadmin", "V1ctoria")))
  val scn = scenario("simpleCreate")
    .exec(
      cypher("create (n:Dummy) return n", Map.empty, "neo4j")
    ).pause(1)
  setUp(scn.inject(atOnceUsers(100))).protocols(boltProtocol)

}