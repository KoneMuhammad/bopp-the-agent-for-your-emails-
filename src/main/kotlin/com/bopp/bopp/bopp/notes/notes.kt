package com.bopp.bopp.bopp.notes

import com.bopp.bopp.bopp.spamemail.UserEmail
import org.springframework.web.bind.annotation.RequestBody
import javax.swing.plaf.nimbus.State


fun saveObjectInRam(): String{
    val initialObject = "looong Sstring"

    return initialObject
}

//getting the most recent emails ie the calls
// and i need a stopper
//this repeation thing si the most difficult right now
//why becuase with repreation there are edge scenarios how does the front end x how does x

//within a single repeation i have to handle multiple edge scenarios

//later ill decide to make the timer longer
/**
 *
 *
 */


val storedGlobally: State<String> = "thisValue"



fun collectiveLogic(){

}

/**
 *  get emails from ui
 *  turn them into spam
 * the part thats recursive is the first sending to the frontend this handles what it handles 1
 * llm result gets turnedintotoolcall .toCode
 */
fun AgentTransformsSpam(@RequestBody emails: List<UserEmail>){

    val areTheySpam = spamHelper(call(emails))
    webclient.google(toolcall(areTheySpam))
}


fun spamHelper(llmResult: String){
    return when (llmResult) {
        "id 1 - 7 spam" -> id8,id7,id5,id4,id2
        "id 2 - 6 spam" -> id1,id5,id9,id5,id1
    }
    else -> "error"

}



fun call(emails: List<UserEmail>) : String {
    val prompt = "you are an llm you take this list of emails and determine weather or not their spam" +
            "through their title subject and body" +
            "here are the list of emails $emails"
    val decision = llm.call(prompt)

    return decision
}