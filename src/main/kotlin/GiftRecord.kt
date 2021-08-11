import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.FileReader

/**
 * Represents an item in the Gift Shop
 */
data class GiftRecord(val name: String, val price: Int) {}

/**
 * Represents a pair of items in the gift shop
 * (overrides toString() to provide clean output)
 */
data class GiftCombo(val first: GiftRecord, val second: GiftRecord)
{
    override fun toString(): String {
        return first.name + " " + first.price + ", " + second.name + " " + second.price
    }
}

/**
 * Exception to be thrown when a file format error is encountered
 */
class FileFormatException(s: String) : Throwable(s) {}

/**
 * Exception to be thrown when there is no valid solution
 */
class NoPairFoundException(s: String) : Throwable(s) {}

/**
 * Reads the supplied CSV file, returning a list of GiftRecord objects
 */
fun readGiftListCSV(file: String) : List<GiftRecord>
{
    // Create the IO File readers and CSV parser.
    // Apache Commons CSV library provides the robust CSV implementation

    val reader = FileReader(file)
    val bufferedReader = BufferedReader(reader)

    val parser = CSVParser(bufferedReader, CSVFormat.DEFAULT)

    val gifts = ArrayList<GiftRecord>()

    // Iterate across the recods in the CSV file

    var lastPrice = -1

    for (record in parser.records)
    {
        val line = gifts.size + 1
        // There must be exactly two fields in each row

        if (record.size() != 2)
        {
            throw FileFormatException("Invalid record format on line $line")
        }

        try {

            val name = record.get(0)
            val price = Integer.parseInt(record.get(1).trim())

            // Ensure that the records in the CSV file are in increasing order of price,
            // or the matching algorithm will fail

            if (price < lastPrice)
            {
                throw FileFormatException("Records out of order on line $line")
            }

            gifts.add(GiftRecord(name, price))
            lastPrice = price

        }
        catch(e: NumberFormatException) {
            throw FileFormatException("Invalid numeric format on line $line")
        }
    }

    return gifts
}

/**
 * Finds the first maximal valued pair of gifts at the given combined price.
 * Returns a GiftCombo object containing the two matching GiftRecords
 */
fun findMaxPair(gifts: List<GiftRecord>, price: Int) : GiftCombo
{
    // Initialize our accumulators
    var best = -1
    var bestPair : Pair<Int, Int>? = null

    val end = gifts.size - 1
    // for each of gifts in the list
    for(i in 0..end)
    {
        // If the price of the gift is > 1/2 the target price, bug out
        // (any subsequent items would be valued such that we exceed the target price)
        if (gifts[i].price > price / 2) break

        // Run through the gifts from our current position, calculating the total

        for(j in i..end)
        {
            val combined = gifts[i].price + gifts[j].price

            // If we exceed the target price, stop looking
            // (all subsequent items would also fail)

            if (combined > price) break

            // If this is better than our current best solution, update the accumulators

            if (combined > best)
            {
                best = combined
                bestPair = Pair<Int,Int>(i,j)
            }
        }
    }

    // If we didn't find a valid solution, throw an exception
    if (bestPair == null)
    {
        throw NoPairFoundException("No pair found")
    }

    // Package up the answer and return
    return GiftCombo(gifts[bestPair.first], gifts[bestPair.second])
}

