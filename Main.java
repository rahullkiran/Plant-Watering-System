import java.io.IOException;
import java.util.Date;
import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.util.Timer; // Timer

public class Main {
    public static void main(String[] args)
            throws IOException, InterruptedException {
            //Initialize board
            String myUSB = "/dev/cu.usbserial-0001"; // Define USB Connection
            IODevice myGroveBoard = new FirmataDevice(myUSB); // Create a FirmataDevice object with a USB connection.
            myGroveBoard.start(); myGroveBoard.ensureInitializationIsDone(); // Start up the FirmataDevice object.

            //Initialize pins
            var myMoistureSensor = myGroveBoard.getPin(16); //Moisture Sensor
            var myPump = myGroveBoard.getPin(7); //Pump
            var myButton = myGroveBoard.getPin(6); //Button

            //OLED DISPLAY
            I2CDevice i2cObject = myGroveBoard.getI2CDevice((byte) 0x3C); // Use 0x3C for the Grove OLED
            SSD1306 theOledObject = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64); // 128x64 OLED SSD1515

            //Main Watering Task
            Timer t = new Timer();
            var task = new waterTask(myButton, myPump, myMoistureSensor, theOledObject);
            System.out.println("COMMENCE!!!");
            t.schedule(task, new Date(), 1500); //Scheduled to operate every 1.5 seconds
    }
}
