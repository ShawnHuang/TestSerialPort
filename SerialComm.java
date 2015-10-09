import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FeatureConvert {
  public FeatureConvert(String input){
      String[] inputs = input.split(",");
      int[] values = new int[6];
      for(int i = 0; i < inputs.length; i++) {
          values[i] = Integer.parseInt(inputs[i]);
      }
      this.vx = values[0];
      this.vy = values[1];
      this.vz = values[2];
      this.ax = values[3];
      this.ay = values[4];
      this.az = values[5];
      this.getValues();
  }
  public void getValues(){
       System.out.println(this.vx+","+this.vy+","+this.vz+","
                            +this.ax+","+this.ay+","+this.az);
  }
  private int vx,vy,vz,ax,ay,az;
  private float fvx,fvy,fvz,fax,fay,faz;
}

public class SerialComm {

  public SerialComm() {
    super();
  }

  void connect( String portName ) throws Exception {

    CommPortIdentifier portIdentifier = CommPortIdentifier
        .getPortIdentifier( portName );

    if( portIdentifier.isCurrentlyOwned() ) {
      System.out.println( "Error: Port is currently in use" );
    }
    else {
      int timeout = 3000;
      CommPort commPort = portIdentifier.open( this.getClass().getName(), timeout );

      if( commPort instanceof SerialPort ) {
        SerialPort serialPort = ( SerialPort )commPort;
        serialPort.setSerialPortParams( 9600,   //鮑率(Baud Rate)
                                        SerialPort.DATABITS_8,
                                        SerialPort.STOPBITS_1,
                                        SerialPort.PARITY_NONE );

        byte[] buffer = new byte[32];
        int len = -1;
        String data ="";
        int i=0;
             String abc="";
             String[] abcs;
             abcs = abc.split("#");
             System.out.println(abcs[0]);
        try {
          while( ( len = serialPort.getInputStream().read(buffer) ) > -1 ) {
             if(i==0) {
               i++;
               continue;
             }
             else if(i==1) i++;
             String str = new String( buffer, 0, len );
             //System.out.println("*************************");
             //System.out.println(str);
             //System.out.println("*************************");

             int idx = str.indexOf("#");
             int count = 0;
             while(idx!=-1){
               count++;
               idx = str.indexOf("#",idx+1);
             }

             String[] strs;
             strs = str.split("#");

             if (count >= 1 && len!=1) {
               if(i!=1){
                 data = data.concat(strs[0]);
                 if (data!="") {
                   //System.out.println("a"+data);
                   FeatureConvert fc = new FeatureConvert(data);
                 }
                 data="";
               }
               if (count == 2) {
                 if(strs.length>=2)
                   data = data.concat(strs[1]);
                 if (data!="") {
                   //System.out.println("b"+data);
                   FeatureConvert fc = new FeatureConvert(data);
                 }
                 data="";
                 if(strs.length==3)
                   data = data.concat(strs[2]);
               }
               else {
                 if(strs.length==2)
                   data = data.concat(strs[1]);
               }
             }
             else {
               if(count == 1) 
               {
                 if (data!="") {
                   //System.out.println("c"+data);
                   FeatureConvert fc = new FeatureConvert(data);
                 }
                 data="";
               }
               else
                 data = data.concat(str);
             }
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
