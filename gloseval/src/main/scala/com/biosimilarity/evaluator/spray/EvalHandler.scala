// -*- mode: Scala;-*- 
// Filename:    EvalHandler.scala 
// Authors:     lgm                                                    
// Creation:    Wed May 15 13:53:55 2013 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.evaluator.spray

import com.protegra_ati.agentservices.store._

import com.biosimilarity.evaluator.distribution._
import com.biosimilarity.evaluator.msgs._
import com.biosimilarity.lift.model.store._
import com.biosimilarity.lift.lib._

import akka.actor._
import spray.routing._
import directives.CompletionMagnet
import spray.http._
import spray.http.StatusCodes._
import MediaTypes._

import spray.httpx.encoding._

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.continuations._ 
import scala.collection.mutable.HashMap

import com.typesafe.config._

import javax.crypto._
import javax.crypto.spec.SecretKeySpec

import java.util.Date
import java.util.UUID

trait EvalHandler {
  self : EvaluationCommsService =>
 
  import DSLCommLink.mTT

  @transient
  implicit val formats = DefaultFormats

  def initializeSessionRequest(json: JValue, onPost : Option[mTT.Resource] => Unit ) = {
    val agentURI = (json \ "content" \ "agentURI").extract[String]
    val uri = new java.net.URI(agentURI)

    if (uri.getScheme() != "agent") {
      throw InitializeSessionException(agentURI, "Unrecognized scheme")
    }
    // TODO: get a proper library to do this
    val userNameAndPwd = uri.getUserInfo.split(":")
    val userName = userNameAndPwd(0)
    val userPwd = userNameAndPwd.length match {
      case 1 => ""
      case _ => userNameAndPwd(0)
    }
    val queryMap = new HashMap[String, String]
    uri.getRawQuery.split("&").map((x: String) => {
      val pair = x.split("=")
      queryMap += ((pair(0), pair(1)))
    })

    val sessionID = UUID.randomUUID
    val erql = agentMgr.erql( sessionID )
    val erspl = agentMgr.erspl( sessionID ) 

    val (
      uri1str : String,
      userCnxn : ConcreteHL.PortableAgentCnxn,
      recoveryCnxn : ConcreteHL.PortableAgentCnxn,
      userData : CnxnCtxtLabel[String,String,String],
      userKeySpec : SecretKeySpec,
      sysKeySpec : SecretKeySpec,
      encrypt : Cipher
    ) = agentMgr.secureCnxn(userName, userPwd, queryMap)

    // create agent
    agentMgr.post[CnxnCtxtLabel[String,String,String]]( erql, erspl )(
       userData, List( userCnxn ), userData
    )
    
    // Store connection
    encrypt.init(Cipher.ENCRYPT_MODE, userKeySpec)
    val uri1Bytes : Array[Byte] = uri1str.getBytes("utf-8")
    val userUri1Enc : String = encrypt.doFinal(uri1Bytes).map("%02x" format _).mkString
    agentMgr.post[String]( erql, erspl )( 
      userData, List( recoveryCnxn ), userUri1Enc
    )
    encrypt.init(Cipher.ENCRYPT_MODE, sysKeySpec)
    val sysUri1Enc : String = encrypt.doFinal(uri1Bytes).map("%02x" format _).mkString
    agentMgr.post[String]( erql, erspl )(
      userData, List( recoveryCnxn ), sysUri1Enc, onPost
    )
  }

  def evalSubscribeRequest(json: JValue) : AgentSessionURI = {
    val diesel.EvaluatorMessageSet.evalSubscribeRequest( sessionURI, expression ) =
      ( json \ "content" ).extract[diesel.EvaluatorMessageSet.evalSubscribeRequest]
    // val sessionURI = (json \ "content" \ "sessionURI").extract[String]
    // val expression = (json \ "content" \ "expression").extract[String]

    // if (sessionURI != "agent-session://ArtVandelay@session1") {
    //   throw EvalException(sessionURI)
    // }

    val sessionID = UUID.randomUUID
    val erql = agentMgr.erql( sessionID )
    val erspl = agentMgr.erspl( sessionID ) 

    expression match {
      case ConcreteHL.FeedExpr( filter, cnxns ) => {                    
        agentMgr.feed( erql, erspl )( filter, cnxns )
      }
      case ConcreteHL.ScoreExpr( filter, cnxns, staff ) => {
        agentMgr.score( erql, erspl )( filter, cnxns, staff )
      }
      case ConcreteHL.InsertContent( filter, cnxns, content : String ) => {
        agentMgr.post[String]( erql, erspl )( filter, cnxns, content )
      }
    }

    sessionURI
  }

  def sessionPing(json: JValue) : String = {
    val sessionURI = (json \ "content" \ "sessionURI").extract[String]
    // TODO: check sessionURI validity
    
    sessionURI
  }



  def closeSessionRequest(json: JValue) : (String, spray.http.HttpBody) = {
    val sessionURI = (json \ "content" \ "sessionURI").extract[String]
    if (sessionURI != "agent-session://ArtVandelay@session1") {
      throw CloseSessionException(sessionURI, "Unknown session.")
    }

    (sessionURI, HttpBody(`application/json`,
      """{
        "msgType": "closeSessionResponse",
        "content": {
          "sessionURI": "agent-session://ArtVandelay@session1",
        }
      }
      """
    ))
  }

}
