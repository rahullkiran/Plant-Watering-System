import java.text.SimpleDateFormat;
import java.util.*;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.util.ArrayList;
import edu.princeton.cs.introcs.StdDraw;
import java.util.Date;

public class waterTask extends TimerTask
{
    //Variables
    private final Pin myButton;
    private final Pin myPump;
    private final Pin myMoistureSensor;
    private final SSD1306 theOledObject;
    public static String logReport = null;

    int threshold = 700, i = 0;
    ArrayList<Integer> moisture = new ArrayList<Integer>();
    ArrayList<Integer> seconds = new ArrayList<Integer>();

    //Date formatting
    SimpleDateFormat dateFormat;
    Date date;

    //Initializing Values
    waterTask(Pin myButton, Pin myPump, Pin myMoistureSensor, SSD1306 theOledObject)
            throws java.io.IOException{

        this.myButton = myButton; //Button
        myButton.setMode(Pin.Mode.INPUT);

        this.myPump = myPump; //Pump
        myPump.setMode(Pin.Mode.OUTPUT);

        this.myMoistureSensor = myMoistureSensor; //Moisture Sensor
        myMoistureSensor.setMode(Pin.Mode.ANALOG);

        this.theOledObject = theOledObject; //OLED
        theOledObject.init();
    }

    //Main Watering Loop
    @Override
    public void run() {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        logReport = dateFormat.format(date);
        moisture.add((int) myMoistureSensor.getValue());

    if(moisture.get(i) > threshold){

        theOledObject.getCanvas().drawString(0,1,"Moisture Level: ");
        theOledObject.getCanvas().drawString(90,1,String.valueOf(moisture.get(i)));
        theOledObject.getCanvas().drawString(0,10,"MOISTURE LOW!");
        theOledObject.getCanvas().drawString(0,30,"Pump Status: ");
        theOledObject.getCanvas().drawString(75,30," ON");
        theOledObject.display();
        logReport = logReport + " The moisture level is low (moisture = " + moisture.get(i) + ", water process activated!)";
        System.out.println(logReport);
        try{
            myPump.setValue(1);
        }
        catch(Exception e){
            System.out.println("Pump Exception!!!");
        }
    }
    else{
        theOledObject.getCanvas().drawString(0,1,"Moisture Level: ");
        theOledObject.getCanvas().drawString(90,1,String.valueOf(moisture.get(i)));
        theOledObject.getCanvas().drawString(0,10,"MOISTURE GOOD!");
        theOledObject.getCanvas().drawString(0,30,"Pump Status: ");
        theOledObject.getCanvas().drawString(75,30,"OFF");
        theOledObject.display();
        logReport = logReport+" The moisture level is sufficient (moisture level = " + moisture.get(i) + ")";
        System.out.println(logReport);
        try{
            myPump.setValue(0);
        }
        catch(Exception e){
            System.out.println("Pump Exception!!!");
        }
    }
    i++;
    seconds.add(i);

    //Button press initiates graphing sequence
    if(myButton.getValue() == 1){
        try{
            myPump.setValue(0);
        }
        catch(Exception e){
            System.out.println("Button Exception");
        }
        graph();
    }
    }

    //Graphing
    public void graph(){
        //set up axes
        StdDraw.setXscale(-5, 20);
        StdDraw.setYscale(525,785);

        // pen parameters
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);

        // axes
        StdDraw.line(-1.5,529,-1.5,780); //y line
        StdDraw.line(-1.5,529,20,529); // xline

        // labels
        StdDraw.text(7.5,520.8,"Seconds [x]");
        StdDraw.text(-1.5,520.8,"0");
        StdDraw.text(20,520.8,"20");

        StdDraw.text(-3.68,670,"Moisture");
        StdDraw.text(-3.68,660,"Level [y]");
        StdDraw.text(-3.68,780,"Dry soil");
        StdDraw.text(-3.68,530,"Moist soil");
        StdDraw.text(8,783,"Moisture vs Time Data");

        //Graph drawing
        for (int i = 0; i < seconds.size(); i++) {
            StdDraw.text(seconds.get(i), moisture.get(i), "*");
        }
        try{
            Thread.sleep(10000);
        }
        catch (Exception e){
            System.out.println("Graphing Exception!!!");
        }
    }
}

