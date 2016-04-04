import java.io._
import java.util.zip._

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

object WorkFile {

  def readFileFormatGz(fileName: String): Map[String, ListBuffer[String]] = {
    val in: InputStream = new GZIPInputStream(new FileInputStream(fileName))
    val reader = scala.io.Source.fromInputStream(in).getLines()
    val header = reader.next.split(",").map(_.trim)

    var year = new ListBuffer[String]()
    var quarter = new ListBuffer[String]()
    var month = new ListBuffer[String]()
    var dayOfMonth = new ListBuffer[String]()
    var dayOfWeek = new ListBuffer[String]()
    var flDay = new ListBuffer[String]()
    var origin = new ListBuffer[String]()
    var dest = new ListBuffer[String]()

    while (reader.hasNext) {
      var row = reader.next
      var column = row.split(",")
      year += (column(0))
      quarter += (column(1))
      month += (column(2))
      dayOfMonth += (column(3))
      dayOfWeek += (column(4))
      flDay += (column(5))
      origin += (column(6))
      dest += (column(7))
    }
    //"YEAR","QUARTER","MONTH","DAY_OF_MONTH","DAY_OF_WEEK","FL_DATE","ORIGIN","DEST"
    val res = Map("YEAR" -> year, "QUARTER" -> quarter, "MONTH" -> month,
      "DAY_OF_MONTH" -> dayOfMonth, "DAY_OF_WEEK" -> dayOfWeek, "FL_DATE" -> flDay, "ORIGIN" -> origin, "DEST" -> dest)
    return res
  }

  def countForLastColumnTable(temp: Map[String, ListBuffer[String]]): Map[String, Int] = {
    val destList: ListBuffer[String] = temp("DEST")
    val dast: List[String] = destList.toList
    countRepeatedValues(dast)
  }

  def countDiffBetweenColumns(temp: Map[String, ListBuffer[String]]): Map[String, Int] = {
    val destList: ListBuffer[String] = temp("DEST")
    val originList: ListBuffer[String] = temp("ORIGIN")
    val resDest = countRepeatedValues(destList.toList)
    val resOrigin = countRepeatedValues(originList.toList)
    countTheDifference(resDest, resOrigin)

  }

  def countTheDifference(first: Map[String, Int], second: Map[String, Int]): Map[String, Int] = {
    val keySet = first.keys
    var sum: Int = 0
    var resmap: Map[String, Int] = Map[String, Int]()
    for (k <- keySet) {
      sum = first(k) - second(k)
      if (sum != 0) {
        resmap += (k -> sum)
      }
    }
    resmap
  }

  def countRepeatedValues(letters: List[String]): Map[String, Int] = {
    var map = Map[String, Int]()
    for (letter <- letters) map += Pair(letter, map.getOrElse(letter, 0) + 1)
    map
  }

  def findMaxValueAndMaxKeyCountLastColumn(temp: Map[String, Int]): Map[String, Int] = {
    val keySet = temp.keys
    var max: Int = 0
    var resMap = Map[String, Int]()
    for (k <- keySet) {
      if (temp(k) == temp.valuesIterator.max) {
        // println(k+","+temp(k))
        resMap += (k -> temp(k))
      }
    }
    resMap
  }

//  def countValueForDays(temp: Map[String, ListBuffer[String]]): Map[String, Int] = {
//
//    val w1 = new ListBuffer[String]()
//    val w2 = new ListBuffer[String]()
//    val w3 = new ListBuffer[String]()
//    val w4 = new ListBuffer[String]()
//    val w5 = new ListBuffer[String]()
//
//    for (t <- temp("DAY_OF_MONTH")) {
//      if (t.toInt <= 7) {w1->countRepeatedValues(temp("DEST").toList)}
//      if (t.toInt >=8 && t.toInt <=14) {w2->countRepeatedValues(temp("DEST").toList)}
//      if (t.toInt >=15 && t.toInt <=21) {w3->countRepeatedValues(temp("DEST").toList)}
//      if (t.toInt >=22 && t.toInt <=28) {w3->countRepeatedValues(temp("DEST").toList)}
//      if (t.toInt >=29 && t.toInt <=31) {w3->countRepeatedValues(temp("DEST").toList)}
//    }
//    val mapTask = Map["W1"->w1,"W2"->w2,"W3"->w3,"W4"->w4,"W5"->w5]
//    println(mapTask)
//    mapTask
//  }

  def writeResultIntoFile(temp: Map[String, Int], fileName: String) {
    val out = new FileWriter("./src/main/resources/result/" + fileName + ".csv")
    val stringAry: Array[String] = (temp.map(_.toString)).toArray
    for (st <- stringAry) {
      out.write((st.replace("(", "")).replace(")", "") + "\n")
    }
    println("*** write into file, done " +
      "file path:" + "./src/main/resources/result/" + fileName + ".csv")
    out.close()
  }

}

