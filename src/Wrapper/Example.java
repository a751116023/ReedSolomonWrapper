/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Wrapper;

/**
 *
 * @author Wai Pai Lee
 */
public class Example {
    public static void main(String [] arguments) {
        EncoderWrapper encoderWrapper = EncoderWrapper.createEncoder();
        
        int [] testData = 
        {
            11, 1, 1, 1, 2, 3, 2, 3, 4, 5, 10, 80, 90, 100,
        };
        
        byte [][] output = new byte[1][1];
        
        try {
            output = encoderWrapper.encode(testData);
        } 
        catch(EncoderWrapper.EightBitOverflowException e) {
            System.out.println(e);
        }
        
        System.out.print("TestData: { ");
        
        for(int elem : testData) {
            System.out.print(elem);
            System.out.print(", ");
        }
        
        System.out.println("}");
        
        System.out.print("Encoded Data: { ");
        
        for(byte[] elem : output) {
            if(elem == null) continue;
            for(byte elem2 : elem) {
                System.out.print(elem2);
                System.out.print(", ");
            }
        }
        
        output[1] = null;
        
        DecoderWrapper decoderWrapper = DecoderWrapper.createDecoder();
        
        int [] testDataOut = decoderWrapper.decode(output);
        
        System.out.println("}");
        
        System.out.print("Corrupted Data: { ");
        
        for(byte[] elem : output) {
            if(elem == null) continue;
            for(byte elem2 : elem) {
                System.out.print(elem2);
                System.out.print(", ");
            }
        }
        
        System.out.println("}");
        
        System.out.print("TestDataOut: { ");
        
        for(int elem : testDataOut) {
            System.out.print(elem);
            System.out.print(", ");
        }
        
        System.out.println("}");
    }
}
