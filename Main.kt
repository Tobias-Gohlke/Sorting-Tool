import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val listOfTypes = listOf("-dataType", "-sortingType", "line", "word", "long", "natural", "byCount", "-inputFile", "-outputFile")
    val dataType: String = if (args.indexOf("-dataType") != -1) args[args.indexOf("-dataType") + 1] else "word"
    val sortingType: String = if (args.indexOf("-sortingType") != -1) {
        try {args[args.indexOf("-sortingType") + 1]} catch (e: Exception) { "natural" }
    } else "natural"
    val inputFile: String = if (args.indexOf("-inputFile") != -1) args[args.indexOf("-inputFile") + 1] else "Null"
    val outputFile: String = if (args.indexOf("-outputFile") != -1) args[args.indexOf("-outputFile") + 1] else "Null"

    for (elements in args) {
        if ((!listOfTypes.contains(elements)) && !elements.endsWith(".txt")) {
            println("\"$elements\" is not a valid parameter. It will be skipped.")
        }
    }
    readInput(dataType, sortingType, inputFile, outputFile)
}

fun readInput(dataType: String, sortingType: String, inputFile: String, outputFile: String) {
    val scanner = Scanner(System.`in`)
    val lines = if (dataType == "Null")File(inputFile).readLines() else mutableListOf()
    var mainList = mutableListOf<String>()
    val preList = mutableListOf<String>()
    // If InputFile is given --> read InputFile
    if (inputFile != "Null") {
        if (dataType == "line") {
            mainList = lines.toMutableList()
        } else {
            for (line in lines) {
                preList += line.split(" ")
            }
            for (i in preList) {
                mainList += i.filter { it != ' ' }
            }
        }
        // If InputFile is not given --> read User Input
    } else {
        if (dataType == "line") {
            while (scanner.hasNextLine()) {
                mainList += scanner.nextLine()
            }
        } else {
            while (scanner.hasNext()) {
                mainList += scanner.next()
            }
        }
    }
    sortAnyType(mainList, sortingType, dataType, outputFile, inputFile)
}

fun sortAnyType(list: MutableList<String>, sortingType: String, dataType: String, outputF: String, inputFile: String) {
    val outputFile: File = try { File(outputF) } catch (e: Exception) { File(inputFile) }
    val listUnsorted = cancelStringLine(dataType, list)
    val listSorted = when(dataType) {
        "long" -> listUnsorted.sortedBy { it.toLong() }.toMutableList()
        "word" -> listUnsorted.sortedBy { it }.toMutableList()
        "line" -> listUnsorted.sortedBy { it }.toMutableList()
        else -> { listUnsorted }
    }
    when (dataType) {
        "long" -> {
            if (outputF != "Null") {
                outputFile.writeText("Total numbers: ${listSorted.lastIndex + 1}.")
            } else println("Total numbers: ${listSorted.lastIndex + 1}.")
        }
        "word" -> {
            if (outputF != "Null") {
                outputFile.writeText("Total words: ${listSorted.lastIndex + 1}.")
            } else println("Total words: ${listSorted.lastIndex + 1}.")
        }
        "line" -> {
            if (outputF != "Null") {
                outputFile.writeText("Total lines: ${listSorted.lastIndex + 1}.")
            } else println("Total lines: ${listSorted.lastIndex + 1}.")
        }
    }
    if (sortingType == "byCount") {
        val mapSorted = listSorted.groupingBy { it }.eachCount()
        for ((k, v) in mapSorted.toList().sortedBy { (key, value) -> value}.toMap()) {
            if (outputF != "Null") {
                outputFile.appendText("$k: $v time(s), ${100 / mapSorted.size * v}%\n")
            } else println("$k: $v time(s), ${100 / mapSorted.size * v}%")
        }
    } else {
        print("Sorted data: ")
        for (i in listSorted) {
            if (outputF == "Null") print("$i ") else outputFile.appendText("$i ")
        }
    }
}

fun cancelStringLine(type: String, list: MutableList<String>) :MutableList<String> {
    val finalList = mutableListOf<String>()
    return if (type == "long") {
        for (i in list) {
            finalList += try {
                i.toLong().toString()
            } catch (e: Exception) {
                println("\"$i\" is not a long. It will be skipped.")
                continue
            }
        }
        finalList
    } else list
}

