import com.fazecast.jSerialComm.*;

public class SerialConnection {

    private SerialPort comPort;
    private String input = null;

    public SerialConnection() {
        comPort = SerialPort.getCommPorts()[0];
        comPort.setBaudRate(9600);
        comPort.openPort();

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;

                byte[] newData = new byte[comPort.bytesAvailable()]; //receive incoming bytes
                comPort.readBytes(newData, newData.length); //read incoming bytes
                input = new String(newData); //convert bytes to string
            }
        });
    }

    public void clearInput() {
        input = null;
    }

    public String getInput() {
        return input;
    }

    public void giveOutput(String data) {
        System.out.println(data);
        try {
            byte[] b = data.getBytes();
            comPort.writeBytes(b, b.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
