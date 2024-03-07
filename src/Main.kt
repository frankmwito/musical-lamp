interface Stack<T: Any>{
    fun pop(): T?
    fun push(element: T)
    fun peek(): T?
    val count : Int
        val isEmpty: Boolean
        get() = count == 0
}
class StackImpl<T: Any>:Stack<T>{
    private val storage = arrayListOf<T>()

    override val count: Int
        get() = storage.size
    override fun pop(): T? {
        return storage.removeLastOrNull()
    }

    override fun peek(): T? {
        return storage.lastOrNull()
    }

    override fun push(element: T) {
        storage.add(element)
    }
}

fun String.infixToPostfix(): String {
    val stack = StackImpl<Char>()
    val result = StringBuilder()

    for (char in this) {
        when {
            char.isLetterOrDigit() -> result.append(char)
            char == '(' -> stack.push(char)
            char == ')' -> {
                while (!stack.isEmpty && stack.peek() != '(') {
                    result.append(stack.pop())
                }
                stack.pop() // Pop the '('
            }
            else -> { // Operator
                while (!stack.isEmpty && stack.peek()?.let { getPrecedence(it) }!! >= getPrecedence(char)) {
                    result.append(stack.pop())
                }
                stack.push(char)
            }
        }
    }

    while (!stack.isEmpty) {
        result.append(stack.pop())
    }

    return result.toString()
}

fun getPrecedence(operator: Char): Int {
    return when (operator) {
        '+', '-' -> 1
        '*', '/' -> 2
        else -> 0
    }
}

fun main() {
    val infixExpression1 = "2 + 3 * 4"
    val infixExpression2 = "(5 + 2) * 3 - 8 / 2"
    val infixExpression3 = "a + b * (c - d) / e"

    println(infixExpression1.infixToPostfix()) // Should print "234*+"
    println(infixExpression2.infixToPostfix()) // Should print "52+3*82/-"
    println(infixExpression3.infixToPostfix()) // Should print "abc-d*+e/"
}

