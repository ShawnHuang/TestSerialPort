import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialComm {

  public SerialComm() {
    super();
  }

  void connect( String portName ) throws Exception {
    CommPortIdentifier portIdentifier = CommPortIdentifier
        .getPortIdentifier( portName );
    if( portIdentifier.isCurrentlyOwned() ) {
      System.out.println( "Error: Port is currently in use" );
    } else {
      int timeout = 3000;
      CommPort commPort = portIdentifier.open( this.getClass().getName(), timeout );

      if( commPort instanceof SerialPort ) {
        SerialPort serialPort = ( SerialPort )commPort;
        serialPort.setSerialPortParams( 9600,   //鮑率(Baud Rate)
                                        SerialPort.DATABITS_8,
                                        SerialPort.STOPBITS_1,
                                        SerialPort.PARITY_NONE );

        byte[] buffer = new byte[ 82 ];
        int len = -1;
        try {
          while( ( len = serialPort.getInputStream().read(buffer) ) > -1 ) {
             System.out.print( new String( buffer, 0, len ) );
          }
        } catch( IOException e ) {
          e.printStackTrace();
        }

      } else {
        System.out.println( "Error: Only serial ports are handled by this example." );
      }
    }
  }

  public static void main( String[] args ) {
    try {
      ( new SerialComm() ).connect( "/dev/ttyUSB0" );  //如接USB的話改成dev/ttyUSB0
    } catch( Exception e ) {
      e.printStackTrace();
    }
  }
}
