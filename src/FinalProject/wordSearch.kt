package FinalProject

import java.io.File

/************************************************************
 * Name:         Sung Goodman
 * Date:         11/25/2022
 * Assignment:   Final Project - Word Search
 * Class Number: CIS283
 * Description:  Write a program that will read a list of words from data file called “words.txt”,
 *               then return a usable puzzle key and puzzle board.
 ************************************************************/
var wordText = "src/FinalProject/words.txt"
const val puzzle = "src/FinalProject/puzzle.txt"
const val puzzleKey = "src/FinalProject/puzzleKey.txt"

class Puzzle(var row: Int, var col: Int, var filename: String) {
    var words = File(wordText).readLines()
    val board = Array(row) { Array(col) { "." } }
    var descWords = sortedArrayDescending()

    init {
//        for (i in 0 until words.size - 1) {
//            words = listOf(words[i].replace(" ",""))
//        }
    }

    fun displayPuzzleKey(): String {
        return createBoard()
    }

    fun displayPuzzle(): String {
        var allString = ""

        for (word in words) {
            allString += word
        }
        for (row in 0 until board.size) {
            for (col in 0 until board[row].size) {
                val randomLetter = allString.random().uppercaseChar()
                setBoardCell(randomLetter.toString(), row, col, board)
            }
        }
        return createBoard()
    }

    fun displayWordList(): String {
        var retString = ""
        retString += "Find the following 45 words: \n\n"

        for (w in 0..((words.size - 1) / 3)) {
            retString += (words[w * 3].padEnd(25) + words[w * 3 + 1].padEnd(25) + words[w * 3 + 2] + "\n")
        }
        return retString.uppercase()
    }

    fun sortedArrayDescending(): List<String> {
        return words.sortedBy { it.length }.reversed()
    }

    fun setBoardCell(word: String, row: Int, col: Int, board: Array<Array<String>>) {
        if (board[row][col] == ".") {
            board[row][col] = word
        }
    }

    fun placeWord(word: String, row: Int, col: Int, dir: String) {
        var rowDir = row
        var colDir = col
        var letter = 0

        for (w in word) {
            board[rowDir][colDir] = word[letter].uppercaseChar().toString()

            when (dir) {
                "N" -> {
                    rowDir--; letter++
                }
                "NE" -> {
                    rowDir--; colDir++; letter++
                }
                "E" -> {
                    colDir++; letter++
                }
                "SE" -> {
                    rowDir++; colDir++; letter++
                }
                "S" -> {
                    rowDir++; letter++
                }
                "SW" -> {
                    rowDir++; colDir--; letter++
                }
                "W" -> {
                    colDir--; letter++
                }
                "NW" -> {
                    rowDir--; colDir--; letter++
                }
            }
        }
    }

    fun testPlace(word: String, row: Int, col: Int, dir: String): Boolean {
        var found = true
        val cols = board.lastIndex
        val rows = board.lastIndex
        var rowDir = row
        var colDir = col

        for (s in word) {
            if (dir == "N") {
                if (row + 1 > word.length && (board[rowDir][colDir] == "." ||
                            board[rowDir][colDir] == s.toString())
                ) {
                    rowDir--
                } else {
                    found = false
                }

            } else if (dir == "NE") {
                if ((row > col && row + col < board.size && row > word.length ||
                            col > row && row + col > board.size && (board.size - col) > word.length) &&
                    (board[rowDir][colDir] == "." || board[rowDir][colDir] == s.toString())
                ) {
                    rowDir--; colDir++
                } else {
                    found = false
                }

            } else if (dir == "E") {
                if (cols - col > word.length && (board[rowDir][colDir] == "." ||
                            board[rowDir][colDir] == s.toString())
                ) {
                    colDir++
                } else {
                    found = false
                }

            } else if (dir == "SE") {
                if ((row > col && row - (board.size - 1) > word.length ||
                            col > row && col - (board.size - 1) > word.length) &&
                    (board[rowDir][colDir] == "." || board[rowDir][colDir] == s.toString())
                ) {
                    rowDir++; colDir++
                } else {
                    found = false
                }

            } else if (dir == "S") {
                if (rows - row > word.length && (board[rowDir][colDir] == "." ||
                            board[rowDir][colDir] == s.toString())
                ) {
                    rowDir++
                } else {
                    found = false
                }

            } else if (dir == "SW") {
                if ((row > col && row - (board.size - 1) > word.length ||
                            col > row && col - (board.size - 1) > word.length) &&
                    (board[rowDir][colDir] == "." || board[rowDir][colDir] == s.toString())
                ) {
                    rowDir++; colDir--
                } else {
                    found = false
                }

            } else if (dir == "W") {
                if (col + 1 > word.length && (board[rowDir][colDir] == "." ||
                            board[rowDir][colDir] == s.toString())
                ) {
                    colDir--
                } else {
                    found = false
                }

            } else if (dir == "NW") {
                if ((row > col && col + 1 > word.length || col > row && row + 1 > word.length) &&
                    (board[rowDir][colDir] == "." || board[rowDir][colDir] == s.toString())
                ) {
                    rowDir--; colDir--
                } else {
                    found = false
                }
            }
        }
        return found
    }

    fun createPuzzle() {
        var rowDir = 0
        var colDir = 0
        var direction = ""

        for (word in descWords) {
            do {
                rowDir = (board.indices).random()
                colDir = (board.indices).random()
                direction = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW").random()

            } while (!testPlace(word, rowDir, colDir, direction))
            placeWord(word, rowDir, colDir, direction)
        }
    }

    fun createBoard(): String {
        var retString = ""

        for (row: Int in board.indices) {
            for (col: Int in 0 until board[row].size) {
                retString += (board[row][col] + " ")
            }
            retString += "\n"
        }
        return retString
    }

    fun writeToPuzzleText() {
        File(puzzle).printWriter().use { out ->
            out.println(displayPuzzle())
            out.println(displayWordList())
        }
    }

    fun writeToPuzzleKeyText() {
        File(puzzleKey).printWriter().use { out ->
            out.println(displayPuzzleKey())
            out.println(displayWordList())
        }
    }
}

fun main() {
    var puz = Puzzle(45, 45, "src/FinalProject/words.txt")

    puz.createPuzzle()

    println(puz.displayPuzzleKey())
    println(puz.displayWordList())
    puz.writeToPuzzleKeyText()

    println(puz.displayPuzzle())
    println(puz.displayWordList())
    puz.writeToPuzzleText()

//    val start = System.currentTimeMillis()
//    for (i in 1..100) {
//        Puzzle(45, 45, "src/FinalProject/words.txt").createPuzzle()
//    }
//    var totalTime = System.currentTimeMillis() - start
//    println("Total time to create 10 puzzles: $totalTime milli-seconds")
//    println("Average time to create 1 puzzle: ${totalTime / 10} milli-seconds")

}


