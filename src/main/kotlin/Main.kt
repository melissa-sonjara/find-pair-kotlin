import java.io.FileNotFoundException

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: find-pair-kotlin <file> <price>")
        return
    }

    println(process(args))
}

fun process(args: Array<String>) : String
{
    try {
        val file = args[0]

        val price = Integer.parseInt(args[1])

        println("Scanning $file for $price")

        val gifts = readGiftListCSV(file)

        val count = gifts.size
        println("$count Records found")

        val best = findMaxPair(gifts, price)

        return best.toString()
    }
    catch(e: FileFormatException)
    {
        return e.toString()
    }
    catch(e: NoPairFoundException)
    {
        return "No valid solution found"
    }
    catch(e: NumberFormatException)
    {
        return "Target price must be an integer"
    }
    catch(e: FileNotFoundException)
    {
        return "Specified file does not exist"
    }
}