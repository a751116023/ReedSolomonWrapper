package Wrapper;

import Algorithm.ReedSolomon;

/**
 *
 * @author Wai Pai Lee
 */
public class EncoderWrapper {
    public static EncoderWrapper createEncoder() {
        if(encoderInstance == null) {
            // Create Wrapper with default value
            encoderInstance = new EncoderWrapper();
        }
        
        return encoderInstance;
    }
    
    public byte [][] encode(int[] dataToBeEncoded) throws EightBitOverflowException{  
        for(int data : dataToBeEncoded) {
            if(data >= MAX_VALUE_PER_DATA) {
                throw new EightBitOverflowException("EightBitOverflowException: Error Max Value Cannot Exeed " + MAX_VALUE_PER_DATA);
            }
        }
        
        storedSize = dataToBeEncoded.length + BYTE_IN_INT;
        shardSize = (storedSize + dataShardNum - 1) / dataShardNum;
        
        byte [] [] shards = convertDataIntoByteShards(dataToBeEncoded);
        
        ReedSolomon reedSolomon = ReedSolomon.create(dataShardNum, parityShardNum);
        reedSolomon.encodeParity(shards, 0, shardSize);
        
        return shards;
    }
    
    public void setShardParam(int dataShardNum, int parityShardNum) {
        setDataShard(dataShardNum);
        setParityShardNum(parityShardNum);
        
        calculateTotalShard();
    }
    
    public void setDataShard(int dataShardNum) {
        this.dataShardNum = dataShardNum;
        
        calculateTotalShard();
    }
    
    public void setParityShardNum(int parityShardNum) {
        this.parityShardNum = parityShardNum;
        
        calculateTotalShard();
    }
    
    public int getDataShard() {
        return dataShardNum;
    }
    
    public int getParityShardNum() {
        return parityShardNum;
    }
    
    private EncoderWrapper() {
        // Default Value
        this.dataShardNum = 1;
        this.parityShardNum = 0;
        
        calculateTotalShard();
    }
    
    private void calculateTotalShard() {
        totalShard = dataShardNum + parityShardNum;
    }
    
    private byte [][] convertDataIntoByteShards(int [] inputData) {
        byte [] byteData = new byte[inputData.length];
        byte [] [] shards = new byte [totalShard] [shardSize];
        
        for(int i = 0; i < inputData.length; i++) {
            byteData[i] = (byte)inputData[i];
        }
        
        for (int i = 0; i < dataShardNum; i++) {
            System.arraycopy(byteData, i * shardSize, shards[i], 0, shardSize);
        }
        
        return shards;
    }
    
    final class EightBitOverflowException extends Exception {
        public EightBitOverflowException(String message) {
            super(message);
        }
    }
    
    private int dataShardNum;
    private int parityShardNum;    
    private int totalShard;
    
    private int storedSize;
    private int shardSize;
    
    private final int BYTE_IN_INT = 4;
    private final int MAX_VALUE_PER_DATA = 256;
    
    private static EncoderWrapper encoderInstance;
    
}
