package test;

import org.example.main.Sensor;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class SensorTest {

    @Test
    public void TestSensorGeneration(){
        double startRange = -3266.8, endRange = 3266.8;
        Sensor testSensor = new Sensor(startRange,endRange,10,"C","Water temperature sensor.");

        for(int i = 0; i < 10; ++i)
            assertTrue(testSensor.getValue() >= startRange && testSensor.getValue() <= endRange);

        // Another case

        startRange = -100;
        endRange = 10000;
        testSensor = new Sensor(startRange,endRange,10,"C","Water temperature sensor.");

        for(int i = 0; i < 10; ++i)
            assertTrue(testSensor.getValue() >= startRange && testSensor.getValue() <= endRange);
    }
}
