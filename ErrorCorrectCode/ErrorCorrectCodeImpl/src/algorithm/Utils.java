package algorithm;

import java.util.*;

public class Utils {

    static int bitNumber = Transmitting.bitNumber;

    public static int binary2decimal(List<Integer> binary) {
        int decimal = 0;
        for (int i = binary.size() - 1,j=0; i >= 0; i--,j++) {
            int value = binary.get(i);
            decimal += value * (1 << j);
        }
        return decimal;
    }

    public static List<Integer> getRandom() {
        int decimal = new Random().nextInt(1 << bitNumber);
        List<Integer> random = new ArrayList<>();
        int curBit = bitNumber - 1;
        while (decimal != 0) {
            int round = (1 << curBit);
            int var = decimal / round;
            random.add(var);
            decimal %= round;
            curBit--;
        }

        while (random.size() != bitNumber) random.add(0);
        return random;
    }

    private static void noise(Data data, int noiseBitNumber) {
        Set<Integer> errorBits = new HashSet<>();
        int paritySize = data.getParity().size();
        int range = bitNumber + paritySize;
        for (int i = 0; i < noiseBitNumber; i++) {
            // error in transmitting
            data.setCorrect(false);
            int bit = checkDuplicate(errorBits, new Random().nextInt(range), range);
            if (bit >= bitNumber) {// error in parity
                int offset = bit - bitNumber;
                List<Integer> parity = data.getParity();
                Integer curParity = parity.get(offset);
                parity.set(offset, 1 - curParity);
                data.setParity(parity);
            } else {// error in data
                Integer integer = data.getData().get(bit);
                data.getData().set(bit, 1 - integer);
            }
        }
    }

    /**
     * no duplicate error in data and parity
     * @return unique new error bit
     */
    private static int checkDuplicate(Set<Integer> errorBits, int bit, int range) {
        while (errorBits.contains(bit)) bit = new Random().nextInt(range);
        errorBits.add(bit);
        return bit;
    }

    private static void noise4HM(HMData data, int noiseBitNumber) {
        int totalSize = data.getDataWithParity().size();
        Set<Integer> errorBits = new HashSet<>();
        for (int i = 0; i < noiseBitNumber; i++) {
            // error in transmitting
            data.setCorrect(false);
            int bit = checkDuplicate(errorBits, new Random().nextInt(totalSize), totalSize);
            Integer integer = data.getDataWithParity().get(bit);
            data.getDataWithParity().set(bit, 1 - integer);
        }
    }

    public static void transmit(Data data) {
        int bits = new Random().nextInt(3);
        if (data instanceof HMData) noise4HM((HMData) data, bits);
        else noise(data, bits);
    }
}
