import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class MainKtTest
{
    @Test
    fun testSimple() {
        assertEquals("Candy Bar 500, Earmuffs 2000", process(arrayOf("test.csv", "2500")))
        assertEquals("Candy Bar 500, Candy Bar 500", process(arrayOf("test.csv", "1000")))
        assertEquals("Bluetooth Stereo 6000, Bluetooth Stereo 6000", process(arrayOf("test.csv", "15000")))
    }

    @Test
    fun testNoSolution() {
        assertEquals("No valid solution found", process(arrayOf("test.csv", "100")))
    }

    @Test
    fun testBadParams()
    {
        assertEquals("Specified file does not exist", process(arrayOf("doesnotexist.csv", "100")))
        assertEquals("Target price must be an integer", process(arrayOf("test.csv", "WOMBAT")))
    }

    @Test
    fun testOrder()
    {
        assertEquals("FileFormatException: Records out of order on line 2", process(arrayOf("test-out-of-order.csv", "2500")))
    }

    @Test
    fun testBadFormat()
    {
        assertEquals("FileFormatException: Invalid numeric format on line 2", process(arrayOf("test-bad-format.csv", "2500")))
        assertEquals("FileFormatException: Invalid record format on line 2", process(arrayOf("test-bad-format-2.csv", "2500")))
    }

    @Test
    fun testFunkyFormat()
    {
        assertEquals("Candy Bar 500, Earmuffs 2000", process(arrayOf("test-funky.csv", "2500")))
    }

    @Test
    fun testZeroRecords()
    {
        assertEquals("No valid solution found", process(arrayOf("test-empty.csv", "2500")))
    }
}