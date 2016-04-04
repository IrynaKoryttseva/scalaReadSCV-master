
object RunnerApp {

  def main(filenames: Array[String]): Unit = {
    //read file
    val fileName = "./src/main/resources/file/planes_log.csv.gz"
    val resultMap=WorkFile.readFileFormatGz((fileName))
    print("Task1: ")
    val result=WorkFile.countForLastColumnTable(resultMap)
    WorkFile.writeResultIntoFile(result,"Task_1_countLastColumns")
    val result2=WorkFile.countDiffBetweenColumns(resultMap)
    print("Task2: ")
    WorkFile.writeResultIntoFile(result2,"Task_2_countTheDifference")
//    print("Task3: ") -not done
//    val result3=WorkFile.countValueForDays(resultMap)
//    WorkFile.writeResultIntoFile(result3,"Task_3_countTheDifference")
    print("Task4: ")
    WorkFile.countForLastColumnTable(resultMap)
    val result4 = WorkFile.findMaxValueAndMaxKeyCountLastColumn(result)
    WorkFile.writeResultIntoFile(result4,"Task_4_maxValueAndMaxKeyCountDest")
  }
}