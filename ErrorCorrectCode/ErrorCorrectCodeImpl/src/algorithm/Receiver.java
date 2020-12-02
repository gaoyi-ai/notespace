package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Receiver extends Transceiver {

    public Data getData() {
        return data;
    }

    public Receiver(Data data) {
        this.data = data;
    }


    @Override
    public List<Integer> HMParity() {
        HMData hmData = (HMData) this.data;
        int paritySize = hmData.getParity().size();



        List<Integer> computeParities = new ArrayList<>();
        int bit = 0;
        int[] d = hmData.getDataWithParity().stream().mapToInt(Integer::intValue).toArray();
        for (int i = 0; i < paritySize; i++) {
            int parityIdx = (1 << bit++);
            computeParities.add(0,
                    getHMParity(d, d[parityIdx - 1], parityIdx));
        }
        return computeParities;
    }

    /**
     * only work correct for 1 bit error
     * @return error position (index from 0)
     * An error occurs at index 0 and no error, the return value is same
     * index 0 is irrelevant, because it is a redundant code
     */
    public int HMParityDirectFixError() {
        HMData hmData = (HMData) this.data;
        List<Integer> HMCode = hmData.getDataWithParity();
        int hmSize = HMCode.size();
        int pos = IntStream.range(0, hmSize)
                .filter(idx -> HMCode.get(idx) == 1)
                .reduce((x, y) -> x ^ y)
                .getAsInt();
        return pos;
    }

}
