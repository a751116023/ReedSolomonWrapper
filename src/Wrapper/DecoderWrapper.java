package Wrapper;

import Algorithm.ReedSolomon;

/**
 *
 * @author Wai Pai Lee
 */
public class DecoderWrapper {
    public static DecoderWrapper createDecoder() {
        if(decoderInstance == null) {
            // Create Wrapper with default value
            decoderInstance = new DecoderWrapper();
        }
        
        return decoderInstance;
    }
    
    public int[] decode(byte[][] dataToBeDecode) {
        byte[][] shards = dataToBeDecode.clone();
        
        boolean [] shardPresent = checkIsShardPresented(shards);

        if (shardCount <= dataShardNum) {
            return null;
        }
        
        ReedSolomon reedSolomon = ReedSolomon.create(dataShardNum, parityShardNum);
        reedSolomon.decodeMissing(shards, shardPresent, 0, shardSize);
        
        int [] convertedData = convertByteDataInto2dIntArray(shards);
        
        int [] decodedData = new int [convertedData[BYTE_IN_INT - 1]];
        
        for(int i = BYTE_IN_INT, j = 0; j < convertedData[BYTE_IN_INT - 1]; i++, j++) {
            decodedData[j] = convertedData[i];
        }
        
        return decodedData;
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
    
    private DecoderWrapper() {
        // Default Value
        this.dataShardNum = 4;
        this.parityShardNum = 2;
        
        calculateTotalShard();
    }
    
    private void calculateTotalShard() {
        totalShard = dataShardNum + parityShardNum;
    }
    
    private boolean [] checkIsShardPresented(byte [][] inputData) {
        boolean [] shardPresent = new boolean [totalShard];
        
        shardCount = 0;
        
        for(int i = 0; i < inputData.length; i++) {
            if(inputData[i].length != 0) {
                shardPresent[i] = true;
                shardSize = inputData[i].length;
                shardCount++;
            }
            else {
                shardPresent[i] = false;
            }    
                
        }
        
        return shardPresent;
    }
    
    private int[] convertByteDataInto2dIntArray(byte [][] inputData) {
        byte [] byteData = new byte [shardSize * dataShardNum];
        int[] decodedData = new int [shardSize * dataShardNum];
        
        for(int i = 0; i < dataShardNum; i++) {
            System.arraycopy(inputData[i], 0, byteData, shardSize * i, shardSize);
        }
        
        for(int i = 0; i < byteData.length; i++) {
            decodedData[i] = byteData[i] & 0xff;
        }
        
        return decodedData;
    }
    
    private int dataShardNum;
    private int parityShardNum;    
    private int shardCount;
    private int totalShard;
    
    private int shardSize;
    
    private final int BYTE_IN_INT = 4;
    
    private static DecoderWrapper decoderInstance;
}
