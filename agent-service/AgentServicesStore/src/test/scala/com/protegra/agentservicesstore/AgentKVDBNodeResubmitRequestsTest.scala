// -*- mode: Scala;-*- 
// Filename:    ResubmissionTest.scala 
// Authors:     lgm                                                    
// Creation:    Tue Oct 30 15:51:29 2012 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.protegra.agentservicesstore

import org.specs._
import org.specs.util._
import org.specs.runner.JUnit4
import org.specs.runner.ConsoleRunner

import com.biosimilarity.lift.model.store._
import com.protegra.agentservicesstore.extensions.StringExtensions._
import com.protegra.agentservicesstore.extensions.ResourceExtensions._
import com.protegra.agentservicesstore.extensions.URIExtensions._

import scala.util.continuations._
import scala.concurrent.{Channel => Chan, _}
import scala.concurrent.cpsops._

import java.net.URI
import java.util.UUID

import com.protegra.agentservicesstore.usage.AgentKVDBScope._
import com.protegra.agentservicesstore.usage.AgentKVDBScope.acT._
import com.protegra.agentservicesstore.usage.AgentKVDBScope.mTT._
import com.protegra.agentservicesstore.usage.AgentUseCase._

import Being.AgentKVDBNodeFactory

class AgentKVDBNodeResubmitRequestsTest
  extends JUnit4(AgentKVDBNodeResubmitRequestsTestSpecs)

object AgentKVDBNodeResubmitRequestsTestSpecsRunner
  extends ConsoleRunner(AgentKVDBNodeResubmitRequestsTestSpecs)

object AgentKVDBNodeResubmitRequestsTestSpecs extends Specification
{  

  "AgentKVDBNode" should {
    
    @transient
    val testConfig =
      AgentKVDBNodeResubmitRequestsTestDefaultConfiguration()
    
    /* --------------------------------------------------------- *
     *                       Scenarios
     * --------------------------------------------------------- */

    "retrieve between UI and Store with a public queue" in {

      /* --------------------------------------------------------- *
       *                       KVDBs
       * --------------------------------------------------------- */

      // val msgConfigFileName = Some("db_store.conf")
      // val store_msgQ = createNode(public_location, List(), msgConfigFileName)

      // spawn {
      //   reset {ui_privateQ.put(cnxnUIStore)(keyMsg.toLabel, Ground(value))}
      // }

      /* --------------------------------------------------------- *
       *                   Store continuations
       * --------------------------------------------------------- */      
      testConfig.registerContinuations(
	testConfig.keyBidsNAsks._2.take( testConfig.numberOfStandingRequests )
      )( testConfig.ckrReportPresent, testConfig.ckrReportAbsent )

      /* --------------------------------------------------------- *
       * Wait at barrier -- will be fixed with a blocking get, soon
       * --------------------------------------------------------- */

      while ( testConfig.barrier() < testConfig.numberOfStandingRequests ) {
	println( "waiting to get past registration barrier; current height: " + testConfig.barrier )
	Thread.sleep( testConfig.TIMEOUT_MED )
      }

      /* --------------------------------------------------------- *
       *                   Resubmit requests
       * --------------------------------------------------------- */      

      testConfig.resubmitRequests(
	testConfig.keyBidsNAsks._2.take( testConfig.numberOfStandingRequests )
      )

      testConfig.justPullKRecords match {
	case false => {
	  println( "\n########################################################\n" )
	  println( " Actually resubmitting requests " )
	  println( "\n########################################################\n" )

	  while ( testConfig.barrier() < testConfig.numberOfStandingRequests ) {
	    println( "waiting to get past resubmission barrier; current height: " + testConfig.barrier )
	    Thread.sleep( testConfig.TIMEOUT_MED )
	  }

	  println( "\n########################################################\n" )
	  
	  for( ( k, v ) <- testConfig.keyMap ) {
	    println( 	  
	      "registered key : " + k + "\n"
	      + "number of times registered : " + v + "\n"
	    )
	  }
	  
	  println( "\n########################################################\n" )
	  
	  testConfig.probeStandingRequests(
	    testConfig.keyBidsNAsks._1.take( testConfig.numberOfStandingRequests )
	  )
	}
	case true => {
	  println( "\n########################################################\n" )
	  println( " Just pulled krecords " )
	  println( "\n########################################################\n" )
	}
      }      

      /* --------------------------------------------------------- *
       *                   Assert invariant
       * --------------------------------------------------------- */      
      
      //fetchString(_resultsQ, cnxnTest, resultKey.toLabel) must be_==(value).eventually(5, TIMEOUT_EVENTUALLY)
    }

  }

}
